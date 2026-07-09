package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.request.PedidoRequest;
import com.pacifico.repuestos.dto.response.PedidoResponse;
import com.pacifico.repuestos.exception.*;
import com.pacifico.repuestos.model.*;
import com.pacifico.repuestos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository       pedidoRepo;
    private final DetallePedidoRepository detalleRepo;
    private final ClienteRepository       clienteRepo;
    private final ProductoRepository      productoRepo;
    private final InventarioRepository    inventarioRepo;
    private final ZonaDeliveryRepository  zonaRepo;

    // ───── CREAR PEDIDO ─────
    @Transactional
    public PedidoResponse crear(String correoCliente, PedidoRequest req) {

        Cliente cliente = clienteRepo.findByUsuarioId(
            obtenerUsuarioId(correoCliente))
            .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        // Validar stock y construir detalles
        List<DetallePedido> detalles   = new ArrayList<>();
        BigDecimal          subtotal   = BigDecimal.ZERO;

        for (PedidoRequest.ItemPedidoRequest item : req.getItems()) {
            Producto producto = productoRepo.findById(item.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Producto", item.getProductoId()));

            Inventario inv = inventarioRepo.findByProductoId(producto.getId())
                .orElseThrow(() -> new BusinessException(
                    "Sin inventario: " + producto.getNombre()));

            if (inv.getStock() < item.getCantidad())
                throw new BusinessException(
                    "Stock insuficiente para: " + producto.getNombre() +
                    ". Disponible: " + inv.getStock());

            BigDecimal precioUnit = producto.getPrecio();
            BigDecimal sub = precioUnit.multiply(BigDecimal.valueOf(item.getCantidad()));
            subtotal = subtotal.add(sub);

            detalles.add(DetallePedido.builder()
                .producto(producto)
                .cantidad(item.getCantidad())
                .precioUnitario(precioUnit)
                .subtotal(sub)
                .build());
        }

        // Calcular delivery
        BigDecimal costoDelivery = BigDecimal.ZERO;
        ZonaDelivery zona = null;
        if (req.getZonaId() != null) {
            zona = zonaRepo.findById(req.getZonaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "ZonaDelivery", req.getZonaId()));
            costoDelivery = zona.getTarifa();
        }

        BigDecimal total = subtotal.add(costoDelivery);

        // Crear pedido
        Pedido pedido = Pedido.builder()
            .numeroPedido(generarNumero())
            .cliente(cliente)
            .estado(Pedido.EstadoPedido.PENDIENTE.name())
            .subtotal(subtotal)
            .costoDelivery(costoDelivery)
            .total(total)
            .tipoDelivery(req.getTipoDelivery())
            .direccionEntrega(req.getDireccionEntrega())
            .zona(zona)
            .observacion(req.getObservacion())
            .build();

        pedido = pedidoRepo.save(pedido);

        // Guardar detalles (el stock lo descuenta trg_inventario_pedido al confirmar)
        for (DetallePedido d : detalles) {
            d.setPedido(pedido);
            detalleRepo.save(d);
        }

        pedido.setDetalles(detalles);
        return toResponse(pedido);
    }

    // ───── LISTAR PEDIDOS DEL CLIENTE ─────
    @Transactional(readOnly = true)
    public List<PedidoResponse> misPedidos(String correoCliente) {
        Long usuarioId = obtenerUsuarioId(correoCliente);
        Cliente cliente = clienteRepo.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        return pedidoRepo.findByClienteIdOrderByFechaPedidoDesc(cliente.getId())
            .stream().map(this::toResponse).toList();
    }

    // ───── DETALLE DE PEDIDO ─────
    @Transactional(readOnly = true)
    public PedidoResponse obtener(Long id, String correoSolicitante, boolean esAdmin) {
        Pedido pedido = pedidoRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        // Un CLIENTE solo puede ver sus propios pedidos. Se usa la misma excepción
        // que "no encontrado" (en vez de un 403) para no revelar a un cliente que
        // el pedido de otro existe.
        if (!esAdmin && !pedido.getCliente().getUsuario().getCorreo().equals(correoSolicitante)) {
            throw new ResourceNotFoundException("Pedido", id);
        }

        return toResponse(pedido);
    }

    // ───── LISTAR TODOS (ADMIN) ─────
    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos(String estado) {
        List<Pedido> pedidos = estado != null
            ? pedidoRepo.findByEstadoOrderByFechaPedidoDesc(estado)
            : pedidoRepo.findAllByOrderByFechaPedidoDesc();
        return pedidos.stream().map(this::toResponse).toList();
    }

    // ───── CAMBIAR ESTADO (ADMIN) ─────
    @Transactional
    public PedidoResponse cambiarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
        try {
            Pedido.EstadoPedido.valueOf(nuevoEstado);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Estado inválido: " + nuevoEstado);
        }
        pedido.setEstado(nuevoEstado);
        return toResponse(pedidoRepo.save(pedido));
    }

    // ───── HELPERS ─────
    private Long obtenerUsuarioId(String correo) {
        return pedidoRepo.findAll().stream()
            .filter(p -> p.getCliente().getUsuario().getCorreo().equals(correo))
            .map(p -> p.getCliente().getUsuario().getId())
            .findFirst()
            .orElse(clienteRepo.findAll().stream()
                .filter(c -> c.getUsuario().getCorreo().equals(correo))
                .map(c -> c.getUsuario().getId())
                .findFirst()
                .orElseThrow(() -> new BusinessException("Usuario no encontrado")));
    }

    private String generarNumero() {
        String fecha = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = pedidoRepo.count() + 1;
        return String.format("PED-%s-%04d", fecha, count);
    }

    private PedidoResponse toResponse(Pedido p) {
        List<PedidoResponse.DetalleResponse> dets = p.getDetalles() != null
            ? p.getDetalles().stream().map(d -> {
                String img = d.getProducto().getImagenes() != null &&
                    !d.getProducto().getImagenes().isEmpty()
                    ? d.getProducto().getImagenes().get(0).getUrl() : null;
                return PedidoResponse.DetalleResponse.builder()
                    .productoId(d.getProducto().getId())
                    .productoNombre(d.getProducto().getNombre())
                    .productoCodigo(d.getProducto().getCodigo())
                    .imagenUrl(img)
                    .cantidad(d.getCantidad())
                    .precioUnitario(d.getPrecioUnitario())
                    .subtotal(d.getSubtotal())
                    .build();
            }).toList()
            : List.of();

        PedidoResponse.ClienteInfo cli = PedidoResponse.ClienteInfo.builder()
            .id(p.getCliente().getId())
            .nombre(p.getCliente().getUsuario().getNombre())
            .correo(p.getCliente().getUsuario().getCorreo())
            .telefono(p.getCliente().getTelefono())
            .build();

        return PedidoResponse.builder()
            .id(p.getId())
            .numeroPedido(p.getNumeroPedido())
            .estado(p.getEstado())
            .subtotal(p.getSubtotal())
            .costoDelivery(p.getCostoDelivery())
            .total(p.getTotal())
            .tipoDelivery(p.getTipoDelivery())
            .direccionEntrega(p.getDireccionEntrega())
            .zona(p.getZona() != null ? p.getZona().getNombre() : null)
            .observacion(p.getObservacion())
            .fechaPedido(p.getFechaPedido())
            .fechaActualizacion(p.getFechaActualizacion())
            .cliente(cli)
            .detalles(dets)
            .build();
    }
}

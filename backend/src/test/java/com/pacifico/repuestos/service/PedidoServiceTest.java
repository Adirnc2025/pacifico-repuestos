package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.request.PedidoRequest;
import com.pacifico.repuestos.dto.response.PedidoResponse;
import com.pacifico.repuestos.exception.*;
import com.pacifico.repuestos.model.*;
import com.pacifico.repuestos.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoService — Pruebas unitarias")
class PedidoServiceTest {

    @Mock private PedidoRepository       pedidoRepo;
    @Mock private DetallePedidoRepository detalleRepo;
    @Mock private ClienteRepository       clienteRepo;
    @Mock private ProductoRepository      productoRepo;
    @Mock private InventarioRepository    inventarioRepo;
    @Mock private ZonaDeliveryRepository  zonaRepo;

    @InjectMocks
    private PedidoService pedidoService;

    private Usuario     usuarioMock;
    private Cliente     clienteMock;
    private Producto    productoMock;
    private Inventario  inventarioMock;

    @BeforeEach
    void setUp() {
        usuarioMock = Usuario.builder()
            .id(1L).nombre("Juan Pérez").correo("juan@test.com")
            .rol(Usuario.Rol.CLIENTE).activo(true).build();

        clienteMock = Cliente.builder()
            .id(1L).usuario(usuarioMock).telefono("966000000").build();

        productoMock = Producto.builder()
            .id(1L).codigo("EMP-001").nombre("Empaque Motor")
            .precio(new BigDecimal("280.00")).activo(true)
            .imagenes(List.of()).build();

        inventarioMock = Inventario.builder()
            .id(1L).producto(productoMock).stock(10).stockMinimo(5).build();
    }

    private PedidoRequest buildRequest(int cantidad) {
        PedidoRequest req = new PedidoRequest();
        PedidoRequest.ItemPedidoRequest item = new PedidoRequest.ItemPedidoRequest();
        item.setProductoId(1L);
        item.setCantidad(cantidad);
        req.setItems(List.of(item));
        req.setTipoDelivery("RECOJO");
        return req;
    }

    // ── CREAR ──
    @Test
    @DisplayName("Crear pedido exitoso descuenta stock y devuelve número de pedido")
    void crear_exitoso() {
        when(clienteRepo.findByUsuarioId(any())).thenReturn(Optional.of(clienteMock));
        when(clienteRepo.findAll()).thenReturn(List.of(clienteMock));
        when(pedidoRepo.findAll()).thenReturn(List.of());
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(inventarioRepo.findByProductoId(1L)).thenReturn(Optional.of(inventarioMock));
        when(pedidoRepo.count()).thenReturn(0L);
        Pedido pedidoSaved = Pedido.builder()
            .id(1L).numeroPedido("PED-20250101-0001")
            .cliente(clienteMock).estado("PENDIENTE")
            .subtotal(new BigDecimal("280.00"))
            .costoDelivery(BigDecimal.ZERO)
            .total(new BigDecimal("280.00"))
            .tipoDelivery("RECOJO").detalles(List.of()).build();
        when(pedidoRepo.save(any())).thenReturn(pedidoSaved);
        when(detalleRepo.save(any())).thenReturn(new DetallePedido());

        PedidoResponse res = pedidoService.crear("juan@test.com", buildRequest(2));

        assertThat(res).isNotNull();
        assertThat(res.getEstado()).isEqualTo("PENDIENTE");
        verify(detalleRepo).save(any(DetallePedido.class));
    }

    @Test
    @DisplayName("Crear pedido lanza excepción si stock insuficiente")
    void crear_stockInsuficiente_lanzaExcepcion() {
        inventarioMock.setStock(1);
        when(clienteRepo.findAll()).thenReturn(List.of(clienteMock));
        when(pedidoRepo.findAll()).thenReturn(List.of());
        when(clienteRepo.findByUsuarioId(any())).thenReturn(Optional.of(clienteMock));
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(inventarioRepo.findByProductoId(1L)).thenReturn(Optional.of(inventarioMock));

        assertThatThrownBy(() ->
            pedidoService.crear("juan@test.com", buildRequest(5)))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Stock insuficiente");

        verify(pedidoRepo, never()).save(any());
        verify(detalleRepo, never()).save(any());
    }

    @Test
    @DisplayName("Crear pedido lanza excepción si producto no existe")
    void crear_productoNoExiste_lanzaExcepcion() {
        when(clienteRepo.findAll()).thenReturn(List.of(clienteMock));
        when(pedidoRepo.findAll()).thenReturn(List.of());
        when(clienteRepo.findByUsuarioId(any())).thenReturn(Optional.of(clienteMock));
        when(productoRepo.findById(99L)).thenReturn(Optional.empty());

        PedidoRequest req = buildRequest(1);
        req.getItems().get(0).setProductoId(99L);

        assertThatThrownBy(() -> pedidoService.crear("juan@test.com", req))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Crear pedido con zona suma costo de delivery al total")
    void crear_conZonaDelivery_sumaDelivery() {
        ZonaDelivery zona = ZonaDelivery.builder()
            .id(1L).nombre("Lima").tipo("INTERPROVINCIAL")
            .tarifa(new BigDecimal("30.00")).activo(true).build();

        when(clienteRepo.findAll()).thenReturn(List.of(clienteMock));
        when(pedidoRepo.findAll()).thenReturn(List.of());
        when(clienteRepo.findByUsuarioId(any())).thenReturn(Optional.of(clienteMock));
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(inventarioRepo.findByProductoId(1L)).thenReturn(Optional.of(inventarioMock));
        when(zonaRepo.findById(1L)).thenReturn(Optional.of(zona));
        when(pedidoRepo.count()).thenReturn(0L);

        Pedido pedidoSaved = Pedido.builder()
            .id(1L).numeroPedido("PED-20250101-0001")
            .cliente(clienteMock).estado("PENDIENTE")
            .subtotal(new BigDecimal("280.00"))
            .costoDelivery(new BigDecimal("30.00"))
            .total(new BigDecimal("310.00"))
            .tipoDelivery("INTERPROVINCIAL").detalles(List.of()).build();
        when(pedidoRepo.save(any())).thenReturn(pedidoSaved);
        when(detalleRepo.save(any())).thenReturn(new DetallePedido());

        PedidoRequest req = buildRequest(1);
        req.setTipoDelivery("INTERPROVINCIAL");
        req.setZonaId(1L);

        PedidoResponse res = pedidoService.crear("juan@test.com", req);

        assertThat(res.getTotal()).isEqualByComparingTo(new BigDecimal("310.00"));
        assertThat(res.getCostoDelivery()).isEqualByComparingTo(new BigDecimal("30.00"));
    }

    // ── CAMBIAR ESTADO ──
    @Test
    @DisplayName("Cambiar estado a valor válido actualiza el pedido")
    void cambiarEstado_valido() {
        Pedido pedido = Pedido.builder()
            .id(1L).numeroPedido("PED-001")
            .cliente(clienteMock).estado("PENDIENTE")
            .subtotal(BigDecimal.TEN).costoDelivery(BigDecimal.ZERO)
            .total(BigDecimal.TEN).tipoDelivery("RECOJO")
            .detalles(List.of()).build();

        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepo.save(any())).thenReturn(pedido);

        PedidoResponse res = pedidoService.cambiarEstado(1L, "CONFIRMADO");

        assertThat(pedido.getEstado()).isEqualTo("CONFIRMADO");
        verify(pedidoRepo).save(pedido);
    }

    @Test
    @DisplayName("Cambiar estado a valor inválido lanza excepción")
    void cambiarEstado_invalido_lanzaExcepcion() {
        Pedido pedido = Pedido.builder()
            .id(1L).estado("PENDIENTE").cliente(clienteMock)
            .subtotal(BigDecimal.TEN).costoDelivery(BigDecimal.ZERO)
            .total(BigDecimal.TEN).tipoDelivery("RECOJO")
            .detalles(List.of()).build();
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));

        assertThatThrownBy(() -> pedidoService.cambiarEstado(1L, "ESTADO_INVALIDO"))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Estado inválido");
    }

    @Test
    @DisplayName("Cambiar estado lanza excepción si pedido no existe")
    void cambiarEstado_pedidoNoExiste_lanzaExcepcion() {
        when(pedidoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.cambiarEstado(99L, "CONFIRMADO"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── OBTENER ──
    @Test
    @DisplayName("Obtener pedido por id existente devuelve datos correctos")
    void obtener_existe() {
        Pedido pedido = Pedido.builder()
            .id(1L).numeroPedido("PED-001").cliente(clienteMock)
            .estado("PENDIENTE").subtotal(new BigDecimal("280.00"))
            .costoDelivery(BigDecimal.ZERO).total(new BigDecimal("280.00"))
            .tipoDelivery("RECOJO").detalles(List.of()).build();

        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoResponse res = pedidoService.obtener(1L, "juan@test.com", false);

        assertThat(res.getNumeroPedido()).isEqualTo("PED-001");
        assertThat(res.getEstado()).isEqualTo("PENDIENTE");
    }

    @Test
    @DisplayName("Obtener pedido lanza excepción si no existe")
    void obtener_noExiste_lanzaExcepcion() {
        when(pedidoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.obtener(99L, "juan@test.com", false))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Cliente no puede ver el pedido de otro cliente")
    void obtener_clienteVePedidoAjeno_lanzaExcepcion() {
        Pedido pedido = Pedido.builder()
            .id(1L).numeroPedido("PED-001").cliente(clienteMock)
            .estado("PENDIENTE").subtotal(new BigDecimal("280.00"))
            .costoDelivery(BigDecimal.ZERO).total(new BigDecimal("280.00"))
            .tipoDelivery("RECOJO").detalles(List.of()).build();

        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));

        assertThatThrownBy(() -> pedidoService.obtener(1L, "otro@test.com", false))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Admin puede ver el pedido de cualquier cliente")
    void obtener_adminVeCualquierPedido_exitoso() {
        Pedido pedido = Pedido.builder()
            .id(1L).numeroPedido("PED-001").cliente(clienteMock)
            .estado("PENDIENTE").subtotal(new BigDecimal("280.00"))
            .costoDelivery(BigDecimal.ZERO).total(new BigDecimal("280.00"))
            .tipoDelivery("RECOJO").detalles(List.of()).build();

        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoResponse res = pedidoService.obtener(1L, "admin@pacificorepuestos.com", true);

        assertThat(res.getNumeroPedido()).isEqualTo("PED-001");
    }
}

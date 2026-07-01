package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.request.*;
import com.pacifico.repuestos.dto.response.*;
import com.pacifico.repuestos.exception.*;
import com.pacifico.repuestos.model.*;
import com.pacifico.repuestos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository       productoRepo;
    private final CategoriaRepository      categoriaRepo;
    private final CompatibilidadRepository compatRepo;
    private final ImagenProductoRepository imagenRepo;
    private final MotorRepository          motorRepo;

    // ───── LISTAR / BUSCAR ─────
    public List<ProductoResponse> listarDestacados() {
        return productoRepo.findByActivoTrueAndDestacadoTrue()
            .stream().map(this::toResponse).toList();
    }

    public List<ProductoResponse> filtrar(Long marcaId, Long modeloId,
                                           Long generacionId, Long motorId,
                                           Long categoriaId) {
        // Si no hay filtros de vehículo, evitar el INNER JOIN de compatibilidades
        // que excluiría productos sin compatibilidades registradas
        if (marcaId == null && modeloId == null && generacionId == null && motorId == null) {
            if (categoriaId != null) {
                return productoRepo.findByActivoTrueAndCategoriaId(categoriaId)
                    .stream().map(this::toResponse).toList();
            }
            return productoRepo.findByActivoTrue()
                .stream().map(this::toResponse).toList();
        }
        return productoRepo.filtrar(marcaId, modeloId, generacionId, motorId, categoriaId)
            .stream().map(this::toResponse).toList();
    }

    public List<ProductoResponse> buscar(String q) {
        if (q == null || q.trim().length() < 3)
            throw new BusinessException("Ingresa al menos 3 caracteres para buscar");
        return productoRepo.buscarPorNombreOCodigo(q.trim())
            .stream().map(this::toResponse).toList();
    }

    public ProductoResponse obtenerPorId(Long id) {
        return toResponse(productoRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto", id)));
    }

    // ───── CRUD ADMIN ─────
    @Transactional
    public ProductoResponse crear(ProductoRequest req) {
        if (productoRepo.existsByCodigo(req.getCodigo()))
            throw new BusinessException("Ya existe un producto con ese código");

        Categoria categoria = req.getCategoriaId() != null
            ? categoriaRepo.findById(req.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", req.getCategoriaId()))
            : null;

        Producto producto = Producto.builder()
            .codigo(req.getCodigo())
            .nombre(req.getNombre())
            .descripcion(req.getDescripcion())
            .precio(req.getPrecio())
            .medidas(req.getMedidas())
            .destacado(req.getDestacado())
            .activo(true)
            .categoria(categoria)
            .build();

        producto = productoRepo.save(producto);

        Inventario inv = Inventario.builder()
            .producto(producto)
            .stock(req.getStock() != null ? req.getStock() : 0)
            .stockMinimo(5)
            .build();

        producto.setInventario(inv);
        return toResponse(productoRepo.save(producto));
    }

    @Transactional
    public ProductoResponse editar(Long id, ProductoRequest req) {
        Producto producto = productoRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto", id));

        if (!producto.getCodigo().equals(req.getCodigo())
                && productoRepo.existsByCodigo(req.getCodigo()))
            throw new BusinessException("Ya existe un producto con ese código");

        Categoria categoria = req.getCategoriaId() != null
            ? categoriaRepo.findById(req.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", req.getCategoriaId()))
            : null;

        producto.setCodigo(req.getCodigo());
        producto.setNombre(req.getNombre());
        producto.setDescripcion(req.getDescripcion());
        producto.setPrecio(req.getPrecio());
        producto.setMedidas(req.getMedidas());
        producto.setDestacado(req.getDestacado());
        producto.setCategoria(categoria);

        if (producto.getInventario() != null && req.getStock() != null)
            producto.getInventario().setStock(req.getStock());

        return toResponse(productoRepo.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = productoRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        producto.setActivo(false);
        productoRepo.save(producto);
    }

    // ───── COMPATIBILIDADES ─────
    @Transactional
    public void agregarCompatibilidad(Long productoId, CompatibilidadRequest req) {
        Producto producto = productoRepo.findById(productoId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto", productoId));
        Motor motor = motorRepo.findById(req.getMotorId())
            .orElseThrow(() -> new ResourceNotFoundException("Motor", req.getMotorId()));

        if (compatRepo.existsByProductoIdAndMotorId(productoId, req.getMotorId()))
            throw new BusinessException("Esta compatibilidad ya existe");

        compatRepo.save(Compatibilidad.builder()
            .producto(producto).motor(motor)
            .observacion(req.getObservacion()).build());
    }

    @Transactional
    public void eliminarCompatibilidad(Long productoId, Long motorId) {
        if (!compatRepo.existsByProductoIdAndMotorId(productoId, motorId))
            throw new ResourceNotFoundException("Compatibilidad no encontrada");
        compatRepo.deleteByProductoIdAndMotorId(productoId, motorId);
    }

    // ───── IMÁGENES ─────
    @Transactional
    public void agregarImagen(Long productoId, String url, boolean esPrincipal) {
        Producto producto = productoRepo.findById(productoId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto", productoId));
        int orden = imagenRepo.findByProductoIdOrderByOrdenAsc(productoId).size();
        imagenRepo.save(ImagenProducto.builder()
            .producto(producto).url(url)
            .esPrincipal(esPrincipal).orden(orden).build());
    }

    @Transactional
    public void eliminarImagen(Long imagenId) {
        if (!imagenRepo.existsById(imagenId))
            throw new ResourceNotFoundException("Imagen no encontrada");
        imagenRepo.deleteById(imagenId);
    }

    // ───── MAPPER ─────
    private ProductoResponse toResponse(Producto p) {
        List<String> urls = p.getImagenes() != null
            ? p.getImagenes().stream().map(ImagenProducto::getUrl).toList()
            : List.of();

        String principal = p.getImagenes() != null ? p.getImagenes().stream()
            .filter(i -> Boolean.TRUE.equals(i.getEsPrincipal()))
            .map(ImagenProducto::getUrl).findFirst()
            .orElse(urls.isEmpty() ? null : urls.get(0)) : null;

        List<CompatibilidadResponse> compats = p.getCompatibilidades() != null
            ? p.getCompatibilidades().stream().map(c -> {
                Motor m = c.getMotor();
                Generacion g = m.getGeneracion();
                return CompatibilidadResponse.builder()
                    .compatibilidadId(c.getId())
                    .motorId(m.getId())
                    .motorCodigo(m.getCodigo())
                    .motorDescripcion(m.getDescripcion())
                    .generacion(g.getNombre())
                    .modelo(g.getModelo().getNombre())
                    .marca(g.getModelo().getMarca().getNombre())
                    .observacion(c.getObservacion())
                    .build();
            }).toList()
            : List.of();

        return ProductoResponse.builder()
            .id(p.getId())
            .codigo(p.getCodigo())
            .nombre(p.getNombre())
            .descripcion(p.getDescripcion())
            .precio(p.getPrecio())
            .medidas(p.getMedidas())
            .destacado(p.getDestacado())
            .categoria(p.getCategoria() != null ? p.getCategoria().getNombre() : null)
            .categoriaId(p.getCategoria() != null ? p.getCategoria().getId() : null)
            .stock(p.getInventario() != null ? p.getInventario().getStock() : 0)
            .imagenes(urls)
            .imagenPrincipal(principal)
            .compatibilidades(compats)
            .build();
    }
}

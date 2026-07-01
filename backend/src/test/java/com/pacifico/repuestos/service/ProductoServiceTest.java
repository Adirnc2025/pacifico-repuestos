package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.request.*;
import com.pacifico.repuestos.dto.response.ProductoResponse;
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
@DisplayName("ProductoService — Pruebas unitarias")
class ProductoServiceTest {

    @Mock private ProductoRepository       productoRepo;
    @Mock private CategoriaRepository      categoriaRepo;
    @Mock private CompatibilidadRepository compatRepo;
    @Mock private ImagenProductoRepository imagenRepo;
    @Mock private MotorRepository          motorRepo;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoMock;
    private ProductoRequest request;

    @BeforeEach
    void setUp() {
        Categoria cat = Categoria.builder().id(1L).nombre("Motor").build();
        Inventario inv = Inventario.builder().stock(10).build();

        productoMock = Producto.builder()
            .id(1L).codigo("EMP-001").nombre("Empaque Motor")
            .precio(new BigDecimal("280.00")).activo(true)
            .destacado(false).categoria(cat).inventario(inv)
            .imagenes(List.of()).compatibilidades(List.of())
            .build();

        request = new ProductoRequest();
        request.setCodigo("EMP-002");
        request.setNombre("Empaque Culata");
        request.setPrecio(new BigDecimal("95.00"));
        request.setStock(8);
        request.setCategoriaId(1L);
    }

    // ── CREAR ──
    @Test
    @DisplayName("Crear producto exitosamente")
    void crear_exitoso() {
        when(productoRepo.existsByCodigo("EMP-002")).thenReturn(false);
        when(categoriaRepo.findById(1L)).thenReturn(
            Optional.of(Categoria.builder().id(1L).nombre("Motor").build()));
        when(productoRepo.save(any())).thenReturn(productoMock);

        ProductoResponse res = productoService.crear(request);

        assertThat(res).isNotNull();
        verify(productoRepo).existsByCodigo("EMP-002");
        verify(productoRepo, times(2)).save(any());
    }

    @Test
    @DisplayName("Crear producto lanza excepción si código ya existe")
    void crear_codigoDuplicado_lanzaExcepcion() {
        when(productoRepo.existsByCodigo("EMP-002")).thenReturn(true);

        assertThatThrownBy(() -> productoService.crear(request))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Ya existe un producto con ese código");

        verify(productoRepo, never()).save(any());
    }

    @Test
    @DisplayName("Crear lanza excepción si la categoría no existe")
    void crear_categoriaNoExiste_lanzaExcepcion() {
        when(productoRepo.existsByCodigo(any())).thenReturn(false);
        when(categoriaRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.crear(request))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── OBTENER ──
    @Test
    @DisplayName("Obtener producto por id existente")
    void obtenerPorId_existe() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));

        ProductoResponse res = productoService.obtenerPorId(1L);

        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getCodigo()).isEqualTo("EMP-001");
        assertThat(res.getStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("Obtener producto lanza excepción si no existe")
    void obtenerPorId_noExiste_lanzaExcepcion() {
        when(productoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.obtenerPorId(99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── EDITAR ──
    @Test
    @DisplayName("Editar producto actualiza los campos correctamente")
    void editar_exitoso() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepo.existsByCodigo("EMP-002")).thenReturn(false);
        when(categoriaRepo.findById(1L)).thenReturn(
            Optional.of(Categoria.builder().id(1L).nombre("Motor").build()));
        when(productoRepo.save(any())).thenReturn(productoMock);

        ProductoResponse res = productoService.editar(1L, request);

        assertThat(res).isNotNull();
        verify(productoRepo).save(any());
    }

    // ── ELIMINAR ──
    @Test
    @DisplayName("Eliminar producto lo marca como inactivo")
    void eliminar_marcaComoInactivo() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepo.save(any())).thenReturn(productoMock);

        productoService.eliminar(1L);

        assertThat(productoMock.getActivo()).isFalse();
        verify(productoRepo).save(productoMock);
    }

    @Test
    @DisplayName("Eliminar lanza excepción si producto no existe")
    void eliminar_noExiste_lanzaExcepcion() {
        when(productoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.eliminar(99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── BUSCAR ──
    @Test
    @DisplayName("Buscar con menos de 3 caracteres lanza excepción")
    void buscar_menosDe3Chars_lanzaExcepcion() {
        assertThatThrownBy(() -> productoService.buscar("em"))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("3 caracteres");
    }

    @Test
    @DisplayName("Buscar con 3 o más caracteres devuelve resultados")
    void buscar_exitoso() {
        when(productoRepo.buscarPorNombreOCodigo("emp"))
            .thenReturn(List.of(productoMock));

        List<ProductoResponse> res = productoService.buscar("emp");

        assertThat(res).hasSize(1);
        assertThat(res.get(0).getNombre()).isEqualTo("Empaque Motor");
    }

    // ── COMPATIBILIDADES ──
    @Test
    @DisplayName("Agregar compatibilidad existente lanza excepción")
    void agregarCompatibilidad_yaExiste_lanzaExcepcion() {
        CompatibilidadRequest req = new CompatibilidadRequest();
        req.setMotorId(1L);

        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(motorRepo.findById(1L)).thenReturn(
            Optional.of(Motor.builder().id(1L).codigo("2GD-FTV").build()));
        when(compatRepo.existsByProductoIdAndMotorId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> productoService.agregarCompatibilidad(1L, req))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("ya existe");
    }

    @Test
    @DisplayName("Agregar compatibilidad nueva se guarda correctamente")
    void agregarCompatibilidad_nueva_exitoso() {
        CompatibilidadRequest req = new CompatibilidadRequest();
        req.setMotorId(1L);

        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(motorRepo.findById(1L)).thenReturn(
            Optional.of(Motor.builder().id(1L).codigo("2GD-FTV").build()));
        when(compatRepo.existsByProductoIdAndMotorId(1L, 1L)).thenReturn(false);

        productoService.agregarCompatibilidad(1L, req);

        verify(compatRepo).save(any(Compatibilidad.class));
    }

    @Test
    @DisplayName("Agregar compatibilidad lanza excepción si motor no existe")
    void agregarCompatibilidad_motorNoExiste_lanzaExcepcion() {
        CompatibilidadRequest req = new CompatibilidadRequest();
        req.setMotorId(99L);

        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(motorRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.agregarCompatibilidad(1L, req))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── LISTAR / FILTRAR ──
    @Test
    @DisplayName("Listar destacados devuelve lista correcta")
    void listarDestacados_devuelveListaCorrecta() {
        when(productoRepo.findByActivoTrueAndDestacadoTrue()).thenReturn(List.of(productoMock));

        List<ProductoResponse> res = productoService.listarDestacados();

        assertThat(res).hasSize(1);
        assertThat(res.get(0).getCodigo()).isEqualTo("EMP-001");
    }

    @Test
    @DisplayName("Filtrar delega al repositorio y mapea resultados")
    void filtrar_delegaAlRepositorio() {
        when(productoRepo.filtrar(any(), any(), any(), any(), any()))
            .thenReturn(List.of(productoMock));

        List<ProductoResponse> res = productoService.filtrar(1L, null, null, null, null);

        assertThat(res).hasSize(1);
        verify(productoRepo).filtrar(1L, null, null, null, null);
    }

    // ── EDITAR ramas adicionales ──
    @Test
    @DisplayName("Editar lanza excepción si producto no existe")
    void editar_noExiste_lanzaExcepcion() {
        when(productoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.editar(99L, request))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Editar lanza excepción si el nuevo código ya pertenece a otro producto")
    void editar_codigoDuplicadoEnOtroProducto_lanzaExcepcion() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepo.existsByCodigo("EMP-002")).thenReturn(true);

        assertThatThrownBy(() -> productoService.editar(1L, request))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Ya existe un producto con ese código");
    }

    @Test
    @DisplayName("Editar no verifica duplicado si el código no cambió")
    void editar_mismoCodigoNoVerificaDuplicado() {
        request.setCodigo("EMP-001"); // mismo código que productoMock
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(categoriaRepo.findById(1L)).thenReturn(
            Optional.of(Categoria.builder().id(1L).nombre("Motor").build()));
        when(productoRepo.save(any())).thenReturn(productoMock);

        ProductoResponse res = productoService.editar(1L, request);

        assertThat(res).isNotNull();
        verify(productoRepo, never()).existsByCodigo(any());
    }

    @Test
    @DisplayName("Editar lanza excepción si categoría no existe")
    void editar_categoriaNoExiste_lanzaExcepcion() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepo.existsByCodigo("EMP-002")).thenReturn(false);
        when(categoriaRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.editar(1L, request))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── ELIMINAR COMPATIBILIDAD ──
    @Test
    @DisplayName("Eliminar compatibilidad existente llama al repositorio")
    void eliminarCompatibilidad_exitoso() {
        when(compatRepo.existsByProductoIdAndMotorId(1L, 2L)).thenReturn(true);

        productoService.eliminarCompatibilidad(1L, 2L);

        verify(compatRepo).deleteByProductoIdAndMotorId(1L, 2L);
    }

    @Test
    @DisplayName("Eliminar compatibilidad lanza excepción si no existe")
    void eliminarCompatibilidad_noExiste_lanzaExcepcion() {
        when(compatRepo.existsByProductoIdAndMotorId(1L, 99L)).thenReturn(false);

        assertThatThrownBy(() -> productoService.eliminarCompatibilidad(1L, 99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── IMÁGENES ──
    @Test
    @DisplayName("Agregar imagen guarda la imagen correctamente")
    void agregarImagen_exitoso() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoMock));
        when(imagenRepo.findByProductoIdOrderByOrdenAsc(1L)).thenReturn(List.of());

        productoService.agregarImagen(1L, "https://img.pacifico.com/emp.jpg", true);

        verify(imagenRepo).save(any(ImagenProducto.class));
    }

    @Test
    @DisplayName("Agregar imagen lanza excepción si producto no existe")
    void agregarImagen_productoNoExiste_lanzaExcepcion() {
        when(productoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.agregarImagen(99L, "url", false))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Eliminar imagen por id llama a deleteById")
    void eliminarImagen_exitoso() {
        when(imagenRepo.existsById(5L)).thenReturn(true);

        productoService.eliminarImagen(5L);

        verify(imagenRepo).deleteById(5L);
    }

    @Test
    @DisplayName("Eliminar imagen lanza excepción si no existe")
    void eliminarImagen_noExiste_lanzaExcepcion() {
        when(imagenRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> productoService.eliminarImagen(99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}

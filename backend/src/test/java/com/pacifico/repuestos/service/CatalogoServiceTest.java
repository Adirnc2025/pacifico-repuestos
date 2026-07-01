package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.response.CatalogoResponse;
import com.pacifico.repuestos.exception.*;
import com.pacifico.repuestos.model.*;
import com.pacifico.repuestos.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CatalogoService — Pruebas unitarias")
class CatalogoServiceTest {

    @Mock private MarcaRepository      marcaRepo;
    @Mock private ModeloRepository     modeloRepo;
    @Mock private GeneracionRepository generacionRepo;
    @Mock private MotorRepository      motorRepo;

    @InjectMocks
    private CatalogoService catalogoService;

    private Marca   marcaMock;
    private Modelo  modeloMock;

    @BeforeEach
    void setUp() {
        marcaMock  = Marca.builder().id(1L).nombre("Toyota").activo(true).build();
        modeloMock = Modelo.builder().id(1L).nombre("Hilux").marca(marcaMock).activo(true).build();
    }

    // ── MARCAS ──
    @Test
    @DisplayName("Listar marcas activas devuelve lista correcta")
    void listarMarcas_devuelveSoloActivas() {
        when(marcaRepo.findByActivoTrue()).thenReturn(List.of(marcaMock));

        List<CatalogoResponse> res = catalogoService.listarMarcas();

        assertThat(res).hasSize(1);
        assertThat(res.get(0).getNombre()).isEqualTo("Toyota");
        verify(marcaRepo).findByActivoTrue();
    }

    @Test
    @DisplayName("Crear marca exitosamente")
    void crearMarca_exitoso() {
        when(marcaRepo.existsByNombre("Nissan")).thenReturn(false);
        when(marcaRepo.save(any())).thenReturn(
            Marca.builder().id(2L).nombre("Nissan").build());

        CatalogoResponse res = catalogoService.crearMarca("Nissan", null);

        assertThat(res.getNombre()).isEqualTo("Nissan");
        verify(marcaRepo).save(any(Marca.class));
    }

    @Test
    @DisplayName("Crear marca lanza excepción si nombre ya existe")
    void crearMarca_nombreDuplicado_lanzaExcepcion() {
        when(marcaRepo.existsByNombre("Toyota")).thenReturn(true);

        assertThatThrownBy(() -> catalogoService.crearMarca("Toyota", null))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Ya existe una marca");

        verify(marcaRepo, never()).save(any());
    }

    @Test
    @DisplayName("Editar marca actualiza nombre correctamente")
    void editarMarca_exitoso() {
        when(marcaRepo.findById(1L)).thenReturn(Optional.of(marcaMock));
        when(marcaRepo.save(any())).thenReturn(marcaMock);

        CatalogoResponse res = catalogoService.editarMarca(1L, "Toyota Motors", null);

        assertThat(marcaMock.getNombre()).isEqualTo("Toyota Motors");
        verify(marcaRepo).save(marcaMock);
    }

    @Test
    @DisplayName("Editar marca lanza excepción si no existe")
    void editarMarca_noExiste_lanzaExcepcion() {
        when(marcaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> catalogoService.editarMarca(99L, "X", null))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Eliminar marca falla si tiene modelos activos")
    void eliminarMarca_conModelos_lanzaExcepcion() {
        when(marcaRepo.findById(1L)).thenReturn(Optional.of(marcaMock));
        when(modeloRepo.findByMarcaIdAndActivoTrue(1L)).thenReturn(List.of(modeloMock));

        assertThatThrownBy(() -> catalogoService.eliminarMarca(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("modelos activos");

        verify(marcaRepo, never()).save(any());
    }

    @Test
    @DisplayName("Eliminar marca sin modelos la desactiva")
    void eliminarMarca_sinModelos_desactiva() {
        when(marcaRepo.findById(1L)).thenReturn(Optional.of(marcaMock));
        when(modeloRepo.findByMarcaIdAndActivoTrue(1L)).thenReturn(List.of());
        when(marcaRepo.save(any())).thenReturn(marcaMock);

        catalogoService.eliminarMarca(1L);

        assertThat(marcaMock.getActivo()).isFalse();
        verify(marcaRepo).save(marcaMock);
    }

    // ── MODELOS ──
    @Test
    @DisplayName("Listar modelos por marca devuelve resultados correctos")
    void listarModelosPorMarca_exitoso() {
        when(modeloRepo.findByMarcaIdAndActivoTrue(1L)).thenReturn(List.of(modeloMock));

        List<CatalogoResponse> res = catalogoService.listarModelosPorMarca(1L);

        assertThat(res).hasSize(1);
        assertThat(res.get(0).getNombre()).isEqualTo("Hilux");
    }

    @Test
    @DisplayName("Crear modelo exitosamente")
    void crearModelo_exitoso() {
        when(marcaRepo.findById(1L)).thenReturn(Optional.of(marcaMock));
        when(modeloRepo.existsByNombreAndMarcaId("Corolla", 1L)).thenReturn(false);
        when(modeloRepo.save(any())).thenReturn(modeloMock);

        CatalogoResponse res = catalogoService.crearModelo(1L, "Corolla");

        assertThat(res).isNotNull();
        verify(modeloRepo).save(any(Modelo.class));
    }

    @Test
    @DisplayName("Crear modelo lanza excepción si ya existe para esa marca")
    void crearModelo_duplicado_lanzaExcepcion() {
        when(marcaRepo.findById(1L)).thenReturn(Optional.of(marcaMock));
        when(modeloRepo.existsByNombreAndMarcaId("Hilux", 1L)).thenReturn(true);

        assertThatThrownBy(() -> catalogoService.crearModelo(1L, "Hilux"))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Ya existe ese modelo");

        verify(modeloRepo, never()).save(any());
    }

    @Test
    @DisplayName("Crear modelo lanza excepción si la marca no existe")
    void crearModelo_marcaNoExiste_lanzaExcepcion() {
        when(marcaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> catalogoService.crearModelo(99L, "Hilux"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── GENERACIONES ──
    @Test
    @DisplayName("Listar generaciones por modelo devuelve lista correcta")
    void listarGeneraciones_exitoso() {
        Generacion gen = Generacion.builder()
            .id(1L).nombre("N80").anioInicio(2015).anioFin(2023)
            .modelo(modeloMock).build();
        when(generacionRepo.findByModeloId(1L)).thenReturn(List.of(gen));

        List<CatalogoResponse> res = catalogoService.listarGeneracionesPorModelo(1L);

        assertThat(res).hasSize(1);
        assertThat(res.get(0).getNombre()).isEqualTo("N80");
        assertThat(res.get(0).getExtra()).contains("2015");
    }

    @Test
    @DisplayName("Crear generación exitosamente")
    void crearGeneracion_exitoso() {
        Generacion gen = Generacion.builder()
            .id(1L).nombre("N80").anioInicio(2015).anioFin(2023)
            .modelo(modeloMock).build();
        when(modeloRepo.findById(1L)).thenReturn(Optional.of(modeloMock));
        when(generacionRepo.save(any())).thenReturn(gen);

        CatalogoResponse res = catalogoService.crearGeneracion(1L, "N80", 2015, 2023);

        assertThat(res.getNombre()).isEqualTo("N80");
        verify(generacionRepo).save(any(Generacion.class));
    }

    @Test
    @DisplayName("Crear generación lanza excepción si modelo no existe")
    void crearGeneracion_modeloNoExiste_lanzaExcepcion() {
        when(modeloRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> catalogoService.crearGeneracion(99L, "N80", 2015, 2023))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── MOTORES ──
    @Test
    @DisplayName("Listar motores por generación devuelve lista correcta")
    void listarMotores_exitoso() {
        Generacion gen = Generacion.builder().id(1L).nombre("N80").modelo(modeloMock).build();
        Motor motor = Motor.builder().id(1L).codigo("2GD-FTV")
            .descripcion("Diesel 2.4L").generacion(gen).build();
        when(motorRepo.findByGeneracionId(1L)).thenReturn(List.of(motor));

        List<CatalogoResponse> res = catalogoService.listarMotoresPorGeneracion(1L);

        assertThat(res).hasSize(1);
        assertThat(res.get(0).getNombre()).isEqualTo("2GD-FTV");
    }

    @Test
    @DisplayName("Crear motor exitosamente")
    void crearMotor_exitoso() {
        Generacion gen = Generacion.builder().id(1L).nombre("N80").modelo(modeloMock).build();
        Motor motor = Motor.builder().id(1L).codigo("2GD-FTV").generacion(gen).build();
        when(generacionRepo.findById(1L)).thenReturn(Optional.of(gen));
        when(motorRepo.save(any())).thenReturn(motor);

        CatalogoResponse res = catalogoService.crearMotor(1L, "2GD-FTV", "Diesel 2.4L", "2.4L");

        assertThat(res.getNombre()).isEqualTo("2GD-FTV");
        verify(motorRepo).save(any(Motor.class));
    }

    @Test
    @DisplayName("Crear motor lanza excepción si generación no existe")
    void crearMotor_generacionNoExiste_lanzaExcepcion() {
        when(generacionRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> catalogoService.crearMotor(99L, "2GD-FTV", null, null))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}

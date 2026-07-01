package com.pacifico.repuestos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacifico.repuestos.dto.response.CatalogoResponse;
import com.pacifico.repuestos.security.JwtUtil;
import com.pacifico.repuestos.service.CatalogoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(value = CatalogoController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class CatalogoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  JwtUtil jwtUtil;
    @MockBean  CatalogoService catalogoService;

    private CatalogoResponse catalogoResp(Long id, String nombre) {
        return CatalogoResponse.builder().id(id).nombre(nombre).build();
    }

    @Test
    void listarMarcas_returns200() throws Exception {
        when(catalogoService.listarMarcas()).thenReturn(List.of(catalogoResp(1L, "Toyota")));

        mockMvc.perform(get("/api/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Toyota"));
    }

    @Test
    void crearMarca_returns201() throws Exception {
        when(catalogoService.crearMarca(any(), any())).thenReturn(catalogoResp(2L, "Honda"));

        mockMvc.perform(post("/api/marcas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("nombre", "Honda"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Honda"));
    }

    @Test
    void editarMarca_returns200() throws Exception {
        when(catalogoService.editarMarca(eq(1L), any(), any()))
                .thenReturn(catalogoResp(1L, "Toyota Actualizado"));

        mockMvc.perform(put("/api/marcas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("nombre", "Toyota Actualizado"))))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarMarca_returns204() throws Exception {
        doNothing().when(catalogoService).eliminarMarca(1L);

        mockMvc.perform(delete("/api/marcas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarModelos_returns200() throws Exception {
        when(catalogoService.listarModelosPorMarca(1L))
                .thenReturn(List.of(catalogoResp(1L, "Hilux")));

        mockMvc.perform(get("/api/modelos").param("marcaId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Hilux"));
    }

    @Test
    void crearModelo_returns201() throws Exception {
        when(catalogoService.crearModelo(anyLong(), anyString()))
                .thenReturn(catalogoResp(2L, "Fortuner"));

        mockMvc.perform(post("/api/modelos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("marcaId", 1, "nombre", "Fortuner"))))
                .andExpect(status().isCreated());
    }

    @Test
    void eliminarModelo_returns204() throws Exception {
        doNothing().when(catalogoService).eliminarModelo(1L);

        mockMvc.perform(delete("/api/modelos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarGeneraciones_returns200() throws Exception {
        when(catalogoService.listarGeneracionesPorModelo(1L))
                .thenReturn(List.of(catalogoResp(1L, "2015-2020")));

        mockMvc.perform(get("/api/generaciones").param("modeloId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void crearGeneracion_returns201() throws Exception {
        when(catalogoService.crearGeneracion(anyLong(), anyString(), any(), any()))
                .thenReturn(catalogoResp(1L, "2015-2020"));

        mockMvc.perform(post("/api/generaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("modeloId", 1, "nombre", "2015-2020",
                                        "anioInicio", 2015, "anioFin", 2020))))
                .andExpect(status().isCreated());
    }

    @Test
    void listarMotores_returns200() throws Exception {
        when(catalogoService.listarMotoresPorGeneracion(1L))
                .thenReturn(List.of(catalogoResp(1L, "2GD-FTV")));

        mockMvc.perform(get("/api/motores").param("generacionId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void crearMotor_returns201() throws Exception {
        when(catalogoService.crearMotor(anyLong(), anyString(), any(), any()))
                .thenReturn(catalogoResp(1L, "2GD-FTV"));

        mockMvc.perform(post("/api/motores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("generacionId", 1, "codigo", "2GD-FTV"))))
                .andExpect(status().isCreated());
    }
}

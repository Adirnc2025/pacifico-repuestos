package com.pacifico.repuestos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacifico.repuestos.dto.request.ProductoRequest;
import com.pacifico.repuestos.dto.response.ProductoResponse;
import com.pacifico.repuestos.exception.ResourceNotFoundException;
import com.pacifico.repuestos.security.JwtUtil;
import com.pacifico.repuestos.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(value = ProductoController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class ProductoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  JwtUtil jwtUtil;
    @MockBean  ProductoService productoService;

    private ProductoResponse mockProducto() {
        return ProductoResponse.builder()
                .id(1L).codigo("EMP-001").nombre("Empaque Motor")
                .precio(new BigDecimal("280.00")).stock(10).build();
    }

    @Test
    void listar_returns200() throws Exception {
        when(productoService.filtrar(any(), any(), any(), any(), any()))
                .thenReturn(List.of(mockProducto()));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("EMP-001"));
    }

    @Test
    void destacados_returns200() throws Exception {
        when(productoService.listarDestacados()).thenReturn(List.of(mockProducto()));

        mockMvc.perform(get("/api/productos/destacados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void buscar_returns200() throws Exception {
        when(productoService.buscar("emp")).thenReturn(List.of(mockProducto()));

        mockMvc.perform(get("/api/productos/buscar").param("q", "emp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Empaque Motor"));
    }

    @Test
    void detalle_existente_returns200() throws Exception {
        when(productoService.obtenerPorId(1L)).thenReturn(mockProducto());

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void detalle_noExiste_returns404() throws Exception {
        when(productoService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Producto", 99L));

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_returns201() throws Exception {
        ProductoRequest req = new ProductoRequest();
        req.setCodigo("EMP-002");
        req.setNombre("Empaque Culata");
        req.setPrecio(new BigDecimal("95.00"));

        when(productoService.crear(any())).thenReturn(mockProducto());

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void editar_returns200() throws Exception {
        ProductoRequest req = new ProductoRequest();
        req.setCodigo("EMP-001");
        req.setNombre("Empaque Motor Updated");
        req.setPrecio(new BigDecimal("300.00"));

        when(productoService.editar(eq(1L), any())).thenReturn(mockProducto());

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void eliminar_returns204() throws Exception {
        doNothing().when(productoService).eliminar(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void agregarCompatibilidad_returns201() throws Exception {
        doNothing().when(productoService).agregarCompatibilidad(eq(1L), any());

        mockMvc.perform(post("/api/productos/1/compatibilidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motorId\":2}"))
                .andExpect(status().isCreated());
    }

    @Test
    void eliminarCompatibilidad_returns204() throws Exception {
        doNothing().when(productoService).eliminarCompatibilidad(1L, 2L);

        mockMvc.perform(delete("/api/productos/1/compatibilidades/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void agregarImagen_returns201() throws Exception {
        doNothing().when(productoService).agregarImagen(eq(1L), anyString(), anyBoolean());

        mockMvc.perform(post("/api/productos/1/imagenes")
                        .param("url", "https://img.test.com/foto.jpg")
                        .param("esPrincipal", "true"))
                .andExpect(status().isCreated());
    }

    @Test
    void eliminarImagen_returns204() throws Exception {
        doNothing().when(productoService).eliminarImagen(5L);

        mockMvc.perform(delete("/api/productos/imagenes/5"))
                .andExpect(status().isNoContent());
    }
}

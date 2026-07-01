package com.pacifico.repuestos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacifico.repuestos.model.Categoria;
import com.pacifico.repuestos.repository.CategoriaRepository;
import com.pacifico.repuestos.security.JwtUtil;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(value = CategoriaController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class CategoriaControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  JwtUtil jwtUtil;
    @MockBean  CategoriaRepository categoriaRepo;

    private Categoria mockCategoria() {
        return Categoria.builder().id(1L).nombre("Motor").descripcion("Piezas de motor").activo(true).build();
    }

    @Test
    void listar_returns200() throws Exception {
        when(categoriaRepo.findByActivoTrue()).thenReturn(List.of(mockCategoria()));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Motor"));
    }

    @Test
    void crear_returns201() throws Exception {
        when(categoriaRepo.save(any())).thenReturn(mockCategoria());

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("nombre", "Motor", "descripcion", "Piezas de motor"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Motor"));
    }

    @Test
    void editar_existente_returns200() throws Exception {
        when(categoriaRepo.findById(1L)).thenReturn(Optional.of(mockCategoria()));
        when(categoriaRepo.save(any())).thenReturn(mockCategoria());

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("nombre", "Motor Updated"))))
                .andExpect(status().isOk());
    }

    @Test
    void editar_noExiste_returns404() throws Exception {
        when(categoriaRepo.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/categorias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("nombre", "X"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_existente_returns204() throws Exception {
        when(categoriaRepo.findById(1L)).thenReturn(Optional.of(mockCategoria()));
        when(categoriaRepo.save(any())).thenReturn(mockCategoria());

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_noExiste_returns404() throws Exception {
        when(categoriaRepo.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }
}

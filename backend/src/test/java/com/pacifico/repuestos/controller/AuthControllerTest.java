package com.pacifico.repuestos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacifico.repuestos.dto.request.LoginRequest;
import com.pacifico.repuestos.dto.request.RegisterRequest;
import com.pacifico.repuestos.dto.response.AuthResponse;
import com.pacifico.repuestos.exception.BusinessException;
import com.pacifico.repuestos.security.JwtUtil;
import com.pacifico.repuestos.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(value = AuthController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  JwtUtil jwtUtil;
    @MockBean  AuthService authService;

    @Test
    void health_returns200() throws Exception {
        mockMvc.perform(get("/api/auth/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Pacífico Repuestos API - OK"));
    }

    @Test
    void login_exitoso_returns200ConToken() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setCorreo("admin@pacifico.com");
        req.setPassword("password123");

        AuthResponse resp = AuthResponse.builder()
                .token("jwt-token").tipo("Bearer")
                .usuarioId(1L).nombre("Admin").correo("admin@pacifico.com").rol("ADMIN")
                .build();
        when(authService.login(any())).thenReturn(resp);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    void register_exitoso_returns201() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Juan Perez");
        req.setCorreo("juan@pacifico.com");
        req.setPassword("password123");

        AuthResponse resp = AuthResponse.builder()
                .token("jwt-token").tipo("Bearer")
                .usuarioId(2L).nombre("Juan Perez").correo("juan@pacifico.com").rol("CLIENTE")
                .build();
        when(authService.register(any())).thenReturn(resp);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    @Test
    void login_credencialesInvalidas_returns400() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setCorreo("noexiste@pacifico.com");
        req.setPassword("wrongpass");

        when(authService.login(any())).thenThrow(new BusinessException("Credenciales inválidas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}

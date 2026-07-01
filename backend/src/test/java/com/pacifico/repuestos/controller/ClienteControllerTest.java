package com.pacifico.repuestos.controller;

import com.pacifico.repuestos.model.Cliente;
import com.pacifico.repuestos.model.Usuario;
import com.pacifico.repuestos.repository.ClienteRepository;
import com.pacifico.repuestos.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(value = ClienteController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class ClienteControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean  JwtUtil jwtUtil;
    @MockBean  ClienteRepository clienteRepo;

    @Test
    void listar_returns200() throws Exception {
        when(clienteRepo.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void listar_conClientes_devuelveListaNoVacia() throws Exception {
        Usuario usuario = Usuario.builder()
            .id(1L).nombre("Juan Pérez").correo("juan@test.com")
            .rol(Usuario.Rol.CLIENTE).activo(true).build();
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        when(clienteRepo.findAll()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}

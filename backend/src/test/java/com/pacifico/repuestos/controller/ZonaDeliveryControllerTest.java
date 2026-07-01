package com.pacifico.repuestos.controller;

import com.pacifico.repuestos.model.ZonaDelivery;
import com.pacifico.repuestos.repository.ZonaDeliveryRepository;
import com.pacifico.repuestos.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ZonaDeliveryController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class ZonaDeliveryControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean  JwtUtil jwtUtil;
    @MockBean  ZonaDeliveryRepository zonaRepo;

    private ZonaDelivery mockZona(String tipo) {
        return ZonaDelivery.builder()
                .id(1L).nombre("Lima Centro").tipo(tipo)
                .tarifa(new BigDecimal("10.00")).activo(true).build();
    }

    @Test
    void zonas_returns200() throws Exception {
        when(zonaRepo.findByActivoTrue()).thenReturn(List.of(mockZona("LOCAL")));

        mockMvc.perform(get("/api/delivery/zonas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Lima Centro"));
    }

    @Test
    void zonasPorTipo_returns200() throws Exception {
        when(zonaRepo.findByTipoAndActivoTrue("LOCAL")).thenReturn(List.of(mockZona("LOCAL")));

        mockMvc.perform(get("/api/delivery/zonas/local"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("LOCAL"));
    }

    @Test
    void zonasPorTipo_sinResultados_returns200ListaVacia() throws Exception {
        when(zonaRepo.findByTipoAndActivoTrue("INTERPROVINCIAL")).thenReturn(List.of());

        mockMvc.perform(get("/api/delivery/zonas/interprovincial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}

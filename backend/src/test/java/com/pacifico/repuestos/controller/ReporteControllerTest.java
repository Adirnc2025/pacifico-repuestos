package com.pacifico.repuestos.controller;

import com.pacifico.repuestos.dto.response.DashboardResponse;
import com.pacifico.repuestos.security.JwtUtil;
import com.pacifico.repuestos.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(value = ReporteController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class ReporteControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean  JwtUtil jwtUtil;
    @MockBean  ReporteService reporteService;

    @Test
    void dashboard_returns200() throws Exception {
        DashboardResponse resp = DashboardResponse.builder()
                .totalProductos(50L).pedidosHoy(3L).pedidosPendientes(5L)
                .pedidosConfirmados(10L).stockBajoCount(2L).stockBajo(List.of())
                .build();
        when(reporteService.dashboard()).thenReturn(resp);

        mockMvc.perform(get("/api/reportes/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProductos").value(50))
                .andExpect(jsonPath("$.pedidosHoy").value(3));
    }

    @Test
    void ventas_returns200() throws Exception {
        ReporteService.VentasReporte ventasResp = ReporteService.VentasReporte.builder()
                .totalPedidos(5).totalIngresos(new BigDecimal("1400.00")).filas(List.of()).build();
        when(reporteService.ventasPorPeriodo(any(), any())).thenReturn(ventasResp);

        mockMvc.perform(get("/api/reportes/ventas")
                        .param("desde", "2025-01-01")
                        .param("hasta", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPedidos").value(5));
    }
}

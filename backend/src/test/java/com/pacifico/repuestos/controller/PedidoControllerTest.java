package com.pacifico.repuestos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacifico.repuestos.dto.request.PedidoRequest;
import com.pacifico.repuestos.dto.response.PedidoResponse;
import com.pacifico.repuestos.security.JwtUtil;
import com.pacifico.repuestos.service.PedidoService;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(value = PedidoController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class PedidoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  JwtUtil jwtUtil;
    @MockBean  PedidoService pedidoService;

    private PedidoResponse mockPedido() {
        return PedidoResponse.builder()
                .id(1L).numeroPedido("PED-001").estado("PENDIENTE")
                .total(new BigDecimal("280.00")).build();
    }

    @Test
    void crear_returns201() throws Exception {
        PedidoRequest.ItemPedidoRequest item = new PedidoRequest.ItemPedidoRequest();
        item.setProductoId(1L);
        item.setCantidad(2);

        PedidoRequest req = new PedidoRequest();
        req.setItems(List.of(item));
        req.setTipoDelivery("RECOJO");

        when(pedidoService.crear(any(), any())).thenReturn(mockPedido());

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroPedido").value("PED-001"));
    }

    @Test
    void misPedidos_returns200() throws Exception {
        when(pedidoService.misPedidos(any())).thenReturn(List.of(mockPedido()));

        mockMvc.perform(get("/api/pedidos/mis-pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void detalle_returns200() throws Exception {
        when(pedidoService.obtener(eq(1L), any(), anyBoolean())).thenReturn(mockPedido());

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void listarTodos_returns200() throws Exception {
        when(pedidoService.listarTodos(any())).thenReturn(List.of(mockPedido()));

        mockMvc.perform(get("/api/pedidos").param("estado", "PENDIENTE"))
                .andExpect(status().isOk());
    }

    @Test
    void cambiarEstado_returns200() throws Exception {
        PedidoResponse updated = PedidoResponse.builder()
                .id(1L).numeroPedido("PED-001").estado("CONFIRMADO")
                .total(new BigDecimal("280.00")).build();
        when(pedidoService.cambiarEstado(eq(1L), eq("CONFIRMADO"))).thenReturn(updated);

        mockMvc.perform(put("/api/pedidos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("estado", "CONFIRMADO"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CONFIRMADO"));
    }
}

package com.pacifico.repuestos.dto.response;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardResponse {
    private Long totalProductos;
    private Long pedidosHoy;
    private Long pedidosPendientes;
    private Long pedidosConfirmados;
    private Long stockBajoCount;
    private List<StockBajoItem> stockBajo;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class StockBajoItem {
        private Long   productoId;
        private String nombre;
        private String codigo;
        private Integer stock;
    }
}

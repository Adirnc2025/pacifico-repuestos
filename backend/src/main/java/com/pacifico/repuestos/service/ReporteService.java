package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.response.DashboardResponse;
import com.pacifico.repuestos.model.*;
import com.pacifico.repuestos.repository.*;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final PedidoRepository     pedidoRepo;
    private final ProductoRepository   productoRepo;
    private final InventarioRepository inventarioRepo;

    // ── Dashboard ──
    @Transactional(readOnly = true)
    public DashboardResponse dashboard() {
        List<DashboardResponse.StockBajoItem> stockBajo =
            inventarioRepo.findByStockLessThanAndStockGreaterThan(5, 0)
                .stream().map(i -> DashboardResponse.StockBajoItem.builder()
                    .productoId(i.getProducto().getId())
                    .nombre(i.getProducto().getNombre())
                    .codigo(i.getProducto().getCodigo())
                    .stock(i.getStock())
                    .build())
                .toList();

        return DashboardResponse.builder()
            .totalProductos(productoRepo.count())
            .pedidosHoy(pedidoRepo.contarHoy())
            .pedidosPendientes(pedidoRepo.contarPorEstado("PENDIENTE"))
            .pedidosConfirmados(pedidoRepo.contarPorEstado("CONFIRMADO"))
            .stockBajoCount((long) stockBajo.size())
            .stockBajo(stockBajo)
            .build();
    }

    // ── Reporte de ventas por período ──
    @Transactional(readOnly = true)
    public VentasReporte ventasPorPeriodo(LocalDate desde, LocalDate hasta) {
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin    = hasta.plusDays(1).atStartOfDay();

        List<Pedido> pedidos = pedidoRepo.findAllByOrderByFechaPedidoDesc()
            .stream()
            .filter(p -> !p.getEstado().equals("CANCELADO"))
            .filter(p -> !p.getFechaPedido().isBefore(inicio)
                      && p.getFechaPedido().isBefore(fin))
            .toList();

        BigDecimal totalIngresos = pedidos.stream()
            .map(Pedido::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<VentasReporte.FilaVenta> filas = pedidos.stream()
            .map(p -> VentasReporte.FilaVenta.builder()
                .pedidoId(p.getId())
                .numeroPedido(p.getNumeroPedido())
                .cliente(p.getCliente().getUsuario().getNombre())
                .estado(p.getEstado())
                .total(p.getTotal())
                .fecha(p.getFechaPedido())
                .build())
            .toList();

        return VentasReporte.builder()
            .desde(desde)
            .hasta(hasta)
            .totalPedidos(pedidos.size())
            .totalIngresos(totalIngresos)
            .filas(filas)
            .build();
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class VentasReporte {
        private LocalDate       desde;
        private LocalDate       hasta;
        private int             totalPedidos;
        private BigDecimal      totalIngresos;
        private List<FilaVenta> filas;

        @Data @Builder @NoArgsConstructor @AllArgsConstructor
        public static class FilaVenta {
            private Long          pedidoId;
            private String        numeroPedido;
            private String        cliente;
            private String        estado;
            private BigDecimal    total;
            private LocalDateTime fecha;
        }
    }
}

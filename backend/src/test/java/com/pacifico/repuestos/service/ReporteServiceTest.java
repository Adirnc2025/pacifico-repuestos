package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.response.DashboardResponse;
import com.pacifico.repuestos.model.*;
import com.pacifico.repuestos.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReporteService — Pruebas unitarias")
class ReporteServiceTest {

    @Mock private PedidoRepository     pedidoRepo;
    @Mock private ProductoRepository   productoRepo;
    @Mock private InventarioRepository inventarioRepo;

    @InjectMocks
    private ReporteService reporteService;

    private Usuario  usuarioMock;
    private Cliente  clienteMock;
    private Producto productoMock;

    @BeforeEach
    void setUp() {
        usuarioMock  = Usuario.builder().id(1L).nombre("Juan").correo("juan@test.com")
            .rol(Usuario.Rol.CLIENTE).activo(true).build();
        clienteMock  = Cliente.builder().id(1L).usuario(usuarioMock).build();
        productoMock = Producto.builder().id(1L).codigo("EMP-001")
            .nombre("Empaque Motor").precio(new BigDecimal("280.00"))
            .activo(true).imagenes(List.of()).build();
    }

    // ── DASHBOARD ──
    @Test
    @DisplayName("Dashboard devuelve métricas correctas")
    void dashboard_devuelveMetricas() {
        when(productoRepo.count()).thenReturn(15L);
        when(pedidoRepo.contarHoy()).thenReturn(3L);
        when(pedidoRepo.contarPorEstado("PENDIENTE")).thenReturn(5L);
        when(pedidoRepo.contarPorEstado("CONFIRMADO")).thenReturn(2L);
        when(inventarioRepo.findByStockLessThanAndStockGreaterThan(5, 0))
            .thenReturn(List.of());

        DashboardResponse res = reporteService.dashboard();

        assertThat(res.getTotalProductos()).isEqualTo(15L);
        assertThat(res.getPedidosHoy()).isEqualTo(3L);
        assertThat(res.getPedidosPendientes()).isEqualTo(5L);
        assertThat(res.getPedidosConfirmados()).isEqualTo(2L);
        assertThat(res.getStockBajoCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Dashboard detecta productos con stock bajo")
    void dashboard_detectaStockBajo() {
        Inventario invBajo = Inventario.builder()
            .id(1L).producto(productoMock).stock(2).stockMinimo(5).build();

        when(productoRepo.count()).thenReturn(1L);
        when(pedidoRepo.contarHoy()).thenReturn(0L);
        when(pedidoRepo.contarPorEstado(any())).thenReturn(0L);
        when(inventarioRepo.findByStockLessThanAndStockGreaterThan(5, 0))
            .thenReturn(List.of(invBajo));

        DashboardResponse res = reporteService.dashboard();

        assertThat(res.getStockBajoCount()).isEqualTo(1L);
        assertThat(res.getStockBajo()).hasSize(1);
        assertThat(res.getStockBajo().get(0).getCodigo()).isEqualTo("EMP-001");
        assertThat(res.getStockBajo().get(0).getStock()).isEqualTo(2);
    }

    @Test
    @DisplayName("Dashboard con cero pedidos devuelve valores en cero")
    void dashboard_sinPedidos_devuelveCero() {
        when(productoRepo.count()).thenReturn(0L);
        when(pedidoRepo.contarHoy()).thenReturn(0L);
        when(pedidoRepo.contarPorEstado(any())).thenReturn(0L);
        when(inventarioRepo.findByStockLessThanAndStockGreaterThan(5, 0))
            .thenReturn(List.of());

        DashboardResponse res = reporteService.dashboard();

        assertThat(res.getTotalProductos()).isZero();
        assertThat(res.getPedidosHoy()).isZero();
        assertThat(res.getStockBajo()).isEmpty();
    }

    // ── VENTAS POR PERÍODO ──
    @Test
    @DisplayName("Ventas por período filtra pedidos cancelados")
    void ventasPorPeriodo_excluyeCancelados() {
        Pedido pedidoActivo = Pedido.builder()
            .id(1L).numeroPedido("PED-001")
            .cliente(clienteMock).estado("ENTREGADO")
            .total(new BigDecimal("280.00"))
            .fechaPedido(LocalDateTime.now()).detalles(List.of()).build();

        Pedido pedidoCancelado = Pedido.builder()
            .id(2L).numeroPedido("PED-002")
            .cliente(clienteMock).estado("CANCELADO")
            .total(new BigDecimal("100.00"))
            .fechaPedido(LocalDateTime.now()).detalles(List.of()).build();

        when(pedidoRepo.findAllByOrderByFechaPedidoDesc())
            .thenReturn(List.of(pedidoActivo, pedidoCancelado));

        LocalDate hoy = LocalDate.now();
        ReporteService.VentasReporte res =
            reporteService.ventasPorPeriodo(hoy.minusDays(1), hoy);

        assertThat(res.getTotalPedidos()).isEqualTo(1);
        assertThat(res.getTotalIngresos()).isEqualByComparingTo(new BigDecimal("280.00"));
        assertThat(res.getFilas()).hasSize(1);
        assertThat(res.getFilas().get(0).getNumeroPedido()).isEqualTo("PED-001");
    }

    @Test
    @DisplayName("Ventas por período calcula total de ingresos correctamente")
    void ventasPorPeriodo_calculaTotalIngresos() {
        Pedido p1 = Pedido.builder().id(1L).numeroPedido("PED-001")
            .cliente(clienteMock).estado("ENTREGADO")
            .total(new BigDecimal("280.00"))
            .fechaPedido(LocalDateTime.now()).detalles(List.of()).build();
        Pedido p2 = Pedido.builder().id(2L).numeroPedido("PED-002")
            .cliente(clienteMock).estado("CONFIRMADO")
            .total(new BigDecimal("150.00"))
            .fechaPedido(LocalDateTime.now()).detalles(List.of()).build();

        when(pedidoRepo.findAllByOrderByFechaPedidoDesc()).thenReturn(List.of(p1, p2));

        LocalDate hoy = LocalDate.now();
        ReporteService.VentasReporte res =
            reporteService.ventasPorPeriodo(hoy.minusDays(1), hoy);

        assertThat(res.getTotalPedidos()).isEqualTo(2);
        assertThat(res.getTotalIngresos()).isEqualByComparingTo(new BigDecimal("430.00"));
    }

    @Test
    @DisplayName("Ventas fuera del rango de fechas no se incluyen")
    void ventasPorPeriodo_fueraDeRango_noSeIncluyen() {
        Pedido pedidoViejo = Pedido.builder().id(1L).numeroPedido("PED-OLD")
            .cliente(clienteMock).estado("ENTREGADO")
            .total(new BigDecimal("100.00"))
            .fechaPedido(LocalDateTime.now().minusDays(30))
            .detalles(List.of()).build();

        when(pedidoRepo.findAllByOrderByFechaPedidoDesc()).thenReturn(List.of(pedidoViejo));

        LocalDate hoy = LocalDate.now();
        ReporteService.VentasReporte res =
            reporteService.ventasPorPeriodo(hoy.minusDays(7), hoy);

        assertThat(res.getTotalPedidos()).isZero();
        assertThat(res.getTotalIngresos()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(res.getFilas()).isEmpty();
    }
}

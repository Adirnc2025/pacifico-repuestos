package com.pacifico.repuestos.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "pedidos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_pedido", unique = true, nullable = false, length = 20)
    private String numeroPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "costo_delivery", precision = 8, scale = 2)
    private BigDecimal costoDelivery = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "tipo_delivery", nullable = false, length = 20)
    private String tipoDelivery; // RECOJO | LOCAL | INTERPROVINCIAL

    @Column(name = "direccion_entrega", length = 300)
    private String direccionEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id")
    private ZonaDelivery zona;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaPedido = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (estado == null) estado = EstadoPedido.PENDIENTE.name();
    }

    @PreUpdate
    protected void onUpdate() { fechaActualizacion = LocalDateTime.now(); }

    public enum EstadoPedido {
        PENDIENTE, CONFIRMADO, EN_PREPARACION, ENVIADO, ENTREGADO, CANCELADO
    }
}

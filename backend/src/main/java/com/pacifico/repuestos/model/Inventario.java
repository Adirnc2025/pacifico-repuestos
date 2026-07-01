package com.pacifico.repuestos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "inventario")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Inventario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", unique = true, nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "stock_minimo")
    private Integer stockMinimo = 5;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @PrePersist @PreUpdate
    protected void onUpdate() { ultimaActualizacion = LocalDateTime.now(); }
}

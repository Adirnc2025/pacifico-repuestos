package com.pacifico.repuestos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "compatibilidades",
    uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id","motor_id"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Compatibilidad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motor_id", nullable = false)
    private Motor motor;

    @Column(length = 300)
    private String observacion;
}

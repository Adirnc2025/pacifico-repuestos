package com.pacifico.repuestos.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name = "zonas_delivery")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ZonaDelivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String tipo; // LOCAL | INTERPROVINCIAL

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal tarifa;

    @Column(nullable = false)
    private Boolean activo = true;
}

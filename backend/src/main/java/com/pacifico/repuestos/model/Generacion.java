package com.pacifico.repuestos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "generaciones")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Generacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "anio_inicio")
    private Integer anioInicio;

    @Column(name = "anio_fin")
    private Integer anioFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;
}

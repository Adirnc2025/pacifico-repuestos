package com.pacifico.repuestos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "modelos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Modelo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    @Column(nullable = false)
    private Boolean activo = true;
}

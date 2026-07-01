package com.pacifico.repuestos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "motores")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Motor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(length = 200)
    private String descripcion;

    @Column(length = 20)
    private String cilindrada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generacion_id", nullable = false)
    private Generacion generacion;
}

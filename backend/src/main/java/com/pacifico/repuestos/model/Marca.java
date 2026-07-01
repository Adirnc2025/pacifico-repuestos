package com.pacifico.repuestos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "marcas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Marca {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(nullable = false)
    private Boolean activo = true;
}

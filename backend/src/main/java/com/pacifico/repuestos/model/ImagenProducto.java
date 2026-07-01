package com.pacifico.repuestos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "imagenes_producto")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ImagenProducto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @Column
    private Integer orden = 0;
}

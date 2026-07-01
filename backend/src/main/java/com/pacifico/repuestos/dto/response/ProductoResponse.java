package com.pacifico.repuestos.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductoResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String medidas;
    private Boolean destacado;
    private String categoria;
    private Long categoriaId;
    private Integer stock;
    private List<String> imagenes;
    private String imagenPrincipal;
    private List<CompatibilidadResponse> compatibilidades;
}

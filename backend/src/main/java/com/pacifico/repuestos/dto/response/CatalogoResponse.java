package com.pacifico.repuestos.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CatalogoResponse {
    private Long id;
    private String nombre;
    private String extra;   // logo, cilindrada, años, etc.
}

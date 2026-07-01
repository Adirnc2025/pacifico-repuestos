package com.pacifico.repuestos.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CompatibilidadResponse {
    private Long compatibilidadId;
    private Long motorId;
    private String motorCodigo;
    private String motorDescripcion;
    private String generacion;
    private String modelo;
    private String marca;
    private String observacion;
}

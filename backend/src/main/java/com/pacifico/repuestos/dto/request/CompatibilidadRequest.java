package com.pacifico.repuestos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompatibilidadRequest {
    @NotNull(message = "El motorId es obligatorio")
    private Long motorId;
    private String observacion;
}

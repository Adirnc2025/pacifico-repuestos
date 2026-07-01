package com.pacifico.repuestos.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "El correo es obligatorio")
    @Email
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}

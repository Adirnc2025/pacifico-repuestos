package com.pacifico.repuestos.dto.response;

import lombok.*;

@Data @AllArgsConstructor @Builder
public class AuthResponse {
    private String token;
    private String tipo;
    private Long usuarioId;
    private String nombre;
    private String correo;
    private String rol;
}

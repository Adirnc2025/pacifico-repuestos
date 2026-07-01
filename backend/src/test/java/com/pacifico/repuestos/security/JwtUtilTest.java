package com.pacifico.repuestos.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    // 32+ caracteres para cumplir HS256 (256 bits mínimo)
    private static final String SECRET = "clave-secreta-de-prueba-para-jwt-tests-pacifico";
    private static final long EXPIRATION = 3_600_000L; // 1 hora

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRATION);
    }

    @Test
    void generateToken_devuelveTokenNoVacio() {
        String token = jwtUtil.generateToken("admin@pacifico.com", "ADMIN", 1L);
        assertThat(token).isNotBlank();
    }

    @Test
    void extractCorreo_devuelveSubjectCorrecto() {
        String token = jwtUtil.generateToken("vendedor@pacifico.com", "VENDEDOR", 5L);
        assertThat(jwtUtil.extractCorreo(token)).isEqualTo("vendedor@pacifico.com");
    }

    @Test
    void extractRol_devuelveRolCorrecto() {
        String token = jwtUtil.generateToken("admin@pacifico.com", "ADMIN", 1L);
        assertThat(jwtUtil.extractRol(token)).isEqualTo("ADMIN");
    }

    @Test
    void isTokenValid_tokenValido_retornaTrue() {
        String token = jwtUtil.generateToken("usuario@test.com", "VENDEDOR", 2L);
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_tokenMalformado_retornaFalse() {
        assertThat(jwtUtil.isTokenValid("esto.no.es.un.jwt")).isFalse();
    }

    @Test
    void isTokenValid_tokenVencido_retornaFalse() {
        ReflectionTestUtils.setField(jwtUtil, "expiration", -1000L); // ya expirado
        String tokenVencido = jwtUtil.generateToken("expirado@test.com", "ADMIN", 3L);
        assertThat(jwtUtil.isTokenValid(tokenVencido)).isFalse();
    }
}

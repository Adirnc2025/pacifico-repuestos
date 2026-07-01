package com.pacifico.repuestos.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFound_returns404WithMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Producto no encontrado");
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("status", 404);
        assertThat(response.getBody()).containsEntry("error", "Producto no encontrado");
        assertThat(response.getBody()).containsKey("timestamp");
    }

    @Test
    void handleBusiness_returns400WithMessage() {
        BusinessException ex = new BusinessException("Código duplicado");
        ResponseEntity<Map<String, Object>> response = handler.handleBusiness(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("status", 400);
        assertThat(response.getBody()).containsEntry("error", "Código duplicado");
        assertThat(response.getBody()).containsKey("timestamp");
    }

    @Test
    void handleValidation_returns400WithFieldErrors() {
        FieldError fieldError = new FieldError("productoRequest", "nombre", "El nombre es obligatorio");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);

        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body).containsEntry("status", 400);
        assertThat(body).containsEntry("error", "Validación fallida");
        assertThat(body).containsKey("campos");

        @SuppressWarnings("unchecked")
        Map<String, String> campos = (Map<String, String>) body.get("campos");
        assertThat(campos).containsEntry("nombre", "El nombre es obligatorio");
    }

    @Test
    void handleGeneral_returns500() {
        Exception ex = new RuntimeException("fallo inesperado");
        ResponseEntity<Map<String, Object>> response = handler.handleGeneral(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("status", 500);
        assertThat(response.getBody()).containsEntry("error", "Error interno del servidor");
        assertThat(response.getBody()).containsKey("timestamp");
    }
}

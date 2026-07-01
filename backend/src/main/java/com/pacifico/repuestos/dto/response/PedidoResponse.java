package com.pacifico.repuestos.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PedidoResponse {
    private Long          id;
    private String        numeroPedido;
    private String        estado;
    private BigDecimal    subtotal;
    private BigDecimal    costoDelivery;
    private BigDecimal    total;
    private String        tipoDelivery;
    private String        direccionEntrega;
    private String        zona;
    private String        observacion;
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaActualizacion;
    private ClienteInfo   cliente;
    private List<DetalleResponse> detalles;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ClienteInfo {
        private Long   id;
        private String nombre;
        private String correo;
        private String telefono;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DetalleResponse {
        private Long       productoId;
        private String     productoNombre;
        private String     productoCodigo;
        private String     imagenUrl;
        private Integer    cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}

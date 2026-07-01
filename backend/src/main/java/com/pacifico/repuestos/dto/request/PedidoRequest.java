package com.pacifico.repuestos.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest {

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    private List<ItemPedidoRequest> items;

    @NotBlank(message = "El tipo de delivery es obligatorio")
    private String tipoDelivery; // RECOJO | LOCAL | INTERPROVINCIAL

    private String direccionEntrega;
    private Long   zonaId;
    private String observacion;

    @Data
    public static class ItemPedidoRequest {
        @NotNull(message = "El productoId es obligatorio")
        private Long productoId;

        @NotNull @Min(value = 1, message = "La cantidad mínima es 1")
        private Integer cantidad;
    }
}

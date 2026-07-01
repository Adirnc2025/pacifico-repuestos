package com.pacifico.repuestos.controller;

import com.pacifico.repuestos.dto.request.PedidoRequest;
import com.pacifico.repuestos.dto.response.PedidoResponse;
import com.pacifico.repuestos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(
            @AuthenticationPrincipal String correo,
            @Valid @RequestBody PedidoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(pedidoService.crear(correo, req));
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponse>> misPedidos(
            @AuthenticationPrincipal String correo) {
        return ResponseEntity.ok(pedidoService.misPedidos(correo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> detalle(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtener(id));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos(
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(pedidoService.listarTodos(estado));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
            pedidoService.cambiarEstado(id, body.get("estado")));
    }
}

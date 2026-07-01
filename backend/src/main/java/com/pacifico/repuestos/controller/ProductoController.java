package com.pacifico.repuestos.controller;

import com.pacifico.repuestos.dto.request.*;
import com.pacifico.repuestos.dto.response.ProductoResponse;
import com.pacifico.repuestos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar(
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Long modeloId,
            @RequestParam(required = false) Long generacionId,
            @RequestParam(required = false) Long motorId,
            @RequestParam(required = false) Long categoriaId) {
        return ResponseEntity.ok(
            productoService.filtrar(marcaId, modeloId, generacionId, motorId, categoriaId));
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoResponse>> destacados() {
        return ResponseEntity.ok(productoService.listarDestacados());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponse>> buscar(@RequestParam String q) {
        return ResponseEntity.ok(productoService.buscar(q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> detalle(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> editar(@PathVariable Long id,
                                                    @Valid @RequestBody ProductoRequest req) {
        return ResponseEntity.ok(productoService.editar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ── COMPATIBILIDADES ──
    @PostMapping("/{id}/compatibilidades")
    public ResponseEntity<Void> agregarCompat(@PathVariable Long id,
                                              @Valid @RequestBody CompatibilidadRequest req) {
        productoService.agregarCompatibilidad(id, req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/compatibilidades/{motorId}")
    public ResponseEntity<Void> eliminarCompat(@PathVariable Long id,
                                               @PathVariable Long motorId) {
        productoService.eliminarCompatibilidad(id, motorId);
        return ResponseEntity.noContent().build();
    }

    // ── IMÁGENES ──
    @PostMapping("/{id}/imagenes")
    public ResponseEntity<Void> agregarImagen(@PathVariable Long id,
                                              @RequestParam String url,
                                              @RequestParam(defaultValue = "false") boolean esPrincipal) {
        productoService.agregarImagen(id, url, esPrincipal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/imagenes/{imagenId}")
    public ResponseEntity<Void> eliminarImagen(@PathVariable Long imagenId) {
        productoService.eliminarImagen(imagenId);
        return ResponseEntity.noContent().build();
    }
}

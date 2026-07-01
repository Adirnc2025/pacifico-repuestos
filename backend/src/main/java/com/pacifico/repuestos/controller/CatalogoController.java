package com.pacifico.repuestos.controller;

import com.pacifico.repuestos.dto.response.CatalogoResponse;
import com.pacifico.repuestos.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;

    // ── MARCAS ──
    @GetMapping("/api/marcas")
    public ResponseEntity<List<CatalogoResponse>> marcas() {
        return ResponseEntity.ok(catalogoService.listarMarcas());
    }

    @PostMapping("/api/marcas")
    public ResponseEntity<CatalogoResponse> crearMarca(@RequestBody Map<String,String> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(catalogoService.crearMarca(body.get("nombre"), body.get("logoUrl")));
    }

    @PutMapping("/api/marcas/{id}")
    public ResponseEntity<CatalogoResponse> editarMarca(@PathVariable Long id,
                                                         @RequestBody Map<String,String> body) {
        return ResponseEntity.ok(
            catalogoService.editarMarca(id, body.get("nombre"), body.get("logoUrl")));
    }

    @DeleteMapping("/api/marcas/{id}")
    public ResponseEntity<Void> eliminarMarca(@PathVariable Long id) {
        catalogoService.eliminarMarca(id);
        return ResponseEntity.noContent().build();
    }

    // ── MODELOS ──
    @GetMapping("/api/modelos")
    public ResponseEntity<List<CatalogoResponse>> modelos(@RequestParam Long marcaId) {
        return ResponseEntity.ok(catalogoService.listarModelosPorMarca(marcaId));
    }

    @PostMapping("/api/modelos")
    public ResponseEntity<CatalogoResponse> crearModelo(@RequestBody Map<String,Object> body) {
        Long marcaId = Long.valueOf(body.get("marcaId").toString());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(catalogoService.crearModelo(marcaId, body.get("nombre").toString()));
    }

    @DeleteMapping("/api/modelos/{id}")
    public ResponseEntity<Void> eliminarModelo(@PathVariable Long id) {
        catalogoService.eliminarModelo(id);
        return ResponseEntity.noContent().build();
    }

    // ── GENERACIONES ──
    @GetMapping("/api/generaciones")
    public ResponseEntity<List<CatalogoResponse>> generaciones(@RequestParam Long modeloId) {
        return ResponseEntity.ok(catalogoService.listarGeneracionesPorModelo(modeloId));
    }

    @PostMapping("/api/generaciones")
    public ResponseEntity<CatalogoResponse> crearGeneracion(@RequestBody Map<String,Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            catalogoService.crearGeneracion(
                Long.valueOf(body.get("modeloId").toString()),
                body.get("nombre").toString(),
                body.get("anioInicio") != null ? Integer.valueOf(body.get("anioInicio").toString()) : null,
                body.get("anioFin")    != null ? Integer.valueOf(body.get("anioFin").toString())    : null
            ));
    }

    // ── MOTORES ──
    @GetMapping("/api/motores")
    public ResponseEntity<List<CatalogoResponse>> motores(@RequestParam Long generacionId) {
        return ResponseEntity.ok(catalogoService.listarMotoresPorGeneracion(generacionId));
    }

    @PostMapping("/api/motores")
    public ResponseEntity<CatalogoResponse> crearMotor(@RequestBody Map<String,Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            catalogoService.crearMotor(
                Long.valueOf(body.get("generacionId").toString()),
                body.get("codigo").toString(),
                body.get("descripcion") != null ? body.get("descripcion").toString() : null,
                body.get("cilindrada")  != null ? body.get("cilindrada").toString()  : null
            ));
    }
}

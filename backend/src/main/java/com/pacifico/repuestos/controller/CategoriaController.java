package com.pacifico.repuestos.controller;

import com.pacifico.repuestos.exception.*;
import com.pacifico.repuestos.model.Categoria;
import com.pacifico.repuestos.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaRepository categoriaRepo;

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaRepo.findByActivoTrue());
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody Map<String,String> body) {
        Categoria c = Categoria.builder()
            .nombre(body.get("nombre"))
            .descripcion(body.get("descripcion"))
            .activo(true).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepo.save(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> editar(@PathVariable Long id,
                                             @RequestBody Map<String,String> body) {
        Categoria c = categoriaRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        c.setNombre(body.get("nombre"));
        c.setDescripcion(body.get("descripcion"));
        return ResponseEntity.ok(categoriaRepo.save(c));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Categoria c = categoriaRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        c.setActivo(false);
        categoriaRepo.save(c);
        return ResponseEntity.noContent().build();
    }
}

package com.pacifico.repuestos.controller;

import com.pacifico.repuestos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository clienteRepo;

    @GetMapping
    public ResponseEntity<List<Map<String,Object>>> listar() {
        List<Map<String,Object>> resultado = new ArrayList<>();
        clienteRepo.findAll().forEach(c -> {
            Map<String,Object> item = new LinkedHashMap<>();
            item.put("id", c.getId());
            item.put("nombre", c.getUsuario().getNombre());
            item.put("correo", c.getUsuario().getCorreo());
            item.put("telefono", c.getTelefono());
            item.put("direccion", c.getDireccion());
            item.put("activo", c.getUsuario().getActivo());
            resultado.add(item);
        });
        return ResponseEntity.ok(resultado);
    }
}

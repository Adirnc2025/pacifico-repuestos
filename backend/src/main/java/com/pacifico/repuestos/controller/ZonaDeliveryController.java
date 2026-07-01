package com.pacifico.repuestos.controller;

import com.pacifico.repuestos.model.ZonaDelivery;
import com.pacifico.repuestos.repository.ZonaDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class ZonaDeliveryController {

    private final ZonaDeliveryRepository zonaRepo;

    @GetMapping("/zonas")
    public ResponseEntity<List<ZonaDelivery>> zonas() {
        return ResponseEntity.ok(zonaRepo.findByActivoTrue());
    }

    @GetMapping("/zonas/{tipo}")
    public ResponseEntity<List<ZonaDelivery>> zonasPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(zonaRepo.findByTipoAndActivoTrue(tipo.toUpperCase()));
    }
}

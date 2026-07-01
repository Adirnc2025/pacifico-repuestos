package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.response.CatalogoResponse;
import com.pacifico.repuestos.exception.*;
import com.pacifico.repuestos.model.*;
import com.pacifico.repuestos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final MarcaRepository      marcaRepo;
    private final ModeloRepository     modeloRepo;
    private final GeneracionRepository generacionRepo;
    private final MotorRepository      motorRepo;

    // ───── MARCAS ─────
    public List<CatalogoResponse> listarMarcas() {
        return marcaRepo.findByActivoTrue().stream()
            .map(m -> CatalogoResponse.builder()
                .id(m.getId()).nombre(m.getNombre()).extra(m.getLogoUrl()).build())
            .toList();
    }

    @Transactional
    public CatalogoResponse crearMarca(String nombre, String logoUrl) {
        if (marcaRepo.existsByNombre(nombre))
            throw new BusinessException("Ya existe una marca con ese nombre");
        Marca marca = marcaRepo.save(
            Marca.builder().nombre(nombre).logoUrl(logoUrl).activo(true).build());
        return CatalogoResponse.builder()
            .id(marca.getId()).nombre(marca.getNombre()).extra(marca.getLogoUrl()).build();
    }

    @Transactional
    public CatalogoResponse editarMarca(Long id, String nombre, String logoUrl) {
        Marca marca = marcaRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Marca", id));
        marca.setNombre(nombre);
        marca.setLogoUrl(logoUrl);
        marcaRepo.save(marca);
        return CatalogoResponse.builder()
            .id(marca.getId()).nombre(marca.getNombre()).extra(marca.getLogoUrl()).build();
    }

    @Transactional
    public void eliminarMarca(Long id) {
        Marca marca = marcaRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Marca", id));
        if (!modeloRepo.findByMarcaIdAndActivoTrue(id).isEmpty())
            throw new BusinessException("No se puede eliminar: la marca tiene modelos activos");
        marca.setActivo(false);
        marcaRepo.save(marca);
    }

    // ───── MODELOS ─────
    public List<CatalogoResponse> listarModelosPorMarca(Long marcaId) {
        return modeloRepo.findByMarcaIdAndActivoTrue(marcaId).stream()
            .map(m -> CatalogoResponse.builder().id(m.getId()).nombre(m.getNombre()).build())
            .toList();
    }

    @Transactional
    public CatalogoResponse crearModelo(Long marcaId, String nombre) {
        Marca marca = marcaRepo.findById(marcaId)
            .orElseThrow(() -> new ResourceNotFoundException("Marca", marcaId));
        if (modeloRepo.existsByNombreAndMarcaId(nombre, marcaId))
            throw new BusinessException("Ya existe ese modelo para esta marca");
        Modelo modelo = modeloRepo.save(
            Modelo.builder().nombre(nombre).marca(marca).activo(true).build());
        return CatalogoResponse.builder().id(modelo.getId()).nombre(modelo.getNombre()).build();
    }

    @Transactional
    public void eliminarModelo(Long id) {
        Modelo modelo = modeloRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Modelo", id));
        modelo.setActivo(false);
        modeloRepo.save(modelo);
    }

    // ───── GENERACIONES ─────
    public List<CatalogoResponse> listarGeneracionesPorModelo(Long modeloId) {
        return generacionRepo.findByModeloId(modeloId).stream()
            .map(g -> CatalogoResponse.builder()
                .id(g.getId()).nombre(g.getNombre())
                .extra(g.getAnioInicio() + " – " + g.getAnioFin()).build())
            .toList();
    }

    @Transactional
    public CatalogoResponse crearGeneracion(Long modeloId, String nombre,
                                             Integer anioInicio, Integer anioFin) {
        Modelo modelo = modeloRepo.findById(modeloId)
            .orElseThrow(() -> new ResourceNotFoundException("Modelo", modeloId));
        Generacion g = generacionRepo.save(
            Generacion.builder().nombre(nombre).modelo(modelo)
                .anioInicio(anioInicio).anioFin(anioFin).build());
        return CatalogoResponse.builder().id(g.getId()).nombre(g.getNombre()).build();
    }

    // ───── MOTORES ─────
    public List<CatalogoResponse> listarMotoresPorGeneracion(Long generacionId) {
        return motorRepo.findByGeneracionId(generacionId).stream()
            .map(m -> CatalogoResponse.builder()
                .id(m.getId()).nombre(m.getCodigo())
                .extra(m.getDescripcion()).build())
            .toList();
    }

    @Transactional
    public CatalogoResponse crearMotor(Long generacionId, String codigo,
                                        String descripcion, String cilindrada) {
        Generacion gen = generacionRepo.findById(generacionId)
            .orElseThrow(() -> new ResourceNotFoundException("Generacion", generacionId));
        Motor motor = motorRepo.save(
            Motor.builder().codigo(codigo).descripcion(descripcion)
                .cilindrada(cilindrada).generacion(gen).build());
        return CatalogoResponse.builder()
            .id(motor.getId()).nombre(motor.getCodigo()).extra(motor.getDescripcion()).build();
    }

    public Motor getMotorById(Long id) {
        return motorRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Motor", id));
    }
}

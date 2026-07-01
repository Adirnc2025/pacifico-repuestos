package com.pacifico.repuestos.repository;
import com.pacifico.repuestos.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    List<Modelo> findByMarcaIdAndActivoTrue(Long marcaId);
    boolean existsByNombreAndMarcaId(String nombre, Long marcaId);
}

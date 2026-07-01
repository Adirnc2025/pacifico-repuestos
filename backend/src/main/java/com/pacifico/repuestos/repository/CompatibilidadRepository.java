package com.pacifico.repuestos.repository;
import com.pacifico.repuestos.model.Compatibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompatibilidadRepository extends JpaRepository<Compatibilidad, Long> {
    List<Compatibilidad> findByProductoId(Long productoId);
    boolean existsByProductoIdAndMotorId(Long productoId, Long motorId);
    void deleteByProductoIdAndMotorId(Long productoId, Long motorId);
}

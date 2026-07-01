package com.pacifico.repuestos.repository;
import com.pacifico.repuestos.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    List<Marca> findByActivoTrue();
    boolean existsByNombre(String nombre);
}

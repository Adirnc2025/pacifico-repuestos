package com.pacifico.repuestos.repository;

import com.pacifico.repuestos.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByProductoId(Long productoId);
    List<Inventario> findByStockLessThanAndStockGreaterThan(int max, int min);
}

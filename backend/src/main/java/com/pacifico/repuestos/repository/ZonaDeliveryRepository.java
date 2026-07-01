package com.pacifico.repuestos.repository;

import com.pacifico.repuestos.model.ZonaDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZonaDeliveryRepository extends JpaRepository<ZonaDelivery, Long> {
    List<ZonaDelivery> findByActivoTrue();
    List<ZonaDelivery> findByTipoAndActivoTrue(String tipo);
}

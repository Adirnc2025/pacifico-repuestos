package com.pacifico.repuestos.repository;
import com.pacifico.repuestos.model.Motor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MotorRepository extends JpaRepository<Motor, Long> {
    List<Motor> findByGeneracionId(Long generacionId);
}

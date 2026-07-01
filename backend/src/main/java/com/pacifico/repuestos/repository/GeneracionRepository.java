package com.pacifico.repuestos.repository;
import com.pacifico.repuestos.model.Generacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GeneracionRepository extends JpaRepository<Generacion, Long> {
    List<Generacion> findByModeloId(Long modeloId);
}

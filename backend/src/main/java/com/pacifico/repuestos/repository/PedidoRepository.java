package com.pacifico.repuestos.repository;

import com.pacifico.repuestos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteIdOrderByFechaPedidoDesc(Long clienteId);

    List<Pedido> findAllByOrderByFechaPedidoDesc();

    List<Pedido> findByEstadoOrderByFechaPedidoDesc(String estado);

    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado")
    Long contarPorEstado(@Param("estado") String estado);

    @Query("""
        SELECT COUNT(p) FROM Pedido p
        WHERE CAST(p.fechaPedido AS date) = CURRENT_DATE
    """)
    Long contarHoy();
}

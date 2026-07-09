package com.pacifico.repuestos.repository;

import com.pacifico.repuestos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("""
        SELECT p FROM Producto p
        LEFT JOIN FETCH p.categoria
        LEFT JOIN FETCH p.inventario
        WHERE p.activo = true
    """)
    List<Producto> findByActivoTrue();

    @Query("""
        SELECT p FROM Producto p
        LEFT JOIN FETCH p.categoria
        LEFT JOIN FETCH p.inventario
        WHERE p.activo = true AND p.destacado = true
    """)
    List<Producto> findByActivoTrueAndDestacadoTrue();

    @Query("""
        SELECT p FROM Producto p
        LEFT JOIN FETCH p.categoria
        LEFT JOIN FETCH p.inventario
        WHERE p.activo = true AND p.categoria.id = :categoriaId
    """)
    List<Producto> findByActivoTrueAndCategoriaId(@Param("categoriaId") Long categoriaId);

    @Query("""
        SELECT DISTINCT p FROM Producto p
        JOIN p.compatibilidades c
        JOIN c.motor m
        WHERE p.activo = true AND m.id = :motorId
    """)
    List<Producto> findByMotorId(@Param("motorId") Long motorId);

    @Query("""
        SELECT p FROM Producto p
        LEFT JOIN FETCH p.categoria
        LEFT JOIN FETCH p.inventario
        WHERE p.activo = true AND (
            LOWER(p.nombre) LIKE LOWER(CONCAT('%',:q,'%')) OR
            LOWER(p.codigo) LIKE LOWER(CONCAT('%',:q,'%'))
        )
    """)
    List<Producto> buscarPorNombreOCodigo(@Param("q") String q);

    @Query("""
        SELECT DISTINCT p FROM Producto p
        LEFT JOIN FETCH p.categoria
        LEFT JOIN FETCH p.inventario
        LEFT JOIN p.compatibilidades c
        LEFT JOIN c.motor m
        LEFT JOIN m.generacion g
        LEFT JOIN g.modelo mo
        LEFT JOIN mo.marca ma
        WHERE p.activo = true
          AND (:marcaId     IS NULL OR ma.id = :marcaId)
          AND (:modeloId    IS NULL OR mo.id = :modeloId)
          AND (:generacionId IS NULL OR g.id = :generacionId)
          AND (:motorId     IS NULL OR m.id  = :motorId)
          AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId)
    """)
    List<Producto> filtrar(
        @Param("marcaId")      Long marcaId,
        @Param("modeloId")     Long modeloId,
        @Param("generacionId") Long generacionId,
        @Param("motorId")      Long motorId,
        @Param("categoriaId")  Long categoriaId
    );

    boolean existsByCodigo(String codigo);
}

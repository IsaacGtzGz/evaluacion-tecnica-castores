package com.castores.almacen.repositories;

import com.castores.almacen.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByEstatus(Integer estatus);

    @Modifying
    @Transactional
    @Query(value = "CALL sp_movimiento_inventario(:idProducto, :cantidad, :tipo, :idUsuario)", nativeQuery = true)
    void ejecutarMovimiento(@Param("idProducto") Integer idProducto, @Param("cantidad") Integer cantidad,
            @Param("tipo") String tipo, @Param("idUsuario") Integer idUsuario);
}
package com.castores.almacen.repositories;

import com.castores.almacen.models.HistoricoMovimientoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoricoMovimientoRepository extends JpaRepository<HistoricoMovimientoDetalle, Integer> {

    List<HistoricoMovimientoDetalle> findByTipoMovimiento(String tipoMovimiento);
}
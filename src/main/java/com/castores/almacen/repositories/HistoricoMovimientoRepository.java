package com.castores.almacen.repositories;

import com.castores.almacen.models.HistoricoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoricoMovimientoRepository extends JpaRepository<HistoricoMovimiento, Integer> {
    List<HistoricoMovimiento> findByTipoMovimiento(String tipoMovimiento);
}
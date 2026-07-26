package com.castores.almacen.repositories;

import com.castores.almacen.models.HistoricoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoricoMovimientoRepository extends JpaRepository<HistoricoMovimiento, Integer> {
    List<HistoricoMovimiento> findByTipoMovimiento(String tipoMovimiento);
}
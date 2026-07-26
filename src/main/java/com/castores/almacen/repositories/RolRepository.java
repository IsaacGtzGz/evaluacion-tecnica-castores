package com.castores.almacen.repositories;

import com.castores.almacen.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {
}
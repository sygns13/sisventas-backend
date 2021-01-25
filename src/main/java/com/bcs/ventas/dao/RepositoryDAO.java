package com.bcs.ventas.dao;

import com.bcs.ventas.model.entities.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryDAO extends JpaRepository<Almacen, Long> {
}

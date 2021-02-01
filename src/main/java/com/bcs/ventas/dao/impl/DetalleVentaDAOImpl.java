package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleVentaDAO;
import com.bcs.ventas.dao.repo.DetalleVentaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DetalleVenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleVentaDAOImpl extends CRUDDAOImpl<DetalleVenta, Long> implements DetalleVentaDAO {

    @Autowired
    private DetalleVentaRepo repo;

    @Override
    protected GenericRepo<DetalleVenta, Long> getRepo() {
        return repo;
    }

}

package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.VentaDAO;
import com.bcs.ventas.dao.repo.VentaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class VentaDAOImpl extends CRUDDAOImpl<Venta, Long> implements VentaDAO {

    @Autowired
    private VentaRepo repo;

    @Override
    protected GenericRepo<Venta, Long> getRepo() {
        return repo;
    }

}

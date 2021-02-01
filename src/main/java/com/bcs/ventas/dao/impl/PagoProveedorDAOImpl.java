package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.PagoProveedorDAO;
import com.bcs.ventas.dao.repo.PagoProveedorRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.PagoProveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class PagoProveedorDAOImpl extends CRUDDAOImpl<PagoProveedor, Long> implements PagoProveedorDAO {

    @Autowired
    private PagoProveedorRepo repo;

    @Override
    protected GenericRepo<PagoProveedor, Long> getRepo() {
        return repo;
    }

}

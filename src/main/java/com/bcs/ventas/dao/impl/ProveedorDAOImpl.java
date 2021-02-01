package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.ProveedorDAO;
import com.bcs.ventas.dao.repo.ProveedorRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Proveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ProveedorDAOImpl extends CRUDDAOImpl<Proveedor, Long> implements ProveedorDAO {

    @Autowired
    private ProveedorRepo repo;

    @Override
    protected GenericRepo<Proveedor, Long> getRepo() {
        return repo;
    }

}

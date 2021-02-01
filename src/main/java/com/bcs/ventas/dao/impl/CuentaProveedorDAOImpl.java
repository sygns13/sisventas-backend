package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.CuentaProveedorDAO;
import com.bcs.ventas.dao.repo.CuentaProveedorRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.CuentaProveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CuentaProveedorDAOImpl extends CRUDDAOImpl<CuentaProveedor, Long> implements CuentaProveedorDAO {

    @Autowired
    private CuentaProveedorRepo repo;

    @Override
    protected GenericRepo<CuentaProveedor, Long> getRepo() {
        return repo;
    }

}

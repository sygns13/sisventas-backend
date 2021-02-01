package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.FacturaProveedorDAO;
import com.bcs.ventas.dao.repo.FacturaProveedorRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.FacturaProveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class FacturaProveedorDAOImpl extends CRUDDAOImpl<FacturaProveedor, Long> implements FacturaProveedorDAO {

    @Autowired
    private FacturaProveedorRepo repo;

    @Override
    protected GenericRepo<FacturaProveedor, Long> getRepo() {
        return repo;
    }

}

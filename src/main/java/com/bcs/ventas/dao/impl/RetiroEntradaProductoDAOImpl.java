package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.RetiroEntradaProductoDAO;
import com.bcs.ventas.dao.repo.RetiroEntradaProductoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.RetiroEntradaProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class RetiroEntradaProductoDAOImpl extends CRUDDAOImpl<RetiroEntradaProducto, Long> implements RetiroEntradaProductoDAO {

    @Autowired
    private RetiroEntradaProductoRepo repo;

    @Override
    protected GenericRepo<RetiroEntradaProducto, Long> getRepo() {
        return repo;
    }

}

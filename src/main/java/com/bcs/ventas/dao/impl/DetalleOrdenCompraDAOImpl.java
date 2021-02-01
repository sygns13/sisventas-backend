package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleOrdenCompraDAO;
import com.bcs.ventas.dao.repo.DetalleOrdenCompraRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DetalleOrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleOrdenCompraDAOImpl extends CRUDDAOImpl<DetalleOrdenCompra, Long> implements DetalleOrdenCompraDAO {

    @Autowired
    private DetalleOrdenCompraRepo repo;

    @Override
    protected GenericRepo<DetalleOrdenCompra, Long> getRepo() {
        return repo;
    }

}

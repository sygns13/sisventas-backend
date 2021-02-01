package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleEntradaStockDAO;
import com.bcs.ventas.dao.repo.DetalleEntradaStockRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DetalleEntradaStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleEntradaStockDAOImpl extends CRUDDAOImpl<DetalleEntradaStock, Long> implements DetalleEntradaStockDAO {

    @Autowired
    private DetalleEntradaStockRepo repo;

    @Override
    protected GenericRepo<DetalleEntradaStock, Long> getRepo() {
        return repo;
    }

}

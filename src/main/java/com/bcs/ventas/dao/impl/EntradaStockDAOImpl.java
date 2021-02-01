package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.EntradaStockDAO;
import com.bcs.ventas.dao.repo.EntradaStockRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.EntradaStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class EntradaStockDAOImpl extends CRUDDAOImpl<EntradaStock, Long> implements EntradaStockDAO {

    @Autowired
    private EntradaStockRepo repo;

    @Override
    protected GenericRepo<EntradaStock, Long> getRepo() {
        return repo;
    }

}

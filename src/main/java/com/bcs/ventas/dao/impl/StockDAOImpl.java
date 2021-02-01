package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.StockDAO;
import com.bcs.ventas.dao.repo.StockRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class StockDAOImpl extends CRUDDAOImpl<Stock, Long> implements StockDAO {

    @Autowired
    private StockRepo repo;

    @Override
    protected GenericRepo<Stock, Long> getRepo() {
        return repo;
    }

}

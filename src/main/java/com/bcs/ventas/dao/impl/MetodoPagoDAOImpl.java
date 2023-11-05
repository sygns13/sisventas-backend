package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.MetodoPagoDAO;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.dao.repo.MetodoPagoRepo;
import com.bcs.ventas.model.entities.MetodoPago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MetodoPagoDAOImpl extends CRUDDAOImpl<MetodoPago, Long> implements MetodoPagoDAO {

    @Autowired
    private MetodoPagoRepo repo;

    @Override
    protected GenericRepo<MetodoPago, Long> getRepo() {
        return repo;
    }
}

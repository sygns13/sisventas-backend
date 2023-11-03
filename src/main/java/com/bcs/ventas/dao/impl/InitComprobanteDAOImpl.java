package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.InitComprobanteDAO;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.dao.repo.InitComprobanteRepo;
import com.bcs.ventas.model.entities.InitComprobante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InitComprobanteDAOImpl  extends CRUDDAOImpl<InitComprobante, Long> implements InitComprobanteDAO {

    @Autowired
    private InitComprobanteRepo repo;
    @Override
    protected GenericRepo<InitComprobante, Long> getRepo() {
        return  repo;
    }
}

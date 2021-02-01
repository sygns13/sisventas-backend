package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.NotasCreditoDAO;
import com.bcs.ventas.dao.repo.NotasCreditoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.NotasCredito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class NotasCreditoDAOImpl extends CRUDDAOImpl<NotasCredito, Long> implements NotasCreditoDAO {

    @Autowired
    private NotasCreditoRepo repo;

    @Override
    protected GenericRepo<NotasCredito, Long> getRepo() {
        return repo;
    }

}

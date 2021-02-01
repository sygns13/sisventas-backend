package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DepositoDAO;
import com.bcs.ventas.dao.repo.DepositoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Deposito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DepositoDAOImpl extends CRUDDAOImpl<Deposito, Long> implements DepositoDAO {

    @Autowired
    private DepositoRepo repo;

    @Override
    protected GenericRepo<Deposito, Long> getRepo() {
        return repo;
    }

}

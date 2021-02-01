package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.BancoDAO;
import com.bcs.ventas.dao.repo.BancoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Banco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class BancoDAOImpl extends CRUDDAOImpl<Banco, Long> implements BancoDAO {

    @Autowired
    private BancoRepo repo;

    @Override
    protected GenericRepo<Banco, Long> getRepo() {
        return repo;
    }

}

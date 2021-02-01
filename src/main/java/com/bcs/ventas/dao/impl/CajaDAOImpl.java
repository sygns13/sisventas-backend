package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.CajaDAO;
import com.bcs.ventas.dao.repo.CajaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Caja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CajaDAOImpl extends CRUDDAOImpl<Caja, Long> implements CajaDAO {

    @Autowired
    private CajaRepo repo;

    @Override
    protected GenericRepo<Caja, Long> getRepo() {
        return repo;
    }

}

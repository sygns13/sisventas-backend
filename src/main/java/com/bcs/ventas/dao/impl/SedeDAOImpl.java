package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.SedeDAO;
import com.bcs.ventas.dao.repo.SedeRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Sede;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class SedeDAOImpl extends CRUDDAOImpl<Sede, Long> implements SedeDAO {

    @Autowired
    private SedeRepo repo;

    @Override
    protected GenericRepo<Sede, Long> getRepo() {
        return repo;
    }

}

package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DistritoDAO;
import com.bcs.ventas.dao.repo.DistritoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Distrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DistritoDAOImpl extends CRUDDAOImpl<Distrito, Long> implements DistritoDAO {

    @Autowired
    private DistritoRepo repo;

    @Override
    protected GenericRepo<Distrito, Long> getRepo() {
        return repo;
    }

}

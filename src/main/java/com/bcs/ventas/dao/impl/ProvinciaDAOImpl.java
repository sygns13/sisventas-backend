package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.ProvinciaDAO;
import com.bcs.ventas.dao.repo.ProvinciaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Provincia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ProvinciaDAOImpl extends CRUDDAOImpl<Provincia, Long> implements ProvinciaDAO {

    @Autowired
    private ProvinciaRepo repo;

    @Override
    protected GenericRepo<Provincia, Long> getRepo() {
        return repo;
    }

}

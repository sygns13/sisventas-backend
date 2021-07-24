package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.ServicioDAO;
import com.bcs.ventas.dao.repo.GenericRepo;

import com.bcs.ventas.dao.repo.ServicioRepo;
import com.bcs.ventas.model.entities.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServicioDAOImpl extends CRUDDAOImpl<Servicio, Long> implements ServicioDAO {

    @Autowired
    private ServicioRepo repo;

    @Override
    protected GenericRepo<Servicio, Long> getRepo() {
        return repo;
    }
}

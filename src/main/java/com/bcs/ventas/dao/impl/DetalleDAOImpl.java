package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleDAO;
import com.bcs.ventas.dao.repo.DetalleRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Detalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleDAOImpl extends CRUDDAOImpl<Detalle, Long> implements DetalleDAO {

    @Autowired
    private DetalleRepo repo;

    @Override
    protected GenericRepo<Detalle, Long> getRepo() {
        return repo;
    }

}

package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.ResumenDetalleDAO;
import com.bcs.ventas.dao.repo.ResumenDetalleRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.ResumenDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ResumenDetalleDAOImpl extends CRUDDAOImpl<ResumenDetalle, Long> implements ResumenDetalleDAO {

    @Autowired
    private ResumenDetalleRepo repo;

    @Override
    protected GenericRepo<ResumenDetalle, Long> getRepo() {
        return repo;
    }

}

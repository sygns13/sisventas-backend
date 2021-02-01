package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.UnidadDAO;
import com.bcs.ventas.dao.repo.UnidadRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Unidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UnidadDAOImpl extends CRUDDAOImpl<Unidad, Long> implements UnidadDAO {

    @Autowired
    private UnidadRepo repo;

    @Override
    protected GenericRepo<Unidad, Long> getRepo() {
        return repo;
    }

}

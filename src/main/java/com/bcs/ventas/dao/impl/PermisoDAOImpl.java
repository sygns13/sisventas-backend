package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.PermisoDAO;
import com.bcs.ventas.dao.repo.PermisoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Permiso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class PermisoDAOImpl extends CRUDDAOImpl<Permiso, Long> implements PermisoDAO {

    @Autowired
    private PermisoRepo repo;

    @Override
    protected GenericRepo<Permiso, Long> getRepo() {
        return repo;
    }

}

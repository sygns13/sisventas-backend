package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DatosUserDAO;
import com.bcs.ventas.dao.repo.DatosUserRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DatosUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DatosUserDAOImpl extends CRUDDAOImpl<DatosUser, Long> implements DatosUserDAO {

    @Autowired
    private DatosUserRepo repo;

    @Override
    protected GenericRepo<DatosUser, Long> getRepo() {
        return repo;
    }

}

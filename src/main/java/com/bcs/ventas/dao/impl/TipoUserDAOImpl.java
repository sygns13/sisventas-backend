package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.TipoUserDAO;
import com.bcs.ventas.dao.repo.TipoUserRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.TipoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TipoUserDAOImpl extends CRUDDAOImpl<TipoUser, Long> implements TipoUserDAO {

    @Autowired
    private TipoUserRepo repo;

    @Override
    protected GenericRepo<TipoUser, Long> getRepo() {
        return repo;
    }

}

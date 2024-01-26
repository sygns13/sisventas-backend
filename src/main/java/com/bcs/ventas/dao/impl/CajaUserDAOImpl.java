package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.CajaUserDAO;
import com.bcs.ventas.dao.repo.CajaUserRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.CajaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CajaUserDAOImpl  extends CRUDDAOImpl<CajaUser, Long> implements CajaUserDAO {

    @Autowired
    private CajaUserRepo repo;

    @Override
    protected GenericRepo<CajaUser, Long> getRepo() {
        return repo;
    }

}

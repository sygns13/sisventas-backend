package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.CajaDatoDAO;
import com.bcs.ventas.dao.repo.CajaDatoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.CajaDato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CajaDatoDAOImpl extends CRUDDAOImpl<CajaDato, Long> implements CajaDatoDAO {

    @Autowired
    private CajaDatoRepo repo;

    @Override
    protected GenericRepo<CajaDato, Long> getRepo() {
        return repo;
    }

}

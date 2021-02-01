package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.PresentacionDAO;
import com.bcs.ventas.dao.repo.PresentacionRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Presentacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class PresentacionDAOImpl extends CRUDDAOImpl<Presentacion, Long> implements PresentacionDAO {

    @Autowired
    private PresentacionRepo repo;

    @Override
    protected GenericRepo<Presentacion, Long> getRepo() {
        return repo;
    }

}

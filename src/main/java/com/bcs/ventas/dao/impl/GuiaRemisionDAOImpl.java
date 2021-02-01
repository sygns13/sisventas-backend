package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.GuiaRemisionDAO;
import com.bcs.ventas.dao.repo.GuiaRemisionRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.GuiaRemision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class GuiaRemisionDAOImpl extends CRUDDAOImpl<GuiaRemision, Long> implements GuiaRemisionDAO {

    @Autowired
    private GuiaRemisionRepo repo;

    @Override
    protected GenericRepo<GuiaRemision, Long> getRepo() {
        return repo;
    }

}

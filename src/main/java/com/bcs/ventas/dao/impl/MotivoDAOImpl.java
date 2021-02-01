package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.MotivoDAO;
import com.bcs.ventas.dao.repo.MotivoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Motivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class MotivoDAOImpl extends CRUDDAOImpl<Motivo, Long> implements MotivoDAO {

    @Autowired
    private MotivoRepo repo;

    @Override
    protected GenericRepo<Motivo, Long> getRepo() {
        return repo;
    }

}

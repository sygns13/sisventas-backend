package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.MotivoHasGuiaRemisionDAO;
import com.bcs.ventas.dao.repo.MotivoHasGuiaRemisionRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.MotivoHasGuiaRemision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class MotivoHasGuiaRemisionDAOImpl extends CRUDDAOImpl<MotivoHasGuiaRemision, Long> implements MotivoHasGuiaRemisionDAO {

    @Autowired
    private MotivoHasGuiaRemisionRepo repo;

    @Override
    protected GenericRepo<MotivoHasGuiaRemision, Long> getRepo() {
        return repo;
    }

}

package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleGuiaRemisionDAO;
import com.bcs.ventas.dao.repo.DetalleGuiaRemisionRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DetalleGuiaRemision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleGuiaRemisionDAOImpl extends CRUDDAOImpl<DetalleGuiaRemision, Long> implements DetalleGuiaRemisionDAO {

    @Autowired
    private DetalleGuiaRemisionRepo repo;

    @Override
    protected GenericRepo<DetalleGuiaRemision, Long> getRepo() {
        return repo;
    }

}

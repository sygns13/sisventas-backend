package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.CajaAccionDAO;
import com.bcs.ventas.dao.repo.CajaAccionRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.CajaAccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CajaAccionDAOImpl extends CRUDDAOImpl<CajaAccion, Long> implements CajaAccionDAO {

    @Autowired
    private CajaAccionRepo repo;

    @Override
    protected GenericRepo<CajaAccion, Long> getRepo() {
        return repo;
    }

}

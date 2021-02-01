package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.IngresoSalidaCajaDAO;
import com.bcs.ventas.dao.repo.IngresoSalidaCajaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class IngresoSalidaCajaDAOImpl extends CRUDDAOImpl<IngresoSalidaCaja, Long> implements IngresoSalidaCajaDAO {

    @Autowired
    private IngresoSalidaCajaRepo repo;

    @Override
    protected GenericRepo<IngresoSalidaCaja, Long> getRepo() {
        return repo;
    }

}

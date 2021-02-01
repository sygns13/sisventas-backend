package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.CotizacionDAO;
import com.bcs.ventas.dao.repo.CotizacionRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Cotizacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CotizacionDAOImpl extends CRUDDAOImpl<Cotizacion, Long> implements CotizacionDAO {

    @Autowired
    private CotizacionRepo repo;

    @Override
    protected GenericRepo<Cotizacion, Long> getRepo() {
        return repo;
    }

}

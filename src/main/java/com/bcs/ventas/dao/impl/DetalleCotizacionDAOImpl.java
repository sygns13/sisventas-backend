package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleCotizacionDAO;
import com.bcs.ventas.dao.repo.DetalleCotizacionRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DetalleCotizacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleCotizacionDAOImpl extends CRUDDAOImpl<DetalleCotizacion, Long> implements DetalleCotizacionDAO {

    @Autowired
    private DetalleCotizacionRepo repo;

    @Override
    protected GenericRepo<DetalleCotizacion, Long> getRepo() {
        return repo;
    }

}

package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.TipoComprobanteDAO;
import com.bcs.ventas.dao.repo.TipoComprobanteRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.TipoComprobante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TipoComprobanteDAOImpl extends CRUDDAOImpl<TipoComprobante, Long> implements TipoComprobanteDAO {

    @Autowired
    private TipoComprobanteRepo repo;

    @Override
    protected GenericRepo<TipoComprobante, Long> getRepo() {
        return repo;
    }

}

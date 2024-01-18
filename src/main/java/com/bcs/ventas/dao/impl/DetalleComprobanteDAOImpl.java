package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleComprobanteDAO;
import com.bcs.ventas.dao.repo.DetalleComprobanteRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DetalleComprobante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleComprobanteDAOImpl extends CRUDDAOImpl<DetalleComprobante, Long> implements DetalleComprobanteDAO {

    @Autowired
    private DetalleComprobanteRepo repo;

    @Override
    protected GenericRepo<DetalleComprobante, Long> getRepo() {
        return repo;
    }

}

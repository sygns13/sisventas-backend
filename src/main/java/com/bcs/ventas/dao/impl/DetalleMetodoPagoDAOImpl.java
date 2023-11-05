package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleMetodoPagoDAO;
import com.bcs.ventas.dao.repo.DetalleMetodoPagoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DetalleMetodoPago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleMetodoPagoDAOImpl extends CRUDDAOImpl<DetalleMetodoPago, Long> implements DetalleMetodoPagoDAO {

    @Autowired
    private DetalleMetodoPagoRepo repo;

    @Override
    protected GenericRepo<DetalleMetodoPago, Long> getRepo() {
        return repo;
    }

}

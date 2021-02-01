package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleOrdenPedidoDAO;
import com.bcs.ventas.dao.repo.DetalleOrdenPedidoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DetalleOrdenPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleOrdenPedidoDAOImpl extends CRUDDAOImpl<DetalleOrdenPedido, Long> implements DetalleOrdenPedidoDAO {

    @Autowired
    private DetalleOrdenPedidoRepo repo;

    @Override
    protected GenericRepo<DetalleOrdenPedido, Long> getRepo() {
        return repo;
    }

}

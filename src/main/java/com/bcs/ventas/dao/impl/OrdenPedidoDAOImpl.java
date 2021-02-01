package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.OrdenPedidoDAO;
import com.bcs.ventas.dao.repo.OrdenPedidoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.OrdenPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class OrdenPedidoDAOImpl extends CRUDDAOImpl<OrdenPedido, Long> implements OrdenPedidoDAO {

    @Autowired
    private OrdenPedidoRepo repo;

    @Override
    protected GenericRepo<OrdenPedido, Long> getRepo() {
        return repo;
    }

}

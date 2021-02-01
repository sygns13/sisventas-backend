package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.OrdenCompraDAO;
import com.bcs.ventas.dao.repo.OrdenCompraRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.OrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class OrdenCompraDAOImpl extends CRUDDAOImpl<OrdenCompra, Long> implements OrdenCompraDAO {

    @Autowired
    private OrdenCompraRepo repo;

    @Override
    protected GenericRepo<OrdenCompra, Long> getRepo() {
        return repo;
    }

}

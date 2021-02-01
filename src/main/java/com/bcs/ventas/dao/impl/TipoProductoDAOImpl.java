package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.TipoProductoDAO;
import com.bcs.ventas.dao.repo.TipoProductoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.TipoProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TipoProductoDAOImpl extends CRUDDAOImpl<TipoProducto, Long> implements TipoProductoDAO {

    @Autowired
    private TipoProductoRepo repo;

    @Override
    protected GenericRepo<TipoProducto, Long> getRepo() {
        return repo;
    }

}

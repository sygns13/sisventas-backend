package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DetalleUnidadProductoDAO;
import com.bcs.ventas.dao.repo.DetalleUnidadProductoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.DetalleUnidadProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DetalleUnidadProductoDAOImpl extends CRUDDAOImpl<DetalleUnidadProducto, Long> implements DetalleUnidadProductoDAO {

    @Autowired
    private DetalleUnidadProductoRepo repo;

    @Override
    protected GenericRepo<DetalleUnidadProducto, Long> getRepo() {
        return repo;
    }

}

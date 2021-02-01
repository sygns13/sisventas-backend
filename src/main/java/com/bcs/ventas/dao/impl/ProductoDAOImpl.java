package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.ProductoDAO;
import com.bcs.ventas.dao.repo.ProductoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ProductoDAOImpl extends CRUDDAOImpl<Producto, Long> implements ProductoDAO {

    @Autowired
    private ProductoRepo repo;

    @Override
    protected GenericRepo<Producto, Long> getRepo() {
        return repo;
    }

}

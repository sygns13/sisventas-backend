package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.repo.AlmacenRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Almacen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class AlmacenDAOImpl extends CRUDDAOImpl<Almacen, Long> implements AlmacenDAO {

    @Autowired
    private AlmacenRepo repo;

    @Override
    protected GenericRepo<Almacen, Long> getRepo() {
        return repo;
    }

}

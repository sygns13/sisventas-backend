package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.CobroVentaDAO;
import com.bcs.ventas.dao.repo.CobroVentaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.CobroVenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CobroVentaDAOImpl extends CRUDDAOImpl<CobroVenta, Long> implements CobroVentaDAO {

    @Autowired
    private CobroVentaRepo repo;

    @Override
    protected GenericRepo<CobroVenta, Long> getRepo() {
        return repo;
    }

}

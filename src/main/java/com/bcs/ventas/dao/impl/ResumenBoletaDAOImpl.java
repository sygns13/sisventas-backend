package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.ResumenBoletaDAO;
import com.bcs.ventas.dao.repo.ResumenBoletaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.ResumenBoleta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ResumenBoletaDAOImpl extends CRUDDAOImpl<ResumenBoleta, Long> implements ResumenBoletaDAO {

    @Autowired
    private ResumenBoletaRepo repo;

    @Override
    protected GenericRepo<ResumenBoleta, Long> getRepo() {
        return repo;
    }

}

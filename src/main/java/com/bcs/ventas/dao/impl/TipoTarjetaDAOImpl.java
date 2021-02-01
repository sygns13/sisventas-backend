package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.TipoTarjetaDAO;
import com.bcs.ventas.dao.repo.TipoTarjetaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.TipoTarjeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TipoTarjetaDAOImpl extends CRUDDAOImpl<TipoTarjeta, Long> implements TipoTarjetaDAO {

    @Autowired
    private TipoTarjetaRepo repo;

    @Override
    protected GenericRepo<TipoTarjeta, Long> getRepo() {
        return repo;
    }

}

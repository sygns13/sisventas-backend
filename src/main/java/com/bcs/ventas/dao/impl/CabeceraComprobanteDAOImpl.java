package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.CabeceraComprobanteDAO;
import com.bcs.ventas.dao.repo.CabeceraComprobanteRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.CabeceraComprobante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CabeceraComprobanteDAOImpl extends CRUDDAOImpl<CabeceraComprobante, Long> implements CabeceraComprobanteDAO {

    @Autowired
    private CabeceraComprobanteRepo repo;

    @Override
    protected GenericRepo<CabeceraComprobante, Long> getRepo() {
        return repo;
    }

}

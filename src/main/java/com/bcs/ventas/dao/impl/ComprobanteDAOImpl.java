package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.ComprobanteDAO;
import com.bcs.ventas.dao.repo.ComprobanteRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Comprobante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ComprobanteDAOImpl extends CRUDDAOImpl<Comprobante, Long> implements ComprobanteDAO {

    @Autowired
    private ComprobanteRepo repo;

    @Override
    protected GenericRepo<Comprobante, Long> getRepo() {
        return repo;
    }

}

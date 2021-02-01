package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.MiscuentaDAO;
import com.bcs.ventas.dao.repo.MiscuentaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Miscuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class MiscuentaDAOImpl extends CRUDDAOImpl<Miscuenta, Long> implements MiscuentaDAO {

    @Autowired
    private MiscuentaRepo repo;

    @Override
    protected GenericRepo<Miscuenta, Long> getRepo() {
        return repo;
    }

}

package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.EmpresaDAO;
import com.bcs.ventas.dao.repo.EmpresaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class EmpresaDAOImpl extends CRUDDAOImpl<Empresa, Long> implements EmpresaDAO {

    @Autowired
    private EmpresaRepo repo;

    @Override
    protected GenericRepo<Empresa, Long> getRepo() {
        return repo;
    }

}

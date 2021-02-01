package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.DepartamentoDAO;
import com.bcs.ventas.dao.repo.DepartamentoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Departamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DepartamentoDAOImpl extends CRUDDAOImpl<Departamento, Long> implements DepartamentoDAO {

    @Autowired
    private DepartamentoRepo repo;

    @Override
    protected GenericRepo<Departamento, Long> getRepo() {
        return repo;
    }

}

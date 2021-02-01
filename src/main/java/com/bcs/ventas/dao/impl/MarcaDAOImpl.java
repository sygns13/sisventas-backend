package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.MarcaDAO;
import com.bcs.ventas.dao.repo.MarcaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Marca;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class MarcaDAOImpl extends CRUDDAOImpl<Marca, Long> implements MarcaDAO {

    @Autowired
    private MarcaRepo repo;

    @Override
    protected GenericRepo<Marca, Long> getRepo() {
        return repo;
    }

}

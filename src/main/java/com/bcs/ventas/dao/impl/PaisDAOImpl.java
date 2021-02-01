package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.PaisDAO;
import com.bcs.ventas.dao.repo.PaisRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Pais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class PaisDAOImpl extends CRUDDAOImpl<Pais, Long> implements PaisDAO {

    @Autowired
    private PaisRepo repo;

    @Override
    protected GenericRepo<Pais, Long> getRepo() {
        return repo;
    }

}

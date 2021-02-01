package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.LoteDAO;
import com.bcs.ventas.dao.repo.LoteRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Lote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class LoteDAOImpl extends CRUDDAOImpl<Lote, Long> implements LoteDAO {

    @Autowired
    private LoteRepo repo;

    @Override
    protected GenericRepo<Lote, Long> getRepo() {
        return repo;
    }

}

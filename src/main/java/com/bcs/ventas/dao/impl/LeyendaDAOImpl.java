package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.LeyendaDAO;
import com.bcs.ventas.dao.repo.LeyendaRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Leyenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class LeyendaDAOImpl extends CRUDDAOImpl<Leyenda, Long> implements LeyendaDAO {

    @Autowired
    private LeyendaRepo repo;

    @Override
    protected GenericRepo<Leyenda, Long> getRepo() {
        return repo;
    }

}

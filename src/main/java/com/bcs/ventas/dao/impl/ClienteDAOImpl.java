package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.ClienteDAO;
import com.bcs.ventas.dao.repo.ClienteRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ClienteDAOImpl extends CRUDDAOImpl<Cliente, Long> implements ClienteDAO {

    @Autowired
    private ClienteRepo repo;

    @Override
    protected GenericRepo<Cliente, Long> getRepo() {
        return repo;
    }

}

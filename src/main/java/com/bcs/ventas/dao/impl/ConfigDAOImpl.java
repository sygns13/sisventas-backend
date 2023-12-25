package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.ConfigDAO;
import com.bcs.ventas.dao.repo.ConfigRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ConfigDAOImpl extends CRUDDAOImpl<Config, String> implements ConfigDAO {

    @Autowired
    private ConfigRepo repo;

    @Override
    protected GenericRepo<Config, String> getRepo() {
        return repo;
    }

}

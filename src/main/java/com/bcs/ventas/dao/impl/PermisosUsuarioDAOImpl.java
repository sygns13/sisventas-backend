package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.PermisosUsuarioDAO;
import com.bcs.ventas.dao.repo.PermisosUsuarioRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.PermisosUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class PermisosUsuarioDAOImpl extends CRUDDAOImpl<PermisosUsuario, Long> implements PermisosUsuarioDAO {

    @Autowired
    private PermisosUsuarioRepo repo;

    @Override
    protected GenericRepo<PermisosUsuario, Long> getRepo() {
        return repo;
    }

}

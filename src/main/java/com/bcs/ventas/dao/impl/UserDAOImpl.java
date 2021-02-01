package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.UserDAO;
import com.bcs.ventas.dao.repo.UserRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserDAOImpl extends CRUDDAOImpl<User, Long> implements UserDAO {

    @Autowired
    private UserRepo repo;

    @Override
    protected GenericRepo<User, Long> getRepo() {
        return repo;
    }

}

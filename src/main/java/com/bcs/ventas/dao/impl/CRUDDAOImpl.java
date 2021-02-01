package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.CRUDDAO;
import com.bcs.ventas.dao.repo.GenericRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CRUDDAOImpl<T, ID> implements CRUDDAO<T, ID> {

    @Autowired
    protected abstract GenericRepo<T, ID> getRepo();


    @Override
    public T registrar(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public T modificar(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public List<T> listar() throws Exception {
        return getRepo().findAll();
    }

    @Override
    public T listarPorId(ID id) throws Exception {
        return getRepo().findById(id).orElse(null);
    }

    @Override
    public void eliminar(ID id) throws Exception {
        getRepo().deleteById(id);
    }
}

package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.RepositoryDAO;
import com.bcs.ventas.model.entities.Almacen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AlmacenDAOImpl implements AlmacenDAO {

    @Autowired
    private RepositoryDAO repo;


    @Override
    public Almacen registrar(Almacen a) throws Exception {
        return repo.save(a);
    }

    @Override
    public Almacen modificar(Almacen a) throws Exception {
        return repo.save(a);
    }

    @Override
    public List<Almacen> listar() throws Exception {
        return repo.findAll();
    }

    @Override
    public Almacen listarPorId(Long id) throws Exception {
        Optional<Almacen> op = repo.findById(id);
        return op.isPresent() ?op.get() : new Almacen();
    }

    @Override
    public void eliminar(Long id) throws Exception {
        repo.deleteById(id);
    }

    //Otros metodos

}

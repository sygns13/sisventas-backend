package com.bcs.ventas.dao;

import java.util.List;
import com.bcs.ventas.model.entities.Almacen;

public interface AlmacenDAO{

    Almacen registrar(Almacen a) throws Exception;

    Almacen modificar(Almacen a) throws Exception;

    List<Almacen> listar() throws Exception;

    Almacen listarPorId(Long id) throws Exception;

    void eliminar(Long id) throws Exception;
}

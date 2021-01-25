package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.Almacen;

import java.util.List;

public interface AlmacenService {

    Almacen registrar(Almacen a) throws Exception;

    Almacen modificar(Almacen a) throws Exception;

    List<Almacen> listar() throws Exception;

    Almacen listarPorId(Long id) throws Exception;

    void eliminar(Long id) throws Exception;


}

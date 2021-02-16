package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.TipoProducto;


public interface TipoProductoService extends GeneralService<TipoProducto, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

}

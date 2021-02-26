package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.TipoProducto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TipoProductoService extends GeneralService<TipoProducto, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<TipoProducto> listar(Pageable pageable, String buscar) throws Exception;

}

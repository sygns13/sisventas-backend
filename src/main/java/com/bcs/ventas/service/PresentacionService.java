package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.Presentacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PresentacionService extends GeneralService<Presentacion, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<Presentacion> listar(Pageable pageable, String buscar) throws Exception;

}

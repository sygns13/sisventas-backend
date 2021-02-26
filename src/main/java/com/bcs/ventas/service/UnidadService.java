package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.Unidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UnidadService extends GeneralService<Unidad, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<Unidad> listar(Pageable pageable, String buscar) throws Exception;

}

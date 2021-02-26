package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MarcaService extends GeneralService<Marca, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<Marca> listar(Pageable pageable, String buscar) throws Exception;

}

package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.Banco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BancoService extends GeneralService<Banco, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<Banco> listar(Pageable pageable, String buscar) throws Exception;

}

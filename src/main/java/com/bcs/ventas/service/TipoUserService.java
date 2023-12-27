package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.TipoUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoUserService extends GeneralService<TipoUser, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<TipoUser> listar(Pageable pageable, String buscar) throws Exception;

}
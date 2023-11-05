package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.TipoTarjeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoTarjetaService extends GeneralService<TipoTarjeta, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<TipoTarjeta> listar(Pageable pageable, String buscar) throws Exception;
}

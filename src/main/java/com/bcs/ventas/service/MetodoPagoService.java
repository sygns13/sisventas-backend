package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.MetodoPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MetodoPagoService extends GeneralService<MetodoPago, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<MetodoPago> listar(Pageable pageable, String buscar) throws Exception;
}

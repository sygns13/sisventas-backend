package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.CobroVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CobroVentaService extends GeneralService<CobroVenta, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<CobroVenta> listar(Pageable pageable, String buscar) throws Exception;
}

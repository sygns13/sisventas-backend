package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.TipoComprobante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoComprobanteService extends GeneralService<TipoComprobante, Long> {

    Page<TipoComprobante> listar(Pageable pageable, String buscar) throws Exception;

    void altabaja(Long id, Integer valor) throws Exception;
}

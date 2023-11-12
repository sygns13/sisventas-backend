package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.InitComprobante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InitComprobanteService extends GeneralService<InitComprobante, Long> {

    void altabaja(Long id, Integer valor, Long userId) throws Exception;

    Page<InitComprobante> listar(Pageable pageable, String buscar, Long tipoComprobante, Long almacenId) throws Exception;

    List<InitComprobante> listar(Long tipoComprobante, Long almacenId) throws Exception;
}

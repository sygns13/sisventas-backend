package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.PagoProveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PagoProveedorService extends GeneralService<PagoProveedor, Long>{

    void altabaja(Long id, Integer valor) throws Exception;

    Page<PagoProveedor> listar(Pageable pageable, String buscar) throws Exception;
}

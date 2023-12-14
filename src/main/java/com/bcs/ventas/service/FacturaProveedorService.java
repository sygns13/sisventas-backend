package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.FacturaProveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacturaProveedorService extends GeneralService<FacturaProveedor, Long>{

    void altabaja(Long id, Integer valor) throws Exception;

    Page<FacturaProveedor> listar(Pageable pageable, String buscar) throws Exception;
}

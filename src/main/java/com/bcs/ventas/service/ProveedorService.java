package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProveedorService extends GeneralService<Proveedor, Long>{

    Page<Proveedor> listar(Pageable pageable, String buscar) throws Exception;

    Proveedor getByDocument(String document) throws Exception;
}

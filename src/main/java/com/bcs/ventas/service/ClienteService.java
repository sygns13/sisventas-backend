package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService extends GeneralService<Cliente, Long> {

    Page<Cliente> listar(Pageable pageable, String buscar) throws Exception;

    Cliente getByDocument(String document) throws Exception;
}

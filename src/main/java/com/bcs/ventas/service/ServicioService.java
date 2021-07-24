package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServicioService extends GeneralService<Servicio, Long> {

    Page<Servicio> listar(Pageable pageable, String buscar) throws Exception;
}

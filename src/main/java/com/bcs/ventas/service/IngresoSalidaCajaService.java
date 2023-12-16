package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IngresoSalidaCajaService extends GeneralService<IngresoSalidaCaja, Long> {

    Page<IngresoSalidaCaja> listar(Pageable pageable, String buscar, Long almacenId) throws Exception;
}

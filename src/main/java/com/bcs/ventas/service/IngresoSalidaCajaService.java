package com.bcs.ventas.service;

import com.bcs.ventas.model.dto.EgresosOtrosDTO;
import com.bcs.ventas.model.dto.IngresosOtrosDTO;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IngresoSalidaCajaService extends GeneralService<IngresoSalidaCaja, Long> {

    Page<IngresoSalidaCaja> listar(Pageable pageable, String buscar, Long almacenId) throws Exception;

    IngresosOtrosDTO listarIngresosReporte(Pageable pageable, FiltroGeneral filtros) throws Exception;

    EgresosOtrosDTO listarEgresosReporte(Pageable pageable, FiltroGeneral filtros) throws Exception;
}

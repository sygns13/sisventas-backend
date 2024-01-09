package com.bcs.ventas.service.reportes;

import com.bcs.ventas.model.dto.CajaSucursalDTO;
import com.bcs.ventas.utils.beans.FiltroGeneral;

public interface CajaReportService {

    CajaSucursalDTO cajaDiariaSucursalReporte(String fecha, Long almacenID) throws Exception;

    CajaSucursalDTO resumenCajaReporte(FiltroGeneral filtros) throws Exception;
}

package com.bcs.ventas.service.reportes;

import com.bcs.ventas.model.dto.CajaSucursalDTO;

public interface CajaReportService {

    CajaSucursalDTO cajaDiariaSucursalReporte(String fecha, Long almacenID) throws Exception;
}

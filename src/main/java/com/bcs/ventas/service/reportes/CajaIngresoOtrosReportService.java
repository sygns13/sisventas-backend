package com.bcs.ventas.service.reportes;

import com.bcs.ventas.utils.beans.FiltroGeneral;

public interface CajaIngresoOtrosReportService {

    byte[] exportPdf(FiltroGeneral filtros) throws Exception;
    byte[] exportXls(FiltroGeneral filtros) throws Exception;
}

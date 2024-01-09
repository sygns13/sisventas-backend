package com.bcs.ventas.service.reportes;

import com.bcs.ventas.utils.beans.FiltroVenta;

public interface CuentasCobrarDetalladoReportService {

    byte[] exportPdf(FiltroVenta filtros) throws Exception;
    byte[] exportXls(FiltroVenta filtros) throws Exception;
}

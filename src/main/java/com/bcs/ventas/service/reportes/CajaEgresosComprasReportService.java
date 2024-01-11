package com.bcs.ventas.service.reportes;

import com.bcs.ventas.utils.beans.FiltroEntradaStock;

public interface CajaEgresosComprasReportService {

    byte[] exportPdf(FiltroEntradaStock filtros) throws Exception;
    byte[] exportXls(FiltroEntradaStock filtros) throws Exception;
}

package com.bcs.ventas.service.comprobantes;

import com.bcs.ventas.utils.beans.FiltroEntradaStock;

public interface FacturaDetailReportService {

    byte[] exportPdf(Long idVenta) throws Exception;
}

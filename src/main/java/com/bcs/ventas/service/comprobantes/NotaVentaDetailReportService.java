package com.bcs.ventas.service.comprobantes;

public interface NotaVentaDetailReportService {

    byte[] exportPdf(Long idVenta) throws Exception;
}

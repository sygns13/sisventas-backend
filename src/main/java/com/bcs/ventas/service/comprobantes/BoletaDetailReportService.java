package com.bcs.ventas.service.comprobantes;

public interface BoletaDetailReportService {

    byte[] exportPdf(Long idVenta) throws Exception;
}

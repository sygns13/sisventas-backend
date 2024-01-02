package com.bcs.ventas.service.reportes;

public interface ProductosPrecioReportService {

    byte[] exportPdf(Long almacenId, Long unidadId) throws Exception;
    byte[] exportXls(Long almacenId, Long unidadId) throws Exception;
}

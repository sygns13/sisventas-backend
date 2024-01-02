package com.bcs.ventas.service.reportes;


public interface ProductosSucursalReportService {

    byte[] exportPdf(Long almacenId) throws Exception;
    byte[] exportXls(Long almacenId) throws Exception;
}

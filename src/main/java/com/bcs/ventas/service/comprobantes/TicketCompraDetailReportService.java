package com.bcs.ventas.service.comprobantes;

public interface TicketCompraDetailReportService {

    byte[] exportPdf(Long idCompra) throws Exception;
}

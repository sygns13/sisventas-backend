package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.comprobantesbeans.TicketCompraDetailReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface TicketCompraJasperService {

    byte[] exportToPdf(List<TicketCompraDetailReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws JRException, FileNotFoundException;
}

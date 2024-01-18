package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.comprobantesbeans.FacturaDetailReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface FacturaJasperService {

    byte[] exportToPdf(List<FacturaDetailReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws JRException, FileNotFoundException;
}

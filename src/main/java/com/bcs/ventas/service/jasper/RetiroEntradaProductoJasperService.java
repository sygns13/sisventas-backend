package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.MovimientosProductosReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface RetiroEntradaProductoJasperService extends ReportGeneratorService<MovimientosProductosReport>{

    byte[] exportToPdf(List<MovimientosProductosReport> list, String nameData, String nameReport, Map<String, String> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<MovimientosProductosReport> list, String nameData, String nameReport, String sheet, Map<String, String> parametros) throws JRException, FileNotFoundException;
}

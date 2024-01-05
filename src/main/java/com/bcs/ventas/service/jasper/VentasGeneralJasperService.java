package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.VentasGeneralReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface VentasGeneralJasperService extends ReportGeneratorService<VentasGeneralReport>{

    byte[] exportToPdf(List<VentasGeneralReport> list, String nameData, String nameReport, Map<String, String> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<VentasGeneralReport> list, String nameData, String nameReport, String sheet, Map<String, String> parametros) throws JRException, FileNotFoundException;
}

package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.ComprasGeneralReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface ComprasGeneralJasperService extends ReportGeneratorService<ComprasGeneralReport>{

    byte[] exportToPdf(List<ComprasGeneralReport> list, String nameData, String nameReport, Map<String, String> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<ComprasGeneralReport> list, String nameData, String nameReport, String sheet, Map<String, String> parametros) throws JRException, FileNotFoundException;
}

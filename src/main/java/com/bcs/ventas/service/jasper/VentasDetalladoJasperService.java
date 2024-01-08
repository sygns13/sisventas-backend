package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.VentasDetalladoReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface VentasDetalladoJasperService extends ReportGeneratorService<VentasDetalladoReport>{

    byte[] exportToPdf(List<VentasDetalladoReport> list, String nameData, String nameReport, Map<String, String> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<VentasDetalladoReport> list, String nameData, String nameReport, String sheet, Map<String, String> parametros) throws JRException, FileNotFoundException;
}

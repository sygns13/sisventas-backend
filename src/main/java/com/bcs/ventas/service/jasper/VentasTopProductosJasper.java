package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.VentasTopProductosReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface VentasTopProductosJasper extends ReportGeneratorService<VentasTopProductosReport>{

    byte[] exportToPdf(List<VentasTopProductosReport> list, String nameData, String nameReport, Map<String, String> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<VentasTopProductosReport> list, String nameData, String nameReport, String sheet, Map<String, String> parametros) throws JRException, FileNotFoundException;
}

package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.CajaIngresoVentasReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface CajaIngresoVentasJasperService extends ReportGeneratorService<CajaIngresoVentasReport>{

    byte[] exportToPdf(List<CajaIngresoVentasReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<CajaIngresoVentasReport> list, String nameData, String nameReport, String sheet, Map<String, Object> parametros) throws JRException, FileNotFoundException;
}

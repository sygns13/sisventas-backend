package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.CuentasCobrarDetalladoReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface CuentasCobrarDetalladoJasperService extends ReportGeneratorService<CuentasCobrarDetalladoReport>{

    byte[] exportToPdf(List<CuentasCobrarDetalladoReport> list, String nameData, String nameReport, Map<String, String> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<CuentasCobrarDetalladoReport> list, String nameData, String nameReport, String sheet, Map<String, String> parametros) throws JRException, FileNotFoundException;
}

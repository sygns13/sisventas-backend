package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.CajaEgresosComprasReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface CajaEgresosComprasJasperService extends ReportGeneratorService<CajaEgresosComprasReport>{

    byte[] exportToPdf(List<CajaEgresosComprasReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<CajaEgresosComprasReport> list, String nameData, String nameReport, String sheet, Map<String, Object> parametros) throws JRException, FileNotFoundException;
}

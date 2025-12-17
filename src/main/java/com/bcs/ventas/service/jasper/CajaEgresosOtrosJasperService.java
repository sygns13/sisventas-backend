package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.CajaEgresosOtrosReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CajaEgresosOtrosJasperService extends ReportGeneratorService<CajaEgresosOtrosReport>{

    byte[] exportToPdf(List<CajaEgresosOtrosReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws JRException, IOException;

    byte[] exportToXls(List<CajaEgresosOtrosReport> list, String nameData, String nameReport, String sheet, Map<String, Object> parametros) throws JRException, IOException;
}

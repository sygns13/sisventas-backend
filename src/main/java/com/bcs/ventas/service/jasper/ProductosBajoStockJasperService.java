package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.ProductosBajoStockReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface ProductosBajoStockJasperService extends ReportGeneratorService<ProductosBajoStockReport>{

    byte[] exportToPdf(List<ProductosBajoStockReport> list, String nameData, String nameReport, Map<String, String> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<ProductosBajoStockReport> list, String nameData, String nameReport, String sheet, Map<String, String> parametros) throws JRException, FileNotFoundException;
}

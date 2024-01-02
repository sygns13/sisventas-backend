package com.bcs.ventas.service.jasper;

import com.bcs.ventas.utils.reportbeans.ProductosSucursalReport;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;

public interface ProductosSucursalJasperService extends ReportGeneratorService<ProductosSucursalReport>{

    byte[] exportToPdf(List<ProductosSucursalReport> list, String sucursal, String nameData, String nameReport) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<ProductosSucursalReport> list, String sucursal, String nameData, String nameReport, String sheet) throws JRException, FileNotFoundException;
}

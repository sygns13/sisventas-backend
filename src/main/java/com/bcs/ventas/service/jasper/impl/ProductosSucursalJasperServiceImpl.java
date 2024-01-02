package com.bcs.ventas.service.jasper.impl;

import com.bcs.ventas.service.jasper.ProductosSucursalJasperService;
import com.bcs.ventas.utils.reportbeans.ProductosSucursalReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductosSucursalJasperServiceImpl extends ReportGeneratorServiceImpl<ProductosSucursalReport> implements ProductosSucursalJasperService {

    public byte[] exportToPdf(List<ProductosSucursalReport> list, String sucursal, String nameData, String nameReport) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(list, sucursal, nameData, nameReport));
    }

    public byte[] exportToXls(List<ProductosSucursalReport> list, String sucursal, String nameData, String nameReport, String sheet) throws JRException, FileNotFoundException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(byteArray);
        JRXlsxExporter exporter = new JRXlsxExporter();

        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[] { sheet });
        exporter.setConfiguration(reportConfigXLS);

        exporter.setExporterInput(new SimpleExporterInput(getReport(list, sucursal, nameData, nameReport)));
        exporter.setExporterOutput(output);

        exporter.exportReport();
        output.close();
        return byteArray.toByteArray();
    }


    private JasperPrint getReport(List<ProductosSucursalReport> list, String sucursal, String nameData, String nameReport) throws FileNotFoundException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(nameData, new JRBeanCollectionDataSource(list));
        params.put("sucursal", sucursal);

        String classpath = "classpath:reportes/" + nameReport + ".jrxml";

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(classpath)
                        .getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}

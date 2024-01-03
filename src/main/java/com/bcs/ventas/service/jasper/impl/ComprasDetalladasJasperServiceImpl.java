package com.bcs.ventas.service.jasper.impl;

import com.bcs.ventas.service.jasper.ComprasDetalladasJasperService;
import com.bcs.ventas.utils.reportbeans.ComprasDetalladasReport;
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
public class ComprasDetalladasJasperServiceImpl extends ReportGeneratorServiceImpl<ComprasDetalladasReport> implements ComprasDetalladasJasperService {

    public byte[] exportToPdf(List<ComprasDetalladasReport> list, String nameData, String nameReport, Map<String, String> parametros) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(list, nameData, nameReport, parametros));
    }

    public byte[] exportToXls(List<ComprasDetalladasReport> list, String nameData, String nameReport, String sheet, Map<String, String> parametros) throws JRException, FileNotFoundException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(byteArray);
        JRXlsxExporter exporter = new JRXlsxExporter();

        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[] { sheet });
        exporter.setConfiguration(reportConfigXLS);

        exporter.setExporterInput(new SimpleExporterInput(getReport(list, nameData, nameReport, parametros)));
        exporter.setExporterOutput(output);

        exporter.exportReport();
        output.close();
        return byteArray.toByteArray();
    }


    private JasperPrint getReport(List<ComprasDetalladasReport> list, String nameData, String nameReport, Map<String, String> parametros) throws FileNotFoundException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(nameData, new JRBeanCollectionDataSource(list));
        params.put("sucursal", parametros.get("sucursal"));
        params.put("fechaHasta", parametros.get("fechaHasta"));
        params.put("fechaDesde", parametros.get("fechaDesde"));
        params.put("estado", parametros.get("estado"));
        params.put("usuario", parametros.get("usuario"));
        params.put("nombreProveedor", parametros.get("nombreProveedor"));
        params.put("documentoProveedor", parametros.get("documentoProveedor"));
        params.put("facturada", parametros.get("facturada"));
        params.put("actualizada", parametros.get("actualizada"));
        params.put("producto", parametros.get("producto"));

        params.put("tipoDocumento", parametros.get("tipoDocumento"));

        String classpath = "classpath:reportes/" + nameReport + ".jrxml";

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(classpath)
                        .getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}

package com.bcs.ventas.service.jasper.impl;

import com.bcs.ventas.service.jasper.VentasTopProductosJasper;
import com.bcs.ventas.utils.reportbeans.VentasTopProductosReport;
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
public class VentasTopProductosJasperImpl extends ReportGeneratorServiceImpl<VentasTopProductosReport> implements VentasTopProductosJasper {

    public byte[] exportToPdf(List<VentasTopProductosReport> list, String nameData, String nameReport, Map<String, String> parametros) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(list, nameData, nameReport, parametros));
    }

    public byte[] exportToXls(List<VentasTopProductosReport> list, String nameData, String nameReport, String sheet, Map<String, String> parametros) throws JRException, FileNotFoundException {
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


    private JasperPrint getReport(List<VentasTopProductosReport> list, String nameData, String nameReport, Map<String, String> parametros) throws FileNotFoundException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(nameData, new JRBeanCollectionDataSource(list));
        params.put("sucursal", parametros.get("sucursal"));
        params.put("fechaDesde", parametros.get("fechaDesde"));
        params.put("fechaHasta", parametros.get("fechaHasta"));
        params.put("usuario", parametros.get("usuario"));
        params.put("nombreCliente", parametros.get("nombreCliente"));
        params.put("documentoCliente", parametros.get("documentoCliente"));
        params.put("tipoDocumento", parametros.get("tipoDocumento"));
        params.put("tipoProducto", parametros.get("tipoProducto"));
        params.put("presentacion", parametros.get("presentacion"));
        params.put("marca", parametros.get("marca"));


        String classpath = "classpath:reportes/" + nameReport + ".jrxml";

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(classpath)
                        .getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}

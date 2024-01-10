package com.bcs.ventas.service.jasper.impl;

import com.bcs.ventas.service.jasper.CajaIngresoVentasJasperService;
import com.bcs.ventas.utils.reportbeans.CajaIngresoVentasReport;
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
public class CajaIngresoVentasJasperServiceImpl extends ReportGeneratorServiceImpl<CajaIngresoVentasReport> implements CajaIngresoVentasJasperService {

    public byte[] exportToPdf(List<CajaIngresoVentasReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(list, nameData, nameReport, parametros));
    }

    public byte[] exportToXls(List<CajaIngresoVentasReport> list, String nameData, String nameReport, String sheet, Map<String, Object> parametros) throws JRException, FileNotFoundException {
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


    private JasperPrint getReport(List<CajaIngresoVentasReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws FileNotFoundException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(nameData, new JRBeanCollectionDataSource(list));
        params.put("sucursal", parametros.get("sucursal"));
        params.put("fechaHasta", parametros.get("fechaHasta"));
        params.put("fechaDesde", parametros.get("fechaDesde"));
        params.put("estado", parametros.get("estado"));

        params.put("nombreCliente", parametros.get("nombreCliente"));
        params.put("documentoCliente", parametros.get("documentoCliente"));
        params.put("comprobante", parametros.get("comprobante"));
        params.put("tipoVenta", parametros.get("tipoVenta"));

        params.put("tipoDocumento", parametros.get("tipoDocumento"));

        params.put("montoTotal", parametros.get("montoTotal"));

        String classpath = "classpath:reportes/" + nameReport + ".jrxml";

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(classpath)
                        .getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}

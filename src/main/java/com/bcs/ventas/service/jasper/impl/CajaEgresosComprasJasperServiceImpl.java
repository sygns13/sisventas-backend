package com.bcs.ventas.service.jasper.impl;

import com.bcs.ventas.service.jasper.CajaEgresosComprasJasperService;
import com.bcs.ventas.utils.reportbeans.CajaEgresosComprasReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CajaEgresosComprasJasperServiceImpl extends ReportGeneratorServiceImpl<CajaEgresosComprasReport> implements CajaEgresosComprasJasperService {

    public byte[] exportToPdf(List<CajaEgresosComprasReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws JRException, IOException {
        return JasperExportManager.exportReportToPdf(getReport(list, nameData, nameReport, parametros));
    }

    public byte[] exportToXls(List<CajaEgresosComprasReport> list, String nameData, String nameReport, String sheet, Map<String, Object> parametros) throws JRException, IOException {
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


    private JasperPrint getReport(List<CajaEgresosComprasReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws IOException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(nameData, new JRBeanCollectionDataSource(list));
        params.put("sucursal", parametros.get("sucursal"));
        params.put("fechaHasta", parametros.get("fechaHasta"));
        params.put("fechaDesde", parametros.get("fechaDesde"));
        params.put("estado", parametros.get("estado"));
        params.put("nombreProveedor", parametros.get("nombreProveedor"));
        params.put("documentoProveedor", parametros.get("documentoProveedor"));
        params.put("tipoDocumento", parametros.get("tipoDocumento"));

        params.put("montoTotal", parametros.get("montoTotal"));

        /*
        String classpath = "classpath:reportes/" + nameReport + ".jrxml";

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(classpath)
                        .getAbsolutePath()), params, new JREmptyDataSource());

        */
        // 1. Definimos la ruta relativa dentro del classpath (sin "classpath:")
        String path = "reportes/" + nameReport + ".jrxml";

        // 2. Usamos ClassPathResource de Spring para leer dentro del JAR
        InputStream inputStream = new ClassPathResource(path).getInputStream();

        // 3. Pasamos el inputStream en lugar del File absolute path
        JasperPrint report = JasperFillManager.fillReport(
                JasperCompileManager.compileReport(inputStream), // Jasper compila directo del stream
                params,
                new JREmptyDataSource()
        );

        return report;
    }
}

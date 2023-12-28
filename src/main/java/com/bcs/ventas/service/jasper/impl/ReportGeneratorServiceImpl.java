package com.bcs.ventas.service.jasper.impl;

import com.bcs.ventas.model.Pet;
import com.bcs.ventas.service.jasper.ReportGeneratorService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class ReportGeneratorServiceImpl<T> implements ReportGeneratorService<T> {
    @Override
    public byte[] exportToPdf(List<T> list, String nameData, String nameReport) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(list, nameData, nameReport));
    }

    @Override
    public byte[] exportToXls(List<T> list,String nameData, String nameReport, String sheet) throws JRException, FileNotFoundException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(byteArray);
        JRXlsxExporter exporter = new JRXlsxExporter();

        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[] { sheet });
        exporter.setConfiguration(reportConfigXLS);

        exporter.setExporterInput(new SimpleExporterInput(getReport(list, nameData, nameReport)));
        exporter.setExporterOutput(output);

        exporter.exportReport();
        output.close();
        return byteArray.toByteArray();
    }

    private JasperPrint getReport(List<T> list, String nameData, String nameReport) throws FileNotFoundException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(nameData, new JRBeanCollectionDataSource(list));
        String classpath = "classpath:reportes/" + nameReport + ".jrxml";

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(classpath)
                        .getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}

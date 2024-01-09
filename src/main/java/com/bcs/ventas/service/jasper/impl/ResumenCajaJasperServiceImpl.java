package com.bcs.ventas.service.jasper.impl;

import com.bcs.ventas.service.jasper.ResumenCajaJasperService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumenCajaJasperServiceImpl implements ResumenCajaJasperService {
    public byte[] exportToPdf(String nameData, String nameReport, Map<String, Object> parametros) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(nameData, nameReport, parametros));
    }

    public byte[] exportToXls(String nameData, String nameReport, String sheet, Map<String, Object> parametros) throws JRException, FileNotFoundException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(byteArray);
        JRXlsxExporter exporter = new JRXlsxExporter();

        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[] { sheet });
        exporter.setConfiguration(reportConfigXLS);

        exporter.setExporterInput(new SimpleExporterInput(getReport(nameData, nameReport, parametros)));
        exporter.setExporterOutput(output);

        exporter.exportReport();
        output.close();
        return byteArray.toByteArray();
    }


    private JasperPrint getReport(String nameData, String nameReport, Map<String, Object> parametros) throws FileNotFoundException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        //params.put(nameData, new JRBeanCollectionDataSource(list));
        params.put("titulo", parametros.get("titulo"));
        params.put("cajaInicial", parametros.get("cajaInicial"));
        params.put("cajaTotal", parametros.get("cajaTotal"));
        params.put("ingresosVentas", parametros.get("ingresosVentas"));
        params.put("ingresosOtros", parametros.get("ingresosOtros"));
        params.put("egresosCompras", parametros.get("egresosCompras"));
        params.put("egresosOtros", parametros.get("egresosOtros"));
        params.put("ingresosTotal", parametros.get("ingresosTotal"));
        params.put("egresosTotal", parametros.get("egresosTotal"));
        params.put("sucursal", parametros.get("sucursal"));


        String classpath = "classpath:reportes/" + nameReport + ".jrxml";

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(classpath)
                        .getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}

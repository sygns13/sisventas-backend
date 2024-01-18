package com.bcs.ventas.service.jasper.impl;

import com.bcs.ventas.service.jasper.NotaVentaJasperService;
import com.bcs.ventas.utils.comprobantesbeans.NotaVentaDetailReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotaVentaJasperServiceImpl implements NotaVentaJasperService {
    @Override
    public byte[] exportToPdf(List<NotaVentaDetailReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(list, nameData, nameReport, parametros));
    }

    private JasperPrint getReport(List<NotaVentaDetailReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws FileNotFoundException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(nameData, new JRBeanCollectionDataSource(list));
        params.put("montoLetra", parametros.get("montoLetra"));

        params.put("empr_razonsocial", parametros.get("empr_razonsocial"));
        params.put("empr_nombrecomercial", parametros.get("empr_nombrecomercial"));
        params.put("empr_direccion", parametros.get("empr_direccion"));
        params.put("empr_distrito", parametros.get("empr_distrito"));
        params.put("empr_nroruc", parametros.get("empr_nroruc"));
        params.put("docu_numero", parametros.get("docu_numero"));
        params.put("docu_fecha", parametros.get("docu_fecha"));
        params.put("clie_tipo_doc", parametros.get("clie_tipo_doc"));
        params.put("clie_numero", parametros.get("clie_numero"));
        params.put("clie_nombre", parametros.get("clie_nombre"));
        params.put("docu_grabada", parametros.get("docu_grabada"));
        params.put("docu_inafecta", parametros.get("docu_inafecta"));
        params.put("docu_exonerada", parametros.get("docu_exonerada"));
        params.put("docu_descuento", parametros.get("docu_descuento"));
        params.put("docu_total", parametros.get("docu_total"));
        params.put("docu_igv", parametros.get("docu_igv"));
        params.put("docu_isc", parametros.get("docu_isc"));
        params.put("docu_otrostributos", parametros.get("docu_otrostributos"));
        params.put("docu_otroscargos", parametros.get("docu_otroscargos"));
        params.put("hashcode", parametros.get("hashcode"));
        params.put("urlConsulta", parametros.get("urlConsulta"));


        String classpath = "classpath:comprobantes/" + nameReport + ".jrxml";

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(classpath)
                        .getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}

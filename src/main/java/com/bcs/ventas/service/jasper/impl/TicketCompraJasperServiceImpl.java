package com.bcs.ventas.service.jasper.impl;

import com.bcs.ventas.service.jasper.TicketCompraJasperService;
import com.bcs.ventas.utils.comprobantesbeans.TicketCompraDetailReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketCompraJasperServiceImpl implements TicketCompraJasperService {
    @Override
    public byte[] exportToPdf(List<TicketCompraDetailReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(list, nameData, nameReport, parametros));
    }

    private JasperPrint getReport(List<TicketCompraDetailReport> list, String nameData, String nameReport, Map<String, Object> parametros) throws FileNotFoundException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(nameData, new JRBeanCollectionDataSource(list));

        params.put("empr_razonsocial", parametros.get("empr_razonsocial"));
        params.put("empr_nombrecomercial", parametros.get("empr_nombrecomercial"));;
        params.put("empr_nroruc", parametros.get("empr_nroruc"));
        params.put("docu_compra_numero", parametros.get("docu_compra_numero"));
        params.put("tipo_comprobante", parametros.get("tipo_comprobante"));
        params.put("docu_numero", parametros.get("docu_numero"));
        params.put("docu_fecha", parametros.get("docu_fecha"));
        params.put("empr_sucursal", parametros.get("empr_sucursal"));
        params.put("pro_tipo_doc", parametros.get("pro_tipo_doc"));
        params.put("pro_numero", parametros.get("pro_numero"));
        params.put("pro_nombre", parametros.get("pro_nombre"));
        params.put("docu_total", parametros.get("docu_total"));
        params.put("montoLetra", parametros.get("montoLetra"));


        String classpath = "classpath:comprobantes/" + nameReport + ".jrxml";

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(classpath)
                        .getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}

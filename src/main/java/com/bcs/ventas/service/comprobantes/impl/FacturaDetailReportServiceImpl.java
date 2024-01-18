package com.bcs.ventas.service.comprobantes.impl;

import com.bcs.ventas.dao.CabeceraComprobanteDAO;
import com.bcs.ventas.dao.ConfigDAO;
import com.bcs.ventas.dao.DetalleComprobanteDAO;
import com.bcs.ventas.dao.mappers.CabeceraComprobanteMapper;
import com.bcs.ventas.dao.mappers.ConfigMapper;
import com.bcs.ventas.dao.mappers.DetalleComprobanteMapper;
import com.bcs.ventas.model.entities.CabeceraComprobante;
import com.bcs.ventas.model.entities.Config;
import com.bcs.ventas.model.entities.DetalleComprobante;
import com.bcs.ventas.service.comprobantes.FacturaDetailReportService;
import com.bcs.ventas.service.jasper.FacturaJasperService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.comprobantesbeans.FacturaDetailReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacturaDetailReportServiceImpl implements FacturaDetailReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private FacturaJasperService facturaJasperService;

    @Autowired
    private CabeceraComprobanteDAO cabeceraComprobanteDAO;

    @Autowired
    private CabeceraComprobanteMapper cabeceraComprobanteMapper;

    @Autowired
    private DetalleComprobanteDAO detalleComprobanteDAO;

    @Autowired
    private DetalleComprobanteMapper detalleComprobanteMapper;

    @Autowired
    private ConfigDAO configDAO;

    @Autowired
    private ConfigMapper configMapper;

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public byte[] exportPdf(Long idVenta) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> parametros = new HashMap<>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID","vista_previa_comp");
        params.put("EMPRESA_ID",EmpresaId);

        List<Config> configs = configMapper.listByParameterMap(params);
        Config config_igv = configs.get(0);

        params.clear();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("VENTA_ID",idVenta);

        List<CabeceraComprobante> cabeceraComprobantes = cabeceraComprobanteMapper.listByParameterMap(params);
        CabeceraComprobante cabeceraComprobante = cabeceraComprobantes.get(0);

        parametros.put("empr_razonsocial", cabeceraComprobante.getEmpRazonsocial());
        parametros.put("empr_nombrecomercial", cabeceraComprobante.getEmpNombrecomercial());
        parametros.put("empr_direccion", cabeceraComprobante.getEmpDireccion());
        parametros.put("empr_distrito", cabeceraComprobante.getEmpDistrito());
        parametros.put("empr_nroruc", cabeceraComprobante.getEmpNroruc());
        parametros.put("docu_numero", cabeceraComprobante.getDocNumero());
        parametros.put("docu_fecha", cabeceraComprobante.getDocFecha().format(formato));
        parametros.put("clie_numero", cabeceraComprobante.getCliNumero());
        parametros.put("clie_nombre", cabeceraComprobante.getCliNombre());
        parametros.put("docu_grabada", cabeceraComprobante.getDocGravada());
        parametros.put("docu_inafecta", cabeceraComprobante.getDocInafecta());
        parametros.put("docu_exonerada", cabeceraComprobante.getDocExonerada());
        parametros.put("docu_descuento", cabeceraComprobante.getDocDescuento());
        parametros.put("docu_total", cabeceraComprobante.getDocTotal());
        parametros.put("docu_igv", cabeceraComprobante.getDocIgv());
        parametros.put("docu_isc", cabeceraComprobante.getDocIsc());
        parametros.put("docu_otrostributos", cabeceraComprobante.getDocOtrostributos());
        parametros.put("docu_otroscargos", cabeceraComprobante.getDocOtroscargos());
        parametros.put("hashcode", cabeceraComprobante.getHashcode());
        parametros.put("urlConsulta", config_igv.getValor());

        parametros.put("montoLetra", cabeceraComprobante.getImporteLetras());

        params.clear();
        params.put("CABECERA_ID", cabeceraComprobante.getId());
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<DetalleComprobante> detalleComprobantes = detalleComprobanteMapper.listByParameterMap(params);

        List<FacturaDetailReport> facturaDetailReports = new ArrayList<>();

        detalleComprobantes.forEach((detalleComprobante) -> {
            FacturaDetailReport facturaDetailReport = new FacturaDetailReport();

            BigDecimal subTotal = new BigDecimal(detalleComprobante.getItemToSubtotal());
            BigDecimal igv = new BigDecimal(detalleComprobante.getItemToIgv());
            BigDecimal total = subTotal.add(igv);

            facturaDetailReport.setCantidad(detalleComprobante.getItemCantidad());
            facturaDetailReport.setProducto(detalleComprobante.getItemDescripcion());
            facturaDetailReport.setPrecioUnitario(detalleComprobante.getItemPreunitfin());
            facturaDetailReport.setPrecioTotal(String.format("%.2f", total));

            facturaDetailReports.add(facturaDetailReport);
        });


        return facturaJasperService.exportToPdf(facturaDetailReports,"data","ticketFacturaElectronica", parametros);
    }
}

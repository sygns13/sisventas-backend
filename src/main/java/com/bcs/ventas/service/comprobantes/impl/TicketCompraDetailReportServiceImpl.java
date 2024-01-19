package com.bcs.ventas.service.comprobantes.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.*;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.comprobantes.NumberFormatToWordsService;
import com.bcs.ventas.service.comprobantes.TicketCompraDetailReportService;
import com.bcs.ventas.service.jasper.TicketCompraJasperService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.comprobantesbeans.TicketCompraDetailReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketCompraDetailReportServiceImpl implements TicketCompraDetailReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private TicketCompraJasperService ticketCompraJasperService;

    @Autowired
    private CabeceraComprobanteDAO cabeceraComprobanteDAO;

    @Autowired
    private EntradaStockMapper entradaStockMapper;

    @Autowired
    private EmpresaMapper empresaMapper;

    @Autowired
    private DetalleComprobanteDAO detalleComprobanteDAO;

    @Autowired
    private DetalleEntradaStockMapper detalleEntradaStockMapper;

    @Autowired
    private ConfigDAO configDAO;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private TipoComprobanteDAO tipoComprobanteDAO;
    @Autowired
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Autowired
    private NumberFormatToWordsService numberFormatToWordsService;

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public byte[] exportPdf(Long idCompra) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> parametros = new HashMap<>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", EmpresaId);

        List<Empresa> empresas = empresaMapper.listByParameterMap(params);

        Empresa empresa = empresas.get(0);

        params.clear();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("ID",idCompra);

        List<EntradaStock> entradaStocks = entradaStockMapper.listByParameterMap(params);
        EntradaStock entradaStock = entradaStocks.get(0);

        parametros.put("empr_razonsocial", empresa.getRazonsocial().toUpperCase());
        parametros.put("empr_nombrecomercial", empresa.getNombreComercial().toUpperCase());
        parametros.put("empr_nroruc", empresa.getRuc());
        parametros.put("docu_compra_numero", entradaStock.getNumero());

        if(entradaStock.getFacturaProveedor() != null){
            TipoComprobante tipoComprobante  = tipoComprobanteDAO.listarPorId(entradaStock.getFacturaProveedor().getTipoComprobante().getId());
            parametros.put("tipo_comprobante", tipoComprobante.getNombre().toUpperCase());
            parametros.put("docu_numero", entradaStock.getFacturaProveedor().getSerie() + "-" + entradaStock.getFacturaProveedor().getNumero());
        }

        parametros.put("docu_fecha", entradaStock.getFecha().format(formato));
        parametros.put("empr_sucursal", entradaStock.getAlmacen().getNombre().toUpperCase());

        if(entradaStock.getProveedor() != null){
            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStock.getProveedor().getTipoDocumento().getId());
            parametros.put("pro_tipo_doc", tipoDocumento.getTipo().toUpperCase() + ":");
            parametros.put("pro_numero", entradaStock.getProveedor().getDocumento());
            parametros.put("pro_nombre", entradaStock.getProveedor().getNombre().toUpperCase());
        }

        parametros.put("docu_total", String.format("%.2f",entradaStock.getTotalMonto()));

        String strImporteLetra = String.format("%.2f",entradaStock.getTotalMonto());
        String INI_SUFIX = "/100 ";
        String strCurrencyCodeWords = "SOLES";
        String CONECTOR = "Y";
        String strSufix = INI_SUFIX + strCurrencyCodeWords;
        strImporteLetra = numberFormatToWordsService.Convert(strImporteLetra, "", "", strSufix, strSufix, CONECTOR, true);

        parametros.put("montoLetra", strImporteLetra.toUpperCase());




        params.clear();
        params.put("ENTRADA_STOCK_ID",entradaStocks.get(0).getId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<DetalleEntradaStock> detalleEntradaStocks = detalleEntradaStockMapper.listByParameterMap(params);

        List<TicketCompraDetailReport> ticketCompraDetailReports = new ArrayList<>();

        detalleEntradaStocks.forEach((detalleEntradaStock) -> {
            TicketCompraDetailReport ticketCompraDetailReport = new TicketCompraDetailReport();

            ticketCompraDetailReport.setCantidad(detalleEntradaStock.getCantidad().toString());
            ticketCompraDetailReport.setProducto(detalleEntradaStock.getUnidad().toUpperCase() + " " + detalleEntradaStock.getProducto().getPresentacion().getPresentacion().toUpperCase() + " " +detalleEntradaStock.getProducto().getNombre().toUpperCase() + " " + detalleEntradaStock.getProducto().getMarca().getNombre().toUpperCase());
            ticketCompraDetailReport.setPrecioUnitario(String.format("%.2f", detalleEntradaStock.getCosto()));
            ticketCompraDetailReport.setPrecioTotal(String.format("%.2f", detalleEntradaStock.getCostoTotal()));

            ticketCompraDetailReports.add(ticketCompraDetailReport);
        });


        return ticketCompraJasperService.exportToPdf(ticketCompraDetailReports,"data","ticketEntradaStock", parametros);
    }
}

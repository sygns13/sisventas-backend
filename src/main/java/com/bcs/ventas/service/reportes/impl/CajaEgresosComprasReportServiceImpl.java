package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.EntradaStockMapper;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.CajaEgresosComprasJasperService;
import com.bcs.ventas.service.reportes.CajaEgresosComprasReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroEntradaStock;
import com.bcs.ventas.utils.reportbeans.CajaEgresosComprasReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CajaEgresosComprasReportServiceImpl implements CajaEgresosComprasReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private CajaEgresosComprasJasperService cajaEgresosComprasJasperService;

    @Autowired
    private UnidadMapper unidadMapper;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private AlmacenDAO almacenDAO;

    @Autowired
    private TipoProductoDAO tipoProductoDAO;

    @Autowired
    private MarcaDAO marcaDAO;

    @Autowired
    private PresentacionDAO presentacionDAO;

    @Autowired
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Autowired
    private EntradaStockMapper entradaStockMapper;

    @Autowired
    private TipoComprobanteDAO tipoComprobanteDAO;

    @Autowired
    private ProveedorDAO proveedorDAO;

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public byte[] exportPdf(FiltroEntradaStock filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> parametros = new HashMap<>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);
        params.put("ESTADO_NO_ANULADO", Constantes.VENTA_ESTADO_ANULADO);

        String almacen = "Todas las sucursales";
        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("ALMACEN_ID", filtros.getAlmacenId());
            Almacen almacen1 = almacenDAO.listarPorId(filtros.getAlmacenId());
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }
        parametros.put("sucursal", almacen);


        if(filtros.getNumeroEntradaStock() != null && !filtros.getNumeroEntradaStock().trim().isEmpty())
            params.put("NUMERO", filtros.getNumeroEntradaStock());

        if(filtros.getId() != null && filtros.getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ID",filtros.getId());

        if(filtros.getFecha() != null)
            params.put("FECHA", filtros.getFecha());

        String fechaDesde = "Todas";
        String fechaHasta = "Todas";
        if(filtros.getFechaInicio() != null && filtros.getFechaFinal() != null){
            params.put("FECHA_INI", filtros.getFechaInicio());
            params.put("FECHA_FIN", filtros.getFechaFinal());
            fechaDesde = filtros.getFechaInicio().format(formato);
            fechaHasta = filtros.getFechaFinal().format(formato);
        }
        parametros.put("fechaDesde", fechaDesde);
        parametros.put("fechaHasta", fechaHasta);

        String estado = "Todos";
        if(filtros.getEstado() != null) {
            params.put("ESTADO", filtros.getEstado());

            switch (filtros.getEstado()) {
                case 0:
                    estado = Constantes.COMPRA_ESTADO_ANULADO_STR;
                    break;
                case 1:
                    estado = Constantes.COMPRA_ESTADO_INICIADO_STR;
                    break;
                case 2:
                    estado = Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA_STR;
                    break;
                case 3:
                    estado = Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL_STR;
                    break;
                case 4:
                    estado = Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL_STR;
                    break;
                default:
                    estado = "Sin estado";
                    break;
            }
        }
        parametros.put("estado", estado);


        if(filtros.getActualizado() != null) {
            params.put("ACTUALIZADO", filtros.getActualizado());
        }

        if(filtros.getFacturado() != null) {
            params.put("FACTURADO", filtros.getFacturado());
        }

        String nombreProveedor = "Todos";
        String documentoProveedor = "";
        String tipoDocumentoProveedor = "";
        if(filtros.getIdProveedor() != null && filtros.getIdProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("PROVEEDOR_ID", filtros.getIdProveedor());
            Proveedor proveedor = proveedorDAO.listarPorId(filtros.getIdProveedor());

            nombreProveedor = proveedor != null ? proveedor.getNombre() : "Proveedor no encontrado";
            documentoProveedor = proveedor != null ? proveedor.getDocumento() : "Documento no encontrado";

            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(proveedor != null ? proveedor.getTipoDocumento().getId(): 0);
            tipoDocumentoProveedor = tipoDocumento != null ? tipoDocumento.getTipo() : "Tipo de documento no encontrado";

        }
        parametros.put("nombreProveedor", nombreProveedor);
        parametros.put("documentoProveedor", documentoProveedor);
        parametros.put("tipoDocumento", tipoDocumentoProveedor);


        if(filtros.getNombreProveedor() != null && !filtros.getNombreProveedor().trim().isEmpty()) {
            params.put("PRO_NOMBRE", "%" + filtros.getNombreProveedor() + "%");
        }



        if(filtros.getDocumentoProveedor() != null && !filtros.getDocumentoProveedor().trim().isEmpty()) {
            params.put("PRO_DOCUMENTO", filtros.getDocumentoProveedor());
        }



        if(filtros.getIdTipoDocumentoProveedor() != null && filtros.getIdTipoDocumentoProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("PRO_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoProveedor());
        }



        if(filtros.getIdFacturaProveedor() != null && filtros.getIdFacturaProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("FACTURA_PROVEEDOR_ID", filtros.getIdFacturaProveedor());

        if(filtros.getSerieFacturaProveedor() != null && !filtros.getSerieFacturaProveedor().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieFacturaProveedor());

        if(filtros.getNumeroFacturaProveedor() != null && !filtros.getNumeroFacturaProveedor().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroFacturaProveedor());

        if(filtros.getIdTipoComprobante() != null && filtros.getIdTipoComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CO_TIPO_COMPROBANTE_ID", filtros.getIdTipoComprobante());

        if(filtros.getIdUser() != null && filtros.getIdUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("USER_ID", filtros.getIdUser());

        if(filtros.getNameUser() != null && !filtros.getNameUser().trim().isEmpty()) {
            params.put("U_NAME", filtros.getNameUser());
        }

        if(filtros.getEmailUser() != null && !filtros.getEmailUser().trim().isEmpty())
            params.put("U_EMAIL", filtros.getEmailUser());

        if(filtros.getIdTipoUser() != null && filtros.getIdTipoUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("U_TIPO_USER_ID", filtros.getIdTipoUser());

        if(filtros.getBuscarDatosUser() != null && !filtros.getBuscarDatosUser().trim().isEmpty())
            params.put("U_BUSCAR", filtros.getBuscarDatosUser());

        //Buscar General
        if(filtros.getBuscarDatos() != null && !filtros.getBuscarDatos().trim().isEmpty())
            params.put("BUSCAR_GENERAL", "%"+filtros.getBuscarDatos()+"%");


        List<EntradaStock> entradaStocks = entradaStockMapper.listByParameterMap(params);

        List<CajaEgresosComprasReport> cajaEgresosComprasReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        AtomicReference<BigDecimal> montoTotal = new AtomicReference<>(new BigDecimal(0));

        entradaStocks.forEach((entradaStock) -> {
            CajaEgresosComprasReport cajaEgresosComprasReportObj = new CajaEgresosComprasReport();

            cajaEgresosComprasReportObj.setNro(nro.get());
            cajaEgresosComprasReportObj.setSucursal(entradaStock.getAlmacen().getNombre());
            cajaEgresosComprasReportObj.setNumeroCompra(entradaStock.getNumero());
            cajaEgresosComprasReportObj.setFecha(entradaStock.getFecha().format(formato));
            cajaEgresosComprasReportObj.setHora(entradaStock.getHora().toString());

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_ANULADO))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_ANULADO_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_INICIADO))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_INICIADO_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL_STR);

            cajaEgresosComprasReportObj.setEstado(entradaStock.getEstadoStr());


            if(entradaStock.getProveedor() != null && entradaStock.getProveedor().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStock.getProveedor().getTipoDocumento().getId());
                    entradaStock.getProveedor().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cajaEgresosComprasReportObj.setProveedor(entradaStock.getProveedor() != null ? entradaStock.getProveedor().getNombre() + " " + entradaStock.getProveedor().getTipoDocumento().getTipo() + " " + entradaStock.getProveedor().getDocumento() : "");
            if(entradaStock.getFacturaProveedor() != null && entradaStock.getFacturaProveedor().getTipoComprobante() != null){
                try {
                    TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(entradaStock.getFacturaProveedor().getTipoComprobante().getId());
                    entradaStock.getFacturaProveedor().setTipoComprobante(tipoComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cajaEgresosComprasReportObj.setComprobante(entradaStock.getFacturaProveedor() != null ? entradaStock.getFacturaProveedor().getTipoComprobante().getNombre() + " " + entradaStock.getFacturaProveedor().getSerie() + "-" + entradaStock.getFacturaProveedor().getNumero() : "");

            cajaEgresosComprasReportObj.setImportePagado(entradaStock.getMontoPagado());

            montoTotal.set(montoTotal.get().add(entradaStock.getMontoPagado()));
            cajaEgresosComprasReport.add(cajaEgresosComprasReportObj);

            nro.set(nro.get() + 1L);
        });

        BigDecimal montoTotalFinal = montoTotal.get();
        parametros.put("montoTotal", montoTotalFinal);


        return cajaEgresosComprasJasperService.exportToPdf(cajaEgresosComprasReport,"data","reportCajaEgresosCompras", parametros);
    }

    @Override
    public byte[] exportXls(FiltroEntradaStock filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> parametros = new HashMap<>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);
        params.put("ESTADO_NO_ANULADO", Constantes.VENTA_ESTADO_ANULADO);

        String almacen = "Todas las sucursales";
        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("ALMACEN_ID", filtros.getAlmacenId());
            Almacen almacen1 = almacenDAO.listarPorId(filtros.getAlmacenId());
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }
        parametros.put("sucursal", almacen);


        if(filtros.getNumeroEntradaStock() != null && !filtros.getNumeroEntradaStock().trim().isEmpty())
            params.put("NUMERO", filtros.getNumeroEntradaStock());

        if(filtros.getId() != null && filtros.getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ID",filtros.getId());

        if(filtros.getFecha() != null)
            params.put("FECHA", filtros.getFecha());

        String fechaDesde = "Todas";
        String fechaHasta = "Todas";
        if(filtros.getFechaInicio() != null && filtros.getFechaFinal() != null){
            params.put("FECHA_INI", filtros.getFechaInicio());
            params.put("FECHA_FIN", filtros.getFechaFinal());
            fechaDesde = filtros.getFechaInicio().format(formato);
            fechaHasta = filtros.getFechaFinal().format(formato);
        }
        parametros.put("fechaDesde", fechaDesde);
        parametros.put("fechaHasta", fechaHasta);

        String estado = "Todos";
        if(filtros.getEstado() != null) {
            params.put("ESTADO", filtros.getEstado());

            switch (filtros.getEstado()) {
                case 0:
                    estado = Constantes.COMPRA_ESTADO_ANULADO_STR;
                    break;
                case 1:
                    estado = Constantes.COMPRA_ESTADO_INICIADO_STR;
                    break;
                case 2:
                    estado = Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA_STR;
                    break;
                case 3:
                    estado = Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL_STR;
                    break;
                case 4:
                    estado = Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL_STR;
                    break;
                default:
                    estado = "Sin estado";
                    break;
            }
        }
        parametros.put("estado", estado);


        if(filtros.getActualizado() != null) {
            params.put("ACTUALIZADO", filtros.getActualizado());
        }

        if(filtros.getFacturado() != null) {
            params.put("FACTURADO", filtros.getFacturado());
        }

        String nombreProveedor = "Todos";
        String documentoProveedor = "";
        String tipoDocumentoProveedor = "";
        if(filtros.getIdProveedor() != null && filtros.getIdProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("PROVEEDOR_ID", filtros.getIdProveedor());
            Proveedor proveedor = proveedorDAO.listarPorId(filtros.getIdProveedor());

            nombreProveedor = proveedor != null ? proveedor.getNombre() : "Proveedor no encontrado";
            documentoProveedor = proveedor != null ? proveedor.getDocumento() : "Documento no encontrado";

            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(proveedor != null ? proveedor.getTipoDocumento().getId(): 0);
            tipoDocumentoProveedor = tipoDocumento != null ? tipoDocumento.getTipo() : "Tipo de documento no encontrado";

        }
        parametros.put("nombreProveedor", nombreProveedor);
        parametros.put("documentoProveedor", documentoProveedor);
        parametros.put("tipoDocumento", tipoDocumentoProveedor);


        if(filtros.getNombreProveedor() != null && !filtros.getNombreProveedor().trim().isEmpty()) {
            params.put("PRO_NOMBRE", "%" + filtros.getNombreProveedor() + "%");
        }



        if(filtros.getDocumentoProveedor() != null && !filtros.getDocumentoProveedor().trim().isEmpty()) {
            params.put("PRO_DOCUMENTO", filtros.getDocumentoProveedor());
        }



        if(filtros.getIdTipoDocumentoProveedor() != null && filtros.getIdTipoDocumentoProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("PRO_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoProveedor());
        }



        if(filtros.getIdFacturaProveedor() != null && filtros.getIdFacturaProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("FACTURA_PROVEEDOR_ID", filtros.getIdFacturaProveedor());

        if(filtros.getSerieFacturaProveedor() != null && !filtros.getSerieFacturaProveedor().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieFacturaProveedor());

        if(filtros.getNumeroFacturaProveedor() != null && !filtros.getNumeroFacturaProveedor().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroFacturaProveedor());

        if(filtros.getIdTipoComprobante() != null && filtros.getIdTipoComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CO_TIPO_COMPROBANTE_ID", filtros.getIdTipoComprobante());

        if(filtros.getIdUser() != null && filtros.getIdUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("USER_ID", filtros.getIdUser());

        if(filtros.getNameUser() != null && !filtros.getNameUser().trim().isEmpty()) {
            params.put("U_NAME", filtros.getNameUser());
        }

        if(filtros.getEmailUser() != null && !filtros.getEmailUser().trim().isEmpty())
            params.put("U_EMAIL", filtros.getEmailUser());

        if(filtros.getIdTipoUser() != null && filtros.getIdTipoUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("U_TIPO_USER_ID", filtros.getIdTipoUser());

        if(filtros.getBuscarDatosUser() != null && !filtros.getBuscarDatosUser().trim().isEmpty())
            params.put("U_BUSCAR", filtros.getBuscarDatosUser());

        //Buscar General
        if(filtros.getBuscarDatos() != null && !filtros.getBuscarDatos().trim().isEmpty())
            params.put("BUSCAR_GENERAL", "%"+filtros.getBuscarDatos()+"%");


        List<EntradaStock> entradaStocks = entradaStockMapper.listByParameterMap(params);

        List<CajaEgresosComprasReport> cajaEgresosComprasReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        AtomicReference<BigDecimal> montoTotal = new AtomicReference<>(new BigDecimal(0));

        entradaStocks.forEach((entradaStock) -> {
            CajaEgresosComprasReport cajaEgresosComprasReportObj = new CajaEgresosComprasReport();

            cajaEgresosComprasReportObj.setNro(nro.get());
            cajaEgresosComprasReportObj.setSucursal(entradaStock.getAlmacen().getNombre());
            cajaEgresosComprasReportObj.setNumeroCompra(entradaStock.getNumero());
            cajaEgresosComprasReportObj.setFecha(entradaStock.getFecha().format(formato));
            cajaEgresosComprasReportObj.setHora(entradaStock.getHora().toString());

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_ANULADO))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_ANULADO_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_INICIADO))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_INICIADO_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL_STR);

            cajaEgresosComprasReportObj.setEstado(entradaStock.getEstadoStr());


            if(entradaStock.getProveedor() != null && entradaStock.getProveedor().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStock.getProveedor().getTipoDocumento().getId());
                    entradaStock.getProveedor().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cajaEgresosComprasReportObj.setProveedor(entradaStock.getProveedor() != null ? entradaStock.getProveedor().getNombre() + " " + entradaStock.getProveedor().getTipoDocumento().getTipo() + " " + entradaStock.getProveedor().getDocumento() : "");
            if(entradaStock.getFacturaProveedor() != null && entradaStock.getFacturaProveedor().getTipoComprobante() != null){
                try {
                    TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(entradaStock.getFacturaProveedor().getTipoComprobante().getId());
                    entradaStock.getFacturaProveedor().setTipoComprobante(tipoComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cajaEgresosComprasReportObj.setComprobante(entradaStock.getFacturaProveedor() != null ? entradaStock.getFacturaProveedor().getTipoComprobante().getNombre() + " " + entradaStock.getFacturaProveedor().getSerie() + "-" + entradaStock.getFacturaProveedor().getNumero() : "");

            cajaEgresosComprasReportObj.setImportePagado(entradaStock.getMontoPagado());

            montoTotal.set(montoTotal.get().add(entradaStock.getMontoPagado()));
            cajaEgresosComprasReport.add(cajaEgresosComprasReportObj);

            nro.set(nro.get() + 1L);
        });

        BigDecimal montoTotalFinal = montoTotal.get();
        parametros.put("montoTotal", montoTotalFinal);


        return cajaEgresosComprasJasperService.exportToXls(cajaEgresosComprasReport,"data","reportCajaEgresosCompras", "hoja1", parametros);
    }
}

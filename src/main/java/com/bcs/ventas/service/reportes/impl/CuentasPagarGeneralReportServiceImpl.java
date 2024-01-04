package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.EntradaStockMapper;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.CuentasPagarGeneralJasperService;
import com.bcs.ventas.service.reportes.CuentasPagarGeneralReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroEntradaStock;
import com.bcs.ventas.utils.reportbeans.CuentasPagarGeneralReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CuentasPagarGeneralReportServiceImpl implements CuentasPagarGeneralReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private CuentasPagarGeneralJasperService cuentasPagarGeneralJasperService;

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

        Map<String, String> parametros = new HashMap<String, String>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);

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
        if(filtros.getEstadoPago() != null){

            if(filtros.getEstadoPago().compareTo(Constantes.CANTIDAD_ZERO) == 0) {
                params.put("NO_ESTADO_PAGO", Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL);
                estado = "Por pagar";
            }

            if(filtros.getEstadoPago().compareTo(Constantes.CANTIDAD_UNIDAD_INTEGER) == 0) {
                params.put("ESTADO", Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL);
                estado = "Pagadas";
            }

        } else {
            if(filtros.getEstado() != null)
                params.put("ESTADO", filtros.getEstado());
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

        String usuario = "Todos";
        if(filtros.getNameUser() != null && !filtros.getNameUser().trim().isEmpty()) {
            params.put("U_NAME", filtros.getNameUser());
            usuario = filtros.getNameUser();
        }
        parametros.put("usuario", usuario);

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

        List<CuentasPagarGeneralReport> cuentasPagarGeneralReportList = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        entradaStocks.forEach((entradaStock) -> {
            CuentasPagarGeneralReport cuentasPagarGeneralReport = new CuentasPagarGeneralReport();

            cuentasPagarGeneralReport.setNro(nro.get());
            cuentasPagarGeneralReport.setSucursal(entradaStock.getAlmacen().getNombre());
            cuentasPagarGeneralReport.setNumeroCompra(entradaStock.getNumero());
            cuentasPagarGeneralReport.setFecha(entradaStock.getFecha().format(formato));
            cuentasPagarGeneralReport.setHora(entradaStock.getHora().toString());

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL))
                entradaStock.setEstadoStr("Pagada");
            else
                entradaStock.setEstadoStr("Por pagar");

            cuentasPagarGeneralReport.setEstado(entradaStock.getEstadoStr());
            cuentasPagarGeneralReport.setFacturada(entradaStock.getFacturado() == 1 ? "Si" : "No");
            cuentasPagarGeneralReport.setActualizada(entradaStock.getActualizado() == 1 ? "Si" : "No");

            if(entradaStock.getProveedor() != null && entradaStock.getProveedor().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStock.getProveedor().getTipoDocumento().getId());
                    entradaStock.getProveedor().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cuentasPagarGeneralReport.setProveedor(entradaStock.getProveedor() != null ? entradaStock.getProveedor().getNombre() + " " + entradaStock.getProveedor().getTipoDocumento().getTipo() + " " + entradaStock.getProveedor().getDocumento() : "");
            if(entradaStock.getFacturaProveedor() != null && entradaStock.getFacturaProveedor().getTipoComprobante() != null){
                try {
                    TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(entradaStock.getFacturaProveedor().getTipoComprobante().getId());
                    entradaStock.getFacturaProveedor().setTipoComprobante(tipoComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cuentasPagarGeneralReport.setComprobante(entradaStock.getFacturaProveedor() != null ? entradaStock.getFacturaProveedor().getTipoComprobante().getNombre() + " " + entradaStock.getFacturaProveedor().getSerie() + "-" + entradaStock.getFacturaProveedor().getNumero() : "");
            cuentasPagarGeneralReport.setUsuario(entradaStock.getUser() != null ? entradaStock.getUser().getName() : "");
            cuentasPagarGeneralReport.setImporteTotal(entradaStock.getImporteTotal());
            cuentasPagarGeneralReport.setImporteCobrado(entradaStock.getMontoPagado());
            cuentasPagarGeneralReport.setImportePendiente(entradaStock.getMontoPorPagar());

            cuentasPagarGeneralReportList.add(cuentasPagarGeneralReport);

            nro.set(nro.get() + 1L);
        });


        return cuentasPagarGeneralJasperService.exportToPdf(cuentasPagarGeneralReportList,"data","reportCuentasPagarGeneral", parametros);
    }

    @Override
    public byte[] exportXls(FiltroEntradaStock filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, String> parametros = new HashMap<String, String>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);

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
        if(filtros.getEstadoPago() != null){

            if(filtros.getEstadoPago().compareTo(Constantes.CANTIDAD_ZERO) == 0) {
                params.put("NO_ESTADO_PAGO", Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL);
                estado = "Por pagar";
            }

            if(filtros.getEstadoPago().compareTo(Constantes.CANTIDAD_UNIDAD_INTEGER) == 0) {
                params.put("ESTADO", Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL);
                estado = "Pagadas";
            }

        } else {
            if(filtros.getEstado() != null)
                params.put("ESTADO", filtros.getEstado());
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

        String usuario = "Todos";
        if(filtros.getNameUser() != null && !filtros.getNameUser().trim().isEmpty()) {
            params.put("U_NAME", filtros.getNameUser());
            usuario = filtros.getNameUser();
        }
        parametros.put("usuario", usuario);

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

        List<CuentasPagarGeneralReport> cuentasPagarGeneralReportList = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        entradaStocks.forEach((entradaStock) -> {
            CuentasPagarGeneralReport cuentasPagarGeneralReport = new CuentasPagarGeneralReport();

            cuentasPagarGeneralReport.setNro(nro.get());
            cuentasPagarGeneralReport.setSucursal(entradaStock.getAlmacen().getNombre());
            cuentasPagarGeneralReport.setNumeroCompra(entradaStock.getNumero());
            cuentasPagarGeneralReport.setFecha(entradaStock.getFecha().format(formato));
            cuentasPagarGeneralReport.setHora(entradaStock.getHora().toString());

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL))
                entradaStock.setEstadoStr("Pagada");
            else
                entradaStock.setEstadoStr("Por pagar");

            cuentasPagarGeneralReport.setEstado(entradaStock.getEstadoStr());
            cuentasPagarGeneralReport.setFacturada(entradaStock.getFacturado() == 1 ? "Si" : "No");
            cuentasPagarGeneralReport.setActualizada(entradaStock.getActualizado() == 1 ? "Si" : "No");

            if(entradaStock.getProveedor() != null && entradaStock.getProveedor().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStock.getProveedor().getTipoDocumento().getId());
                    entradaStock.getProveedor().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cuentasPagarGeneralReport.setProveedor(entradaStock.getProveedor() != null ? entradaStock.getProveedor().getNombre() + " " + entradaStock.getProveedor().getTipoDocumento().getTipo() + " " + entradaStock.getProveedor().getDocumento() : "");
            if(entradaStock.getFacturaProveedor() != null && entradaStock.getFacturaProveedor().getTipoComprobante() != null){
                try {
                    TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(entradaStock.getFacturaProveedor().getTipoComprobante().getId());
                    entradaStock.getFacturaProveedor().setTipoComprobante(tipoComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cuentasPagarGeneralReport.setComprobante(entradaStock.getFacturaProveedor() != null ? entradaStock.getFacturaProveedor().getTipoComprobante().getNombre() + " " + entradaStock.getFacturaProveedor().getSerie() + "-" + entradaStock.getFacturaProveedor().getNumero() : "");
            cuentasPagarGeneralReport.setUsuario(entradaStock.getUser() != null ? entradaStock.getUser().getName() : "");
            cuentasPagarGeneralReport.setImporteTotal(entradaStock.getImporteTotal());
            cuentasPagarGeneralReport.setImporteCobrado(entradaStock.getMontoPagado());
            cuentasPagarGeneralReport.setImportePendiente(entradaStock.getMontoPorPagar());

            cuentasPagarGeneralReportList.add(cuentasPagarGeneralReport);

            nro.set(nro.get() + 1L);
        });

        return cuentasPagarGeneralJasperService.exportToXls(cuentasPagarGeneralReportList,"data","reportCuentasPagarGeneral", "hoja1", parametros);
    }
}

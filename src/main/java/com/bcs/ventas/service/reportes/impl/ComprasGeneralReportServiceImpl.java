package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.EntradaStockMapper;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.model.dto.InventarioDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.ComprasGeneralJasperService;
import com.bcs.ventas.service.reportes.ComprasGeneralReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroEntradaStock;
import com.bcs.ventas.utils.reportbeans.ComprasGeneralReport;
import com.bcs.ventas.utils.reportbeans.ProductosInventarioReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ComprasGeneralReportServiceImpl implements ComprasGeneralReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private ComprasGeneralJasperService comprasGeneralJasperService;

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

        String actualizado = "Todos";
        if(filtros.getActualizado() != null) {
            params.put("ACTUALIZADO", filtros.getActualizado());
            switch (filtros.getActualizado()) {
                case 0:
                    actualizado = "No";
                    break;
                case 1:
                    actualizado = "Si";
                    break;
                default:
                    actualizado = "Sin actualizar";
                    break;
            }
        }
        parametros.put("actualizada", actualizado);

        String facturado = "Todos";
        if(filtros.getFacturado() != null) {
            params.put("FACTURADO", filtros.getFacturado());
            switch (filtros.getFacturado()) {
                case 0:
                    facturado = "No";
                    break;
                case 1:
                    facturado = "Si";
                    break;
                default:
                    facturado = "Sin facturar";
                    break;
            }
        }
        parametros.put("facturada", facturado);

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

        List<ComprasGeneralReport> comprasGeneralReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        entradaStocks.forEach((entradaStock) -> {
            ComprasGeneralReport compraGeneralReport = new ComprasGeneralReport();

            compraGeneralReport.setNro(nro.get());
            compraGeneralReport.setSucursal(entradaStock.getAlmacen().getNombre());
            compraGeneralReport.setNumeroCompra(entradaStock.getNumero());
            compraGeneralReport.setFecha(entradaStock.getFecha().format(formato));
            compraGeneralReport.setHora(entradaStock.getHora().toString());

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

            compraGeneralReport.setEstado(entradaStock.getEstadoStr());
            compraGeneralReport.setFacturada(entradaStock.getFacturado() == 1 ? "Si" : "No");
            compraGeneralReport.setActualizada(entradaStock.getActualizado() == 1 ? "Si" : "No");

            if(entradaStock.getProveedor() != null && entradaStock.getProveedor().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStock.getProveedor().getTipoDocumento().getId());
                    entradaStock.getProveedor().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            compraGeneralReport.setProveedor(entradaStock.getProveedor() != null ? entradaStock.getProveedor().getNombre() + " " + entradaStock.getProveedor().getTipoDocumento().getTipo() + " " + entradaStock.getProveedor().getDocumento() : "");
            if(entradaStock.getFacturaProveedor() != null && entradaStock.getFacturaProveedor().getTipoComprobante() != null){
                try {
                    TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(entradaStock.getFacturaProveedor().getTipoComprobante().getId());
                    entradaStock.getFacturaProveedor().setTipoComprobante(tipoComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            compraGeneralReport.setComprobante(entradaStock.getFacturaProveedor() != null ? entradaStock.getFacturaProveedor().getTipoComprobante().getNombre() + " " + entradaStock.getFacturaProveedor().getSerie() + "-" + entradaStock.getFacturaProveedor().getNumero() : "");
            compraGeneralReport.setUsuario(entradaStock.getUser() != null ? entradaStock.getUser().getName() : "");
            compraGeneralReport.setMonto(entradaStock.getTotalMonto());

            comprasGeneralReport.add(compraGeneralReport);

            nro.set(nro.get() + 1L);
        });


        return comprasGeneralJasperService.exportToPdf(comprasGeneralReport,"data","reportComprasGenerales", parametros);
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

        String actualizado = "Todos";
        if(filtros.getActualizado() != null) {
            params.put("ACTUALIZADO", filtros.getActualizado());
            switch (filtros.getActualizado()) {
                case 0:
                    actualizado = "No";
                    break;
                case 1:
                    actualizado = "Si";
                    break;
                default:
                    actualizado = "Sin actualizar";
                    break;
            }
        }
        parametros.put("actualizada", actualizado);

        String facturado = "Todos";
        if(filtros.getFacturado() != null) {
            params.put("FACTURADO", filtros.getFacturado());
            switch (filtros.getFacturado()) {
                case 0:
                    facturado = "No";
                    break;
                case 1:
                    facturado = "Si";
                    break;
                default:
                    facturado = "Sin facturar";
                    break;
            }
        }
        parametros.put("facturada", facturado);

        if(filtros.getIdProveedor() != null && filtros.getIdProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("PROVEEDOR_ID", filtros.getIdProveedor());

        String nombreProveedor = "Todos";
        if(filtros.getNombreProveedor() != null && !filtros.getNombreProveedor().trim().isEmpty()) {
            params.put("PRO_NOMBRE", "%" + filtros.getNombreProveedor() + "%");
            nombreProveedor = filtros.getNombreProveedor();
        }
        parametros.put("nombreProveedor", nombreProveedor);

        String documentoProveedor = "";
        if(filtros.getDocumentoProveedor() != null && !filtros.getDocumentoProveedor().trim().isEmpty()) {
            params.put("PRO_DOCUMENTO", filtros.getDocumentoProveedor());
            documentoProveedor = filtros.getDocumentoProveedor();
        }
        parametros.put("documentoProveedor", documentoProveedor);

        String tipoDocumentoProveedor = "";
        if(filtros.getIdTipoDocumentoProveedor() != null && filtros.getIdTipoDocumentoProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("PRO_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoProveedor());
            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(filtros.getIdTipoDocumentoProveedor());
            tipoDocumentoProveedor = tipoDocumento != null ? tipoDocumento.getTipo() : "Tipo de documento no encontrado";
        }
        parametros.put("tipoDocumento", tipoDocumentoProveedor);


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

        List<ComprasGeneralReport> comprasGeneralReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        entradaStocks.forEach((entradaStock) -> {
            ComprasGeneralReport compraGeneralReport = new ComprasGeneralReport();

            compraGeneralReport.setNro(nro.get());
            compraGeneralReport.setSucursal(entradaStock.getAlmacen().getNombre());
            compraGeneralReport.setNumeroCompra(entradaStock.getNumero());
            compraGeneralReport.setFecha(entradaStock.getFecha().format(formato));
            compraGeneralReport.setHora(entradaStock.getHora().toString());

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

            compraGeneralReport.setEstado(entradaStock.getEstadoStr());
            compraGeneralReport.setFacturada(entradaStock.getFacturado() == 1 ? "Si" : "No");
            compraGeneralReport.setActualizada(entradaStock.getActualizado() == 1 ? "Si" : "No");

            if(entradaStock.getProveedor() != null && entradaStock.getProveedor().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStock.getProveedor().getTipoDocumento().getId());
                    entradaStock.getProveedor().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            compraGeneralReport.setProveedor(entradaStock.getProveedor() != null ? entradaStock.getProveedor().getNombre() + " " + entradaStock.getProveedor().getTipoDocumento().getTipo() + " " + entradaStock.getProveedor().getDocumento() : "");
            if(entradaStock.getFacturaProveedor() != null && entradaStock.getFacturaProveedor().getTipoComprobante() != null){
                try {
                    TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(entradaStock.getFacturaProveedor().getTipoComprobante().getId());
                    entradaStock.getFacturaProveedor().setTipoComprobante(tipoComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            compraGeneralReport.setComprobante(entradaStock.getFacturaProveedor() != null ? entradaStock.getFacturaProveedor().getTipoComprobante().getNombre() + " " + entradaStock.getFacturaProveedor().getSerie() + "-" + entradaStock.getFacturaProveedor().getNumero() : "");
            compraGeneralReport.setUsuario(entradaStock.getUser() != null ? entradaStock.getUser().getName() : "");
            compraGeneralReport.setMonto(entradaStock.getTotalMonto());

            comprasGeneralReport.add(compraGeneralReport);

            nro.set(nro.get() + 1L);
        });


        return comprasGeneralJasperService.exportToXls(comprasGeneralReport,"data","reportComprasGenerales", "hoja1", parametros);
    }
}

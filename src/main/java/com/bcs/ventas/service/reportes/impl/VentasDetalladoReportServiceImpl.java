package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.VentaMapper;
import com.bcs.ventas.model.dto.VentasDetallesDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.VentasDetalladoJasperService;
import com.bcs.ventas.service.reportes.VentasDetalladoReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroVenta;
import com.bcs.ventas.utils.reportbeans.VentasDetalladoReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class VentasDetalladoReportServiceImpl implements VentasDetalladoReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private VentaMapper ventaMapper;

    @Autowired
    private AlmacenDAO almacenDAO;

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Autowired
    private InitComprobanteDAO initComprobanteDAO;

    @Autowired
    private TipoComprobanteDAO tipoComprobanteDAO;

    @Autowired
    private VentasDetalladoJasperService ventasDetalladoJasperService;

    @Autowired
    private ProductoDAO productoDAO;

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public byte[] exportPdf(FiltroVenta filtros) throws Exception {

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

        if(filtros.getNumeroVenta() != null && !filtros.getNumeroVenta().trim().isEmpty())
            params.put("NUMERO_VENTA", filtros.getNumeroVenta());

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
        if(filtros.getEstadoVenta() != null) {
            params.put("ESTADO", filtros.getEstadoVenta());

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_ANULADO))
                estado = Constantes.VENTA_ESTADO_ANULADO_STR;

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_INICIADO))
                estado = Constantes.VENTA_ESTADO_INICIADO_STR;

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                estado = Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR;

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                estado = Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR;

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                estado = Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR;
        }
        parametros.put("estado", estado);

        if(filtros.getPagado() != null)
            params.put("PAGADO", filtros.getPagado());

        String tipoVenta = "Todas";
        if(filtros.getTipoVenta() != null && filtros.getTipoVenta() > Constantes.CANTIDAD_ZERO) {
            params.put("TIPO", filtros.getTipoVenta());
            if(filtros.getTipoVenta() .equals(Constantes.VENTA_TIPO_BIENES))
                tipoVenta = "Venta de Bienes";

            if(filtros.getTipoVenta() .equals(Constantes.VENTA_TIPO_SERVICIOS))
                tipoVenta = "Venta de Servicios";
        }
        parametros.put("tipoVenta", tipoVenta);

        String nombreCliente = "Todos";
        String documentoCliente = "";
        String tipoDocumentoCliente = "";
        if(filtros.getIdCliente() != null && filtros.getIdCliente().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("CLIENTE_ID", filtros.getIdCliente());
            Cliente cliente = clienteDAO.listarPorId(filtros.getIdCliente());

            nombreCliente = cliente != null ? cliente.getNombre() : "Cliente no encontrado";
            documentoCliente = cliente != null ? cliente.getDocumento() : "Cliente no encontrado";

            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(cliente != null ? cliente.getTipoDocumento().getId(): 0);
            tipoDocumentoCliente = tipoDocumento != null ? tipoDocumento.getTipo() : "Tipo de documento no encontrado";
        }
        parametros.put("nombreCliente", nombreCliente);
        parametros.put("documentoCliente", documentoCliente);
        parametros.put("tipoDocumento", tipoDocumentoCliente);

        if(filtros.getNombreCliente() != null && !filtros.getNombreCliente().trim().isEmpty())
            params.put("CLI_NOMBRE", "%"+filtros.getNombreCliente()+"%");

        if(filtros.getDocumentoCliente() != null && !filtros.getDocumentoCliente().trim().isEmpty())
            params.put("CLI_DOCUMENTO", filtros.getDocumentoCliente());

        if(filtros.getIdTipoDocumentoCliente() != null)
            params.put("CLI_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoCliente());

        if(filtros.getIdComprobante() != null && filtros.getIdComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("COMPROBANTE_ID", filtros.getIdComprobante());

        if(filtros.getSerieComprobante() != null && !filtros.getSerieComprobante().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieComprobante());

        if(filtros.getNumeroComprobante() != null && !filtros.getNumeroComprobante().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroComprobante());

        String comprobante = "Todos";
        if(filtros.getIdTipoComprobante() != null && filtros.getIdTipoComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("CO_TIPO_COMPROBANTE_ID", filtros.getIdTipoComprobante());
            TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(filtros.getIdTipoComprobante());
            comprobante = tipoComprobante != null ? tipoComprobante.getNombre() : "Tipo de comprobante no encontrado";

        }
        parametros.put("comprobante", comprobante);


        if(filtros.getIdUser() != null && filtros.getIdUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("USER_ID", filtros.getIdUser());
        }

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

        //Filtro para Detalle de Venta
        String productoName = "Todos";
        if(filtros.getIdProducto() != null && filtros.getIdProducto().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("PRODUCTO_ID", filtros.getIdProducto());
            Producto producto = productoDAO.listarPorId(filtros.getIdProducto());
            productoName = producto != null ? producto.getNombre() : "Producto no encontrado";
        }
        parametros.put("producto", productoName);


        //List<Venta> ventas = ventaMapper.listByParameterMap(params);
        List<VentasDetallesDTO> ventasDTO = ventaMapper.listDetailByParameterMap(params);

        List<VentasDetalladoReport> ventasDetalladoReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        ventasDTO.forEach((dto) -> {

            VentasDetalladoReport ventaDetalladoReport = new VentasDetalladoReport();

            ventaDetalladoReport.setNro(nro.get());
            ventaDetalladoReport.setSucursal(dto.getVenta().getAlmacen().getNombre());
            ventaDetalladoReport.setNumeroVenta(dto.getVenta().getNumeroVenta());
            ventaDetalladoReport.setFecha(dto.getVenta().getFecha().format(formato));
            ventaDetalladoReport.setHora(dto.getVenta().getHora().toString());

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_ANULADO))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_ANULADO_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_INICIADO))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_INICIADO_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR);

            ventaDetalladoReport.setEstado(dto.getVenta().getEstadoStr());

            if(dto.getVenta().getCliente() != null && dto.getVenta().getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(dto.getVenta().getCliente().getTipoDocumento().getId());
                    dto.getVenta().getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ventaDetalladoReport.setCliente(dto.getVenta().getCliente() != null ? dto.getVenta().getCliente().getNombre() + " " + dto.getVenta().getCliente().getTipoDocumento().getTipo() + " " + dto.getVenta().getCliente().getDocumento() : "");

            ventaDetalladoReport.setComprobante("");
            if(dto.getVenta().getComprobante() != null && dto.getVenta().getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(dto.getVenta().getComprobante().getInitComprobante().getId());
                    dto.getVenta().getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            ventaDetalladoReport.setComprobante(dto.getVenta().getComprobante() != null ? dto.getVenta().getComprobante().getInitComprobante().getTipoComprobante().getNombre() + " " + dto.getVenta().getComprobante().getSerie() + " " + dto.getVenta().getComprobante().getNumero() : "");

            ventaDetalladoReport.setTipoVenta("");
            if(dto.getVenta().getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                ventaDetalladoReport.setTipoVenta("Venta de Bienes");

            if(dto.getVenta().getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                ventaDetalladoReport.setTipoVenta("Venta de Servicios");

            ventaDetalladoReport.setUsuario(dto.getVenta().getUser() != null ? dto.getVenta().getUser().getName() : "");
            //ventaDetalladoReport.setMonto(dto.getVenta().getTotalMonto());


            ventaDetalladoReport.setProducto(dto.getDetalleVenta().getProducto() != null ? dto.getDetalleVenta().getUnidad() + " " + dto.getDetalleVenta().getProducto().getPresentacion().getPresentacion() + " " + dto.getDetalleVenta().getProducto().getNombre() + " " + dto.getDetalleVenta().getProducto().getMarca().getNombre() : "");
            ventaDetalladoReport.setCodigo(dto.getDetalleVenta().getProducto() != null ? dto.getDetalleVenta().getProducto().getCodigoProducto() : "");

            ventaDetalladoReport.setCantidad(dto.getDetalleVenta().getCantidad());
            ventaDetalladoReport.setPrecioUnitario(dto.getDetalleVenta().getPrecioVenta());

            if(dto.getDetalleVenta().getTipDescuento().equals(Constantes.TIPO_DESCUENTO_PORCENTIL))
                ventaDetalladoReport.setDescuento(dto.getDetalleVenta().getDescuento().setScale(2, RoundingMode.HALF_DOWN) + "%");
            else
                ventaDetalladoReport.setDescuento("S/." + dto.getDetalleVenta().getDescuento().setScale(2, RoundingMode.HALF_DOWN));

            ventaDetalladoReport.setPrecioTotal(dto.getDetalleVenta().getPrecioTotal());

            ventasDetalladoReport.add(ventaDetalladoReport);

            nro.set(nro.get() + 1L);

        });

        return ventasDetalladoJasperService.exportToPdf(ventasDetalladoReport,"data","reportVentasDetallado", parametros);
    }

    @Override
    public byte[] exportXls(FiltroVenta filtros) throws Exception {

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

        if(filtros.getNumeroVenta() != null && !filtros.getNumeroVenta().trim().isEmpty())
            params.put("NUMERO_VENTA", filtros.getNumeroVenta());

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
        if(filtros.getEstadoVenta() != null) {
            params.put("ESTADO", filtros.getEstadoVenta());

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_ANULADO))
                estado = Constantes.VENTA_ESTADO_ANULADO_STR;

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_INICIADO))
                estado = Constantes.VENTA_ESTADO_INICIADO_STR;

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                estado = Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR;

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                estado = Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR;

            if(filtros.getEstadoVenta() .equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                estado = Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR;
        }
        parametros.put("estado", estado);

        if(filtros.getPagado() != null)
            params.put("PAGADO", filtros.getPagado());

        String tipoVenta = "Todas";
        if(filtros.getTipoVenta() != null && filtros.getTipoVenta() > Constantes.CANTIDAD_ZERO) {
            params.put("TIPO", filtros.getTipoVenta());
            if(filtros.getTipoVenta() .equals(Constantes.VENTA_TIPO_BIENES))
                tipoVenta = "Venta de Bienes";

            if(filtros.getTipoVenta() .equals(Constantes.VENTA_TIPO_SERVICIOS))
                tipoVenta = "Venta de Servicios";
        }
        parametros.put("tipoVenta", tipoVenta);

        String nombreCliente = "Todos";
        String documentoCliente = "";
        String tipoDocumentoCliente = "";
        if(filtros.getIdCliente() != null && filtros.getIdCliente().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("CLIENTE_ID", filtros.getIdCliente());
            Cliente cliente = clienteDAO.listarPorId(filtros.getIdCliente());

            nombreCliente = cliente != null ? cliente.getNombre() : "Cliente no encontrado";
            documentoCliente = cliente != null ? cliente.getDocumento() : "Cliente no encontrado";

            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(cliente != null ? cliente.getTipoDocumento().getId(): 0);
            tipoDocumentoCliente = tipoDocumento != null ? tipoDocumento.getTipo() : "Tipo de documento no encontrado";
        }
        parametros.put("nombreCliente", nombreCliente);
        parametros.put("documentoCliente", documentoCliente);
        parametros.put("tipoDocumento", tipoDocumentoCliente);

        if(filtros.getNombreCliente() != null && !filtros.getNombreCliente().trim().isEmpty())
            params.put("CLI_NOMBRE", "%"+filtros.getNombreCliente()+"%");

        if(filtros.getDocumentoCliente() != null && !filtros.getDocumentoCliente().trim().isEmpty())
            params.put("CLI_DOCUMENTO", filtros.getDocumentoCliente());

        if(filtros.getIdTipoDocumentoCliente() != null)
            params.put("CLI_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoCliente());

        if(filtros.getIdComprobante() != null && filtros.getIdComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("COMPROBANTE_ID", filtros.getIdComprobante());

        if(filtros.getSerieComprobante() != null && !filtros.getSerieComprobante().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieComprobante());

        if(filtros.getNumeroComprobante() != null && !filtros.getNumeroComprobante().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroComprobante());

        String comprobante = "Todos";
        if(filtros.getIdTipoComprobante() != null && filtros.getIdTipoComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("CO_TIPO_COMPROBANTE_ID", filtros.getIdTipoComprobante());
            TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(filtros.getIdTipoComprobante());
            comprobante = tipoComprobante != null ? tipoComprobante.getNombre() : "Tipo de comprobante no encontrado";

        }
        parametros.put("comprobante", comprobante);


        if(filtros.getIdUser() != null && filtros.getIdUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("USER_ID", filtros.getIdUser());
        }

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

        //Filtro para Detalle de Venta
        String productoName = "Todos";
        if(filtros.getIdProducto() != null && filtros.getIdProducto().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("PRODUCTO_ID", filtros.getIdProducto());
            Producto producto = productoDAO.listarPorId(filtros.getIdProducto());
            productoName = producto != null ? producto.getNombre() : "Producto no encontrado";
        }
        parametros.put("producto", productoName);


        //List<Venta> ventas = ventaMapper.listByParameterMap(params);
        List<VentasDetallesDTO> ventasDTO = ventaMapper.listDetailByParameterMap(params);

        List<VentasDetalladoReport> ventasDetalladoReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        ventasDTO.forEach((dto) -> {

            VentasDetalladoReport ventaDetalladoReport = new VentasDetalladoReport();

            ventaDetalladoReport.setNro(nro.get());
            ventaDetalladoReport.setSucursal(dto.getVenta().getAlmacen().getNombre());
            ventaDetalladoReport.setNumeroVenta(dto.getVenta().getNumeroVenta());
            ventaDetalladoReport.setFecha(dto.getVenta().getFecha().format(formato));
            ventaDetalladoReport.setHora(dto.getVenta().getHora().toString());

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_ANULADO))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_ANULADO_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_INICIADO))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_INICIADO_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR);

            ventaDetalladoReport.setEstado(dto.getVenta().getEstadoStr());

            if(dto.getVenta().getCliente() != null && dto.getVenta().getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(dto.getVenta().getCliente().getTipoDocumento().getId());
                    dto.getVenta().getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ventaDetalladoReport.setCliente(dto.getVenta().getCliente() != null ? dto.getVenta().getCliente().getNombre() + " " + dto.getVenta().getCliente().getTipoDocumento().getTipo() + " " + dto.getVenta().getCliente().getDocumento() : "");

            ventaDetalladoReport.setComprobante("");
            if(dto.getVenta().getComprobante() != null && dto.getVenta().getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(dto.getVenta().getComprobante().getInitComprobante().getId());
                    dto.getVenta().getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            ventaDetalladoReport.setComprobante(dto.getVenta().getComprobante() != null ? dto.getVenta().getComprobante().getInitComprobante().getTipoComprobante().getNombre() + " " + dto.getVenta().getComprobante().getSerie() + " " + dto.getVenta().getComprobante().getNumero() : "");

            ventaDetalladoReport.setTipoVenta("");
            if(dto.getVenta().getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                ventaDetalladoReport.setTipoVenta("Venta de Bienes");

            if(dto.getVenta().getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                ventaDetalladoReport.setTipoVenta("Venta de Servicios");

            ventaDetalladoReport.setUsuario(dto.getVenta().getUser() != null ? dto.getVenta().getUser().getName() : "");
            //ventaDetalladoReport.setMonto(dto.getVenta().getTotalMonto());


            ventaDetalladoReport.setProducto(dto.getDetalleVenta().getProducto() != null ? dto.getDetalleVenta().getUnidad() + " " + dto.getDetalleVenta().getProducto().getPresentacion().getPresentacion() + " " + dto.getDetalleVenta().getProducto().getNombre() + " " + dto.getDetalleVenta().getProducto().getMarca().getNombre() : "");
            ventaDetalladoReport.setCodigo(dto.getDetalleVenta().getProducto() != null ? dto.getDetalleVenta().getProducto().getCodigoProducto() : "");

            ventaDetalladoReport.setCantidad(dto.getDetalleVenta().getCantidad());
            ventaDetalladoReport.setPrecioUnitario(dto.getDetalleVenta().getPrecioVenta());

            if(dto.getDetalleVenta().getTipDescuento().equals(Constantes.TIPO_DESCUENTO_PORCENTIL))
                ventaDetalladoReport.setDescuento(dto.getDetalleVenta().getDescuento().setScale(2, RoundingMode.HALF_DOWN) + "%");
            else
                ventaDetalladoReport.setDescuento("S/." + dto.getDetalleVenta().getDescuento().setScale(2, RoundingMode.HALF_DOWN));

            ventaDetalladoReport.setPrecioTotal(dto.getDetalleVenta().getPrecioTotal());

            ventasDetalladoReport.add(ventaDetalladoReport);

            nro.set(nro.get() + 1L);

        });

        return ventasDetalladoJasperService.exportToXls(ventasDetalladoReport,"data","reportVentasDetallado", "hoja1", parametros);
    }
}


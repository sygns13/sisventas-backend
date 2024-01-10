package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.VentaMapper;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.CajaIngresoVentasJasperService;
import com.bcs.ventas.service.reportes.CajaIngresoVentasReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroVenta;
import com.bcs.ventas.utils.reportbeans.CajaIngresoVentasReport;
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
public class CajaIngresoVentasReportServiceImpl implements CajaIngresoVentasReportService {

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
    private CajaIngresoVentasJasperService cajaIngresoVentasJasperService;

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public byte[] exportPdf(FiltroVenta filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> parametros = new HashMap<>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ESTADO_NO_ANULADO", Constantes.VENTA_ESTADO_ANULADO);
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


        List<Venta> ventas = ventaMapper.listByParameterMap(params);

        List<CajaIngresoVentasReport> cajaIngresoVentasReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        final AtomicReference<BigDecimal> montoTotal = new AtomicReference<>(new BigDecimal(0));

        ventas.forEach((venta) -> {

            CajaIngresoVentasReport cajaIngresoVentasReportObj = new CajaIngresoVentasReport();

            cajaIngresoVentasReportObj.setNro(nro.get());
            cajaIngresoVentasReportObj.setSucursal(venta.getAlmacen().getNombre());
            cajaIngresoVentasReportObj.setNumeroVenta(venta.getNumeroVenta());
            cajaIngresoVentasReportObj.setFecha(venta.getFecha().format(formato));
            cajaIngresoVentasReportObj.setHora(venta.getHora().toString());

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_ANULADO))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_ANULADO_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_INICIADO))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_INICIADO_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR);

            cajaIngresoVentasReportObj.setEstado(venta.getEstadoStr());

            if(venta.getCliente() != null && venta.getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(venta.getCliente().getTipoDocumento().getId());
                    venta.getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            cajaIngresoVentasReportObj.setCliente(venta.getCliente() != null ? venta.getCliente().getNombre() + " " + venta.getCliente().getTipoDocumento().getTipo() + " " + venta.getCliente().getDocumento() : "");

            cajaIngresoVentasReportObj.setComprobante("");
            if(venta.getComprobante() != null && venta.getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(venta.getComprobante().getInitComprobante().getId());
                    venta.getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cajaIngresoVentasReportObj.setComprobante(venta.getComprobante() != null ? venta.getComprobante().getInitComprobante().getTipoComprobante().getNombre() + " " + venta.getComprobante().getSerie() + " " + venta.getComprobante().getNumero() : "");

            cajaIngresoVentasReportObj.setTipoVenta("");
            if(venta.getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                cajaIngresoVentasReportObj.setTipoVenta("Venta de Bienes");

            if(venta.getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                cajaIngresoVentasReportObj.setTipoVenta("Venta de Servicios");


            cajaIngresoVentasReportObj.setImportePagado(venta.getMontoCobrado());

            montoTotal.set(montoTotal.get().add(venta.getMontoCobrado()));
            cajaIngresoVentasReport.add(cajaIngresoVentasReportObj);

            nro.set(nro.get() + 1L);

        });
        
        BigDecimal montoTotalFinal = montoTotal.get();
        parametros.put("montoTotal", montoTotalFinal);

        return cajaIngresoVentasJasperService.exportToPdf(cajaIngresoVentasReport,"data","reportCajaIngresoVentas", parametros);
    }

    @Override
    public byte[] exportXls(FiltroVenta filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> parametros = new HashMap<>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ESTADO_NO_ANULADO", Constantes.VENTA_ESTADO_ANULADO);
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


        List<Venta> ventas = ventaMapper.listByParameterMap(params);

        List<CajaIngresoVentasReport> cajaIngresoVentasReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        AtomicReference<BigDecimal> montoTotal = new AtomicReference<>(new BigDecimal(0));

        ventas.forEach((venta) -> {

            CajaIngresoVentasReport cajaIngresoVentasReportObj = new CajaIngresoVentasReport();

            cajaIngresoVentasReportObj.setNro(nro.get());
            cajaIngresoVentasReportObj.setSucursal(venta.getAlmacen().getNombre());
            cajaIngresoVentasReportObj.setNumeroVenta(venta.getNumeroVenta());
            cajaIngresoVentasReportObj.setFecha(venta.getFecha().format(formato));
            cajaIngresoVentasReportObj.setHora(venta.getHora().toString());

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_ANULADO))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_ANULADO_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_INICIADO))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_INICIADO_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR);

            cajaIngresoVentasReportObj.setEstado(venta.getEstadoStr());

            if(venta.getCliente() != null && venta.getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(venta.getCliente().getTipoDocumento().getId());
                    venta.getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            cajaIngresoVentasReportObj.setCliente(venta.getCliente() != null ? venta.getCliente().getNombre() + " " + venta.getCliente().getTipoDocumento().getTipo() + " " + venta.getCliente().getDocumento() : "");

            cajaIngresoVentasReportObj.setComprobante("");
            if(venta.getComprobante() != null && venta.getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(venta.getComprobante().getInitComprobante().getId());
                    venta.getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            cajaIngresoVentasReportObj.setComprobante(venta.getComprobante() != null ? venta.getComprobante().getInitComprobante().getTipoComprobante().getNombre() + " " + venta.getComprobante().getSerie() + " " + venta.getComprobante().getNumero() : "");

            cajaIngresoVentasReportObj.setTipoVenta("");
            if(venta.getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                cajaIngresoVentasReportObj.setTipoVenta("Venta de Bienes");

            if(venta.getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                cajaIngresoVentasReportObj.setTipoVenta("Venta de Servicios");


            cajaIngresoVentasReportObj.setImportePagado(venta.getMontoCobrado());

            montoTotal.set(montoTotal.get().add(venta.getMontoCobrado()));
            cajaIngresoVentasReport.add(cajaIngresoVentasReportObj);

            nro.set(nro.get() + 1L);

        });

        BigDecimal montoTotalFinal = montoTotal.get();
        parametros.put("montoTotal", montoTotalFinal);

        return cajaIngresoVentasJasperService.exportToXls(cajaIngresoVentasReport,"data","reportCajaIngresoVentas", "hoja1", parametros);
    }
}

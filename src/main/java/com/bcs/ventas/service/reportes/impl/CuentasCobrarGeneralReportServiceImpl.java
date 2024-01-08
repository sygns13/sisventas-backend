package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.VentaMapper;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.CuentasCobrarGeneralJasperService;
import com.bcs.ventas.service.reportes.CuentasCobrarGeneralReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroVenta;
import com.bcs.ventas.utils.reportbeans.CuentasCobrarGeneralReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CuentasCobrarGeneralReportServiceImpl implements CuentasCobrarGeneralReportService {

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
    private CuentasCobrarGeneralJasperService cuentasCobrarGeneralJasperService;

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


        if(filtros.getEstadoVenta() != null) {
            params.put("ESTADO", filtros.getEstadoVenta());
        }


        String estado = "Todas";
        if(filtros.getPagado() != null) {
            params.put("PAGADO", filtros.getPagado());
            if(filtros.getPagado() .equals(Constantes.VENTA_SI_PAGADO))
                estado = "ventas Cobradas";
            else
                estado = "ventas por Cobrar";
        }
        parametros.put("estado", estado);

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


        List<Venta> ventas = ventaMapper.listByParameterMap(params);

        List<CuentasCobrarGeneralReport> ventasCobrarGeneralReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        ventas.forEach((venta) -> {

            CuentasCobrarGeneralReport ventaCobrarGeneralReport = new CuentasCobrarGeneralReport();

            ventaCobrarGeneralReport.setNro(nro.get());
            ventaCobrarGeneralReport.setSucursal(venta.getAlmacen().getNombre());
            ventaCobrarGeneralReport.setNumeroVenta(venta.getNumeroVenta());
            ventaCobrarGeneralReport.setFecha(venta.getFecha().format(formato));
            ventaCobrarGeneralReport.setHora(venta.getHora().toString());

            if(venta.getPagado().equals(Constantes.VENTA_SI_PAGADO))
                ventaCobrarGeneralReport.setEstado("Cobrada");
            else
                ventaCobrarGeneralReport.setEstado("Por Cobrar");

            if(venta.getCliente() != null && venta.getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(venta.getCliente().getTipoDocumento().getId());
                    venta.getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ventaCobrarGeneralReport.setCliente(venta.getCliente() != null ? venta.getCliente().getNombre() + " " + venta.getCliente().getTipoDocumento().getTipo() + " " + venta.getCliente().getDocumento() : "");

            ventaCobrarGeneralReport.setComprobante("");
            if(venta.getComprobante() != null && venta.getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(venta.getComprobante().getInitComprobante().getId());
                    venta.getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            ventaCobrarGeneralReport.setComprobante(venta.getComprobante() != null ? venta.getComprobante().getInitComprobante().getTipoComprobante().getNombre() + " " + venta.getComprobante().getSerie() + " " + venta.getComprobante().getNumero() : "");

            ventaCobrarGeneralReport.setTipoVenta("");
            if(venta.getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                ventaCobrarGeneralReport.setTipoVenta("Venta de Bienes");

            if(venta.getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                ventaCobrarGeneralReport.setTipoVenta("Venta de Servicios");

            ventaCobrarGeneralReport.setUsuario(venta.getUser() != null ? venta.getUser().getName() : "");
            ventaCobrarGeneralReport.setImporteTotal(venta.getImporteTotal());
            ventaCobrarGeneralReport.setImporteCobrado(venta.getMontoCobrado());
            ventaCobrarGeneralReport.setImportePendiente(venta.getMontoPorCobrar());

            ventasCobrarGeneralReport.add(ventaCobrarGeneralReport);

            nro.set(nro.get() + 1L);

        });

        return cuentasCobrarGeneralJasperService.exportToPdf(ventasCobrarGeneralReport,"data","reportCuentasCobrarGeneral", parametros);
    }

    @Override
    public byte[] exportXls(FiltroVenta filtros) throws Exception {

        ///Oauth inicio
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


        if(filtros.getEstadoVenta() != null) {
            params.put("ESTADO", filtros.getEstadoVenta());
        }


        String estado = "Todas";
        if(filtros.getPagado() != null) {
            params.put("PAGADO", filtros.getPagado());
            if(filtros.getPagado() .equals(Constantes.VENTA_SI_PAGADO))
                estado = "ventas Cobradas";
            else
                estado = "ventas por Cobrar";
        }
        parametros.put("estado", estado);

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


        List<Venta> ventas = ventaMapper.listByParameterMap(params);

        List<CuentasCobrarGeneralReport> ventasCobrarGeneralReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        ventas.forEach((venta) -> {

            CuentasCobrarGeneralReport ventaCobrarGeneralReport = new CuentasCobrarGeneralReport();

            ventaCobrarGeneralReport.setNro(nro.get());
            ventaCobrarGeneralReport.setSucursal(venta.getAlmacen().getNombre());
            ventaCobrarGeneralReport.setNumeroVenta(venta.getNumeroVenta());
            ventaCobrarGeneralReport.setFecha(venta.getFecha().format(formato));
            ventaCobrarGeneralReport.setHora(venta.getHora().toString());

            if(venta.getPagado().equals(Constantes.VENTA_SI_PAGADO))
                ventaCobrarGeneralReport.setEstado("Cobrada");
            else
                ventaCobrarGeneralReport.setEstado("Por Cobrar");

            if(venta.getCliente() != null && venta.getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(venta.getCliente().getTipoDocumento().getId());
                    venta.getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ventaCobrarGeneralReport.setCliente(venta.getCliente() != null ? venta.getCliente().getNombre() + " " + venta.getCliente().getTipoDocumento().getTipo() + " " + venta.getCliente().getDocumento() : "");

            ventaCobrarGeneralReport.setComprobante("");
            if(venta.getComprobante() != null && venta.getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(venta.getComprobante().getInitComprobante().getId());
                    venta.getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            ventaCobrarGeneralReport.setComprobante(venta.getComprobante() != null ? venta.getComprobante().getInitComprobante().getTipoComprobante().getNombre() + " " + venta.getComprobante().getSerie() + " " + venta.getComprobante().getNumero() : "");

            ventaCobrarGeneralReport.setTipoVenta("");
            if(venta.getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                ventaCobrarGeneralReport.setTipoVenta("Venta de Bienes");

            if(venta.getTipo().equals(Constantes.VENTA_TIPO_BIENES))
                ventaCobrarGeneralReport.setTipoVenta("Venta de Servicios");

            ventaCobrarGeneralReport.setUsuario(venta.getUser() != null ? venta.getUser().getName() : "");
            ventaCobrarGeneralReport.setImporteTotal(venta.getImporteTotal());
            ventaCobrarGeneralReport.setImporteCobrado(venta.getMontoCobrado());
            ventaCobrarGeneralReport.setImportePendiente(venta.getMontoPorCobrar());

            ventasCobrarGeneralReport.add(ventaCobrarGeneralReport);

            nro.set(nro.get() + 1L);

        });

        return cuentasCobrarGeneralJasperService.exportToXls(ventasCobrarGeneralReport,"data","reportCuentasCobrarGeneral", "hoja1", parametros);
    }
}

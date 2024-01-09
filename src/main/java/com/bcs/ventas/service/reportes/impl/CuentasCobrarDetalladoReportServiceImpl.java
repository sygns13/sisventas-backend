package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.VentaMapper;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.CuentasCobrarDetalladoJasperService;
import com.bcs.ventas.service.reportes.CuentasCobrarDetalladoReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroVenta;
import com.bcs.ventas.utils.reportbeans.CuentasCobrarDetalladoReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CuentasCobrarDetalladoReportServiceImpl implements CuentasCobrarDetalladoReportService {

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
    private CuentasCobrarDetalladoJasperService cuentasCobrarDetalladoJasperService;

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


        List<CobroVenta> ventasDTO = ventaMapper.listByParameterMapCobrarVenta(params);

        List<CuentasCobrarDetalladoReport> ventasCobrarDetalladoReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        ventasDTO.forEach((dto) -> {

            CuentasCobrarDetalladoReport ventaCobrarDetalladoReport = new CuentasCobrarDetalladoReport();

            ventaCobrarDetalladoReport.setNro(nro.get());
            ventaCobrarDetalladoReport.setSucursal(dto.getVenta().getAlmacen().getNombre());
            ventaCobrarDetalladoReport.setNumeroVenta(dto.getVenta().getNumeroVenta());
            ventaCobrarDetalladoReport.setFecha(dto.getFecha().format(formato));


            if(dto.getVenta().getCliente() != null && dto.getVenta().getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(dto.getVenta().getCliente().getTipoDocumento().getId());
                    dto.getVenta().getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ventaCobrarDetalladoReport.setCliente(dto.getVenta().getCliente() != null ? dto.getVenta().getCliente().getNombre() + " " + dto.getVenta().getCliente().getTipoDocumento().getTipo() + " " + dto.getVenta().getCliente().getDocumento() : "");

            ventaCobrarDetalladoReport.setComprobante("");
            if(dto.getVenta().getComprobante() != null && dto.getVenta().getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(dto.getVenta().getComprobante().getInitComprobante().getId());
                    dto.getVenta().getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            ventaCobrarDetalladoReport.setComprobante(dto.getVenta().getComprobante() != null ? dto.getVenta().getComprobante().getInitComprobante().getTipoComprobante().getNombre() + " " + dto.getVenta().getComprobante().getSerie() + " " + dto.getVenta().getComprobante().getNumero() : "");

            ventaCobrarDetalladoReport.setMetodoPago(dto.getMetodoPago() != null ? dto.getMetodoPago().getNombre() : "");

            ventaCobrarDetalladoReport.setDatosPago("");

            if(dto.getMetodoPago() != null){

                if(dto.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){
                    ventaCobrarDetalladoReport.setDatosPago("Banco: " + dto.getBanco() + " Nro. de Cuenta: " + dto.getNumeroCuenta() + " Nro. Operación: " + dto.getCodigoOperacion());
                }

                if(dto.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){
                    ventaCobrarDetalladoReport.setDatosPago("Tarjeta: " + dto.getTipoTarjeta() + " Last 4: " + dto.getNumeroTarjeta());
                }

                if(dto.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){
                    ventaCobrarDetalladoReport.setDatosPago("Número de Celular: " + dto.getNumeroCelular() + " Nro. Operación: " + dto.getCodigoOperacion());
                }

                if(dto.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){
                    ventaCobrarDetalladoReport.setDatosPago("Banco: " + dto.getBanco() + " Nro. Cheque: " + dto.getNumeroCheque());
                }
            }


            ventaCobrarDetalladoReport.setImportePago(dto.getImporte());
            ventaCobrarDetalladoReport.setUsuario(dto.getVenta().getUser() != null ? dto.getVenta().getUser().getName() : "");


            ventasCobrarDetalladoReport.add(ventaCobrarDetalladoReport);

            nro.set(nro.get() + 1L);

        });

        return cuentasCobrarDetalladoJasperService.exportToPdf(ventasCobrarDetalladoReport,"data","reportCuentasCobrarDetallado", parametros);
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


        List<CobroVenta> ventasDTO = ventaMapper.listByParameterMapCobrarVenta(params);

        List<CuentasCobrarDetalladoReport> ventasCobrarDetalladoReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        ventasDTO.forEach((dto) -> {

            CuentasCobrarDetalladoReport ventaCobrarDetalladoReport = new CuentasCobrarDetalladoReport();

            ventaCobrarDetalladoReport.setNro(nro.get());
            ventaCobrarDetalladoReport.setSucursal(dto.getVenta().getAlmacen().getNombre());
            ventaCobrarDetalladoReport.setNumeroVenta(dto.getVenta().getNumeroVenta());
            ventaCobrarDetalladoReport.setFecha(dto.getFecha().format(formato));


            if(dto.getVenta().getCliente() != null && dto.getVenta().getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(dto.getVenta().getCliente().getTipoDocumento().getId());
                    dto.getVenta().getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ventaCobrarDetalladoReport.setCliente(dto.getVenta().getCliente() != null ? dto.getVenta().getCliente().getNombre() + " " + dto.getVenta().getCliente().getTipoDocumento().getTipo() + " " + dto.getVenta().getCliente().getDocumento() : "");

            ventaCobrarDetalladoReport.setComprobante("");
            if(dto.getVenta().getComprobante() != null && dto.getVenta().getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(dto.getVenta().getComprobante().getInitComprobante().getId());
                    dto.getVenta().getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            ventaCobrarDetalladoReport.setComprobante(dto.getVenta().getComprobante() != null ? dto.getVenta().getComprobante().getInitComprobante().getTipoComprobante().getNombre() + " " + dto.getVenta().getComprobante().getSerie() + " " + dto.getVenta().getComprobante().getNumero() : "");

            ventaCobrarDetalladoReport.setMetodoPago(dto.getMetodoPago() != null ? dto.getMetodoPago().getNombre() : "");

            ventaCobrarDetalladoReport.setDatosPago("");

            if(dto.getMetodoPago() != null){

                if(dto.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){
                    ventaCobrarDetalladoReport.setDatosPago("Banco: " + dto.getBanco() + " Nro. de Cuenta: " + dto.getNumeroCuenta() + " Nro. Operación: " + dto.getCodigoOperacion());
                }

                if(dto.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){
                    ventaCobrarDetalladoReport.setDatosPago("Tarjeta: " + dto.getTipoTarjeta() + " Last 4: " + dto.getNumeroTarjeta());
                }

                if(dto.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){
                    ventaCobrarDetalladoReport.setDatosPago("Número de Celular: " + dto.getNumeroCelular() + " Nro. Operación: " + dto.getCodigoOperacion());
                }

                if(dto.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){
                    ventaCobrarDetalladoReport.setDatosPago("Banco: " + dto.getBanco() + " Nro. Cheque: " + dto.getNumeroCheque());
                }
            }


            ventaCobrarDetalladoReport.setImportePago(dto.getImporte());
            ventaCobrarDetalladoReport.setUsuario(dto.getVenta().getUser() != null ? dto.getVenta().getUser().getName() : "");


            ventasCobrarDetalladoReport.add(ventaCobrarDetalladoReport);

            nro.set(nro.get() + 1L);

        });

        return cuentasCobrarDetalladoJasperService.exportToXls(ventasCobrarDetalladoReport,"data","reportCuentasCobrarDetallado", "hoja1", parametros);
    }
}

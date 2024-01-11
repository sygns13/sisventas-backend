package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.mappers.IngresoSalidaCajaMapper;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.service.jasper.CajaEgresosOtrosJasperService;
import com.bcs.ventas.service.reportes.CajaEgresosOtrosReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.reportbeans.CajaEgresosOtrosReport;
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
public class CajaEgresosOtrosReportServiceImpl implements CajaEgresosOtrosReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private AlmacenDAO almacenDAO;

    @Autowired
    private IngresoSalidaCajaMapper ingresoSalidaCajaMapper;

    @Autowired
    private CajaEgresosOtrosJasperService cajaEgresosOtrosJasperService;

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public byte[] exportPdf(FiltroGeneral filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final
        Map<String, Object> parametros = new HashMap<>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        //params.put("BUSCAR","%"+buscar+"%");
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);
        params.put("TIPO", Constantes.TIPO_RETIRO_PRODUCTOS);

        String almacen = "Todas las sucursales";
        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("ALMACEN_ID", filtros.getAlmacenId());
            Almacen almacen1 = almacenDAO.listarPorId(filtros.getAlmacenId());
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }
        parametros.put("sucursal", almacen);

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

        String tipoComprobante = "Todos";
        if(filtros.getTipoComprobanteOtros() != null && filtros.getTipoComprobanteOtros().compareTo(Constantes.CANTIDAD_UNIDAD_NEGATIVE_INTEGER) > 0){
            params.put("TIPO_COMPROBANTE", filtros.getTipoComprobanteOtros());
            switch (filtros.getTipoComprobanteOtros()){
                case 0:
                    tipoComprobante = "Movimiento Libre";
                    break;
                case 1:
                    tipoComprobante = "Factura";
                    break;
                case 2:
                    tipoComprobante = "Boleta";
                    break;
                case 3:
                    tipoComprobante = "Recibo por Honorarios";
                    break;
                case 4:
                    tipoComprobante = "Nota de Débito";
                    break;
                case 5:
                    tipoComprobante = "Nota de Crédito";
                    break;
                case 6:
                    tipoComprobante = "Recibo";
                    break;
                case 7:
                    tipoComprobante = "Otros";
                    break;
            }
        }
        parametros.put("tipoComprobante", tipoComprobante);


        List<IngresoSalidaCaja> ingresoSalidaCajas = ingresoSalidaCajaMapper.listByParameterMap(params);

        List<CajaEgresosOtrosReport> cajaEgresosOtrosReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        final AtomicReference<BigDecimal> montoTotal = new AtomicReference<>(new BigDecimal(0));

        ingresoSalidaCajas.forEach((ingreso) -> {

            CajaEgresosOtrosReport CajaEgresosOtrosReportoBJ = new CajaEgresosOtrosReport();

            CajaEgresosOtrosReportoBJ.setNro(nro.get());
            CajaEgresosOtrosReportoBJ.setSucursal(ingreso.getAlmacen().getNombre());
            CajaEgresosOtrosReportoBJ.setConcepto(ingreso.getConcepto());
            CajaEgresosOtrosReportoBJ.setFecha(ingreso.getFecha().format(formato));
            CajaEgresosOtrosReportoBJ.setHora(ingreso.getHora().toString());

            String tipoMovimientoComprobante = "";
            switch (ingreso.getTipoComprobante()){
                case 0:
                    tipoMovimientoComprobante = "Movimiento Libre";
                    break;
                case 1:
                    tipoMovimientoComprobante = "Factura";
                    break;
                case 2:
                    tipoMovimientoComprobante = "Boleta";
                    break;
                case 3:
                    tipoMovimientoComprobante = "Recibo por Honorarios";
                    break;
                case 4:
                    tipoMovimientoComprobante = "Nota de Débito";
                    break;
                case 5:
                    tipoMovimientoComprobante = "Nota de Crédito";
                    break;
                case 6:
                    tipoMovimientoComprobante = "Recibo";
                    break;
                case 7:
                    tipoMovimientoComprobante = "Otros";
                    break;
            }

            CajaEgresosOtrosReportoBJ.setTipoComprobante(tipoMovimientoComprobante);
            CajaEgresosOtrosReportoBJ.setComprobante(ingreso.getComprobante());

            CajaEgresosOtrosReportoBJ.setImportePagado(ingreso.getMonto());

            montoTotal.set(montoTotal.get().add(ingreso.getMonto()));

            cajaEgresosOtrosReport.add(CajaEgresosOtrosReportoBJ);

            nro.set(nro.get() + 1L);

        });

        BigDecimal montoTotalFinal = montoTotal.get();
        parametros.put("montoTotal", montoTotalFinal);

        return cajaEgresosOtrosJasperService.exportToPdf(cajaEgresosOtrosReport,"data","reportCajaEgresosOtros", parametros);
    }

    @Override
    public byte[] exportXls(FiltroGeneral filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final
        Map<String, Object> parametros = new HashMap<>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        //params.put("BUSCAR","%"+buscar+"%");
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);
        params.put("TIPO", Constantes.TIPO_RETIRO_PRODUCTOS);

        String almacen = "Todas las sucursales";
        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0) {
            params.put("ALMACEN_ID", filtros.getAlmacenId());
            Almacen almacen1 = almacenDAO.listarPorId(filtros.getAlmacenId());
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }
        parametros.put("sucursal", almacen);

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

        String tipoComprobante = "Todos";
        if(filtros.getTipoComprobanteOtros() != null && filtros.getTipoComprobanteOtros().compareTo(Constantes.CANTIDAD_UNIDAD_NEGATIVE_INTEGER) > 0){
            params.put("TIPO_COMPROBANTE", filtros.getTipoComprobanteOtros());
            switch (filtros.getTipoComprobanteOtros()){
                case 0:
                    tipoComprobante = "Movimiento Libre";
                    break;
                case 1:
                    tipoComprobante = "Factura";
                    break;
                case 2:
                    tipoComprobante = "Boleta";
                    break;
                case 3:
                    tipoComprobante = "Recibo por Honorarios";
                    break;
                case 4:
                    tipoComprobante = "Nota de Débito";
                    break;
                case 5:
                    tipoComprobante = "Nota de Crédito";
                    break;
                case 6:
                    tipoComprobante = "Recibo";
                    break;
                case 7:
                    tipoComprobante = "Otros";
                    break;
            }
        }
        parametros.put("tipoComprobante", tipoComprobante);


        List<IngresoSalidaCaja> ingresoSalidaCajas = ingresoSalidaCajaMapper.listByParameterMap(params);

        List<CajaEgresosOtrosReport> cajaEgresosOtrosReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        final AtomicReference<BigDecimal> montoTotal = new AtomicReference<>(new BigDecimal(0));

        ingresoSalidaCajas.forEach((ingreso) -> {

            CajaEgresosOtrosReport CajaEgresosOtrosReportoBJ = new CajaEgresosOtrosReport();

            CajaEgresosOtrosReportoBJ.setNro(nro.get());
            CajaEgresosOtrosReportoBJ.setSucursal(ingreso.getAlmacen().getNombre());
            CajaEgresosOtrosReportoBJ.setConcepto(ingreso.getConcepto());
            CajaEgresosOtrosReportoBJ.setFecha(ingreso.getFecha().format(formato));
            CajaEgresosOtrosReportoBJ.setHora(ingreso.getHora().toString());

            String tipoMovimientoComprobante = "";
            switch (ingreso.getTipoComprobante()){
                case 0:
                    tipoMovimientoComprobante = "Movimiento Libre";
                    break;
                case 1:
                    tipoMovimientoComprobante = "Factura";
                    break;
                case 2:
                    tipoMovimientoComprobante = "Boleta";
                    break;
                case 3:
                    tipoMovimientoComprobante = "Recibo por Honorarios";
                    break;
                case 4:
                    tipoMovimientoComprobante = "Nota de Débito";
                    break;
                case 5:
                    tipoMovimientoComprobante = "Nota de Crédito";
                    break;
                case 6:
                    tipoMovimientoComprobante = "Recibo";
                    break;
                case 7:
                    tipoMovimientoComprobante = "Otros";
                    break;
            }

            CajaEgresosOtrosReportoBJ.setTipoComprobante(tipoMovimientoComprobante);
            CajaEgresosOtrosReportoBJ.setComprobante(ingreso.getComprobante());

            CajaEgresosOtrosReportoBJ.setImportePagado(ingreso.getMonto());

            montoTotal.set(montoTotal.get().add(ingreso.getMonto()));

            cajaEgresosOtrosReport.add(CajaEgresosOtrosReportoBJ);

            nro.set(nro.get() + 1L);

        });

        BigDecimal montoTotalFinal = montoTotal.get();
        parametros.put("montoTotal", montoTotalFinal);

        return cajaEgresosOtrosJasperService.exportToXls(cajaEgresosOtrosReport,"data","reportCajaEgresosOtros", "hoja1", parametros);
    }
}

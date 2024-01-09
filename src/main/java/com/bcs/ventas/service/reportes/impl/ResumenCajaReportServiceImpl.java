package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.CobroVentaDAO;
import com.bcs.ventas.dao.IngresoSalidaCajaDAO;
import com.bcs.ventas.dao.PagoProveedorDAO;
import com.bcs.ventas.dao.mappers.CobroVentaMapper;
import com.bcs.ventas.dao.mappers.IngresoSalidaCajaMapper;
import com.bcs.ventas.dao.mappers.PagoProveedorMapper;
import com.bcs.ventas.model.dto.CajaSucursalDTO;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.CobroVenta;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.model.entities.PagoProveedor;
import com.bcs.ventas.service.AlmacenService;
import com.bcs.ventas.service.jasper.ResumenCajaJasperService;
import com.bcs.ventas.service.reportes.ResumenCajaReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResumenCajaReportServiceImpl implements ResumenCajaReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private ResumenCajaJasperService resumenCajaJasperService;

    @Autowired
    private CobroVentaDAO cobroVentaDAO;

    @Autowired
    private CobroVentaMapper cobroVentaMapper;

    @Autowired
    private PagoProveedorDAO pagoProveedorDAO;

    @Autowired
    private PagoProveedorMapper pagoProveedorMapper;

    @Autowired
    private IngresoSalidaCajaDAO ingresoSalidaCajaDAO;

    @Autowired
    private IngresoSalidaCajaMapper ingresoSalidaCajaMapper;

    @Autowired
    private AlmacenService almacenService;

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public byte[] exportPdf(FiltroGeneral filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final
        CajaSucursalDTO cajaSucursalDTO = new CajaSucursalDTO();


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);



        String almacen = "Todas las sucursales";
        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",filtros.getAlmacenId());
            Almacen almacen1 = almacenService.listarPorId(filtros.getAlmacenId());
            cajaSucursalDTO.setAlmacen(almacen1);
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }

        String titulo = "Resumen de Caja";
        if(filtros.getFecha() != null) {
            params.put("FECHA", filtros.getFecha());
            titulo = titulo + " - Del: " + filtros.getFecha().format(formato);
        }
        else {
            String fechaDesde = "Todas";
            String fechaHasta = "Todas";
            if (filtros.getFechaInicio() != null && filtros.getFechaFinal() != null) {
                params.put("FECHA_INI", filtros.getFechaInicio());
                params.put("FECHA_FIN", filtros.getFechaFinal());
                fechaDesde = filtros.getFechaInicio().format(formato);
                fechaHasta = filtros.getFechaFinal().format(formato);

                titulo = titulo + " - Desde: " + fechaDesde + " Hasta: " + fechaHasta;
            }
        }


        List<CobroVenta> cobroVentas = cobroVentaMapper.listByParameterMap(params);
        List<PagoProveedor> pagoProveedors = pagoProveedorMapper.listByParameterMap(params);

        params.put("TIPO",Constantes.INGRESO_SALIDAS_CAJA_TIPO_INGRESO);
        List<IngresoSalidaCaja> ingresoOtros = ingresoSalidaCajaMapper.listByParameterMap(params);

        params.put("TIPO",Constantes.INGRESO_SALIDAS_CAJA_TIPO_SALIDA);
        List<IngresoSalidaCaja> salidaOtros = ingresoSalidaCajaMapper.listByParameterMap(params);


        cajaSucursalDTO.setIngresosVentas(new BigDecimal("0"));
        cajaSucursalDTO.setEgresosCompras(new BigDecimal("0"));
        cajaSucursalDTO.setIngresosOtros(new BigDecimal("0"));
        cajaSucursalDTO.setEgresosOtros(new BigDecimal("0"));

        cajaSucursalDTO.setCajaInicial(new BigDecimal("0"));

        cobroVentas.forEach(cobroVenta -> {
            cajaSucursalDTO.setIngresosVentas(cajaSucursalDTO.getIngresosVentas().add(cobroVenta.getImporte()));
        });

        pagoProveedors.forEach(pagoProveedor -> {
            cajaSucursalDTO.setEgresosCompras(cajaSucursalDTO.getEgresosCompras().add(pagoProveedor.getMontoReal()));
        });

        ingresoOtros.forEach(ingresoOtro -> {
            cajaSucursalDTO.setIngresosOtros(cajaSucursalDTO.getIngresosOtros().add(ingresoOtro.getMonto()));
        });

        salidaOtros.forEach(salidaOtro -> {
            cajaSucursalDTO.setEgresosOtros(cajaSucursalDTO.getEgresosOtros().add(salidaOtro.getMonto()));
        });

        cajaSucursalDTO.setIngresosTotal(cajaSucursalDTO.getIngresosVentas().add(cajaSucursalDTO.getIngresosOtros()));
        cajaSucursalDTO.setEgresosTotal(cajaSucursalDTO.getEgresosCompras().add(cajaSucursalDTO.getEgresosOtros()));

        cajaSucursalDTO.setCajaTotal(cajaSucursalDTO.getIngresosTotal().subtract(cajaSucursalDTO.getEgresosTotal()));



        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("titulo", titulo);
        parametros.put("sucursal", almacen);

        parametros.put("cajaInicial", cajaSucursalDTO.getCajaInicial());
        parametros.put("cajaTotal", cajaSucursalDTO.getCajaTotal());
        parametros.put("ingresosVentas", cajaSucursalDTO.getIngresosVentas());
        parametros.put("ingresosOtros", cajaSucursalDTO.getIngresosOtros());
        parametros.put("egresosCompras", cajaSucursalDTO.getEgresosCompras());
        parametros.put("egresosOtros", cajaSucursalDTO.getEgresosOtros());
        parametros.put("ingresosTotal", cajaSucursalDTO.getIngresosTotal());
        parametros.put("egresosTotal", cajaSucursalDTO.getEgresosTotal());


        return resumenCajaJasperService.exportToPdf("data","reportResumenCaja", parametros);
    }

    @Override
    public byte[] exportXls(FiltroGeneral filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final
        CajaSucursalDTO cajaSucursalDTO = new CajaSucursalDTO();


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);



        String almacen = "Todas las sucursales";
        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",filtros.getAlmacenId());
            Almacen almacen1 = almacenService.listarPorId(filtros.getAlmacenId());
            cajaSucursalDTO.setAlmacen(almacen1);
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }

        String titulo = "Resumen de Caja";
        if(filtros.getFecha() != null) {
            params.put("FECHA", filtros.getFecha());
            titulo = titulo + " - Del: " + filtros.getFecha().format(formato);
        }
        else {
            String fechaDesde = "Todas";
            String fechaHasta = "Todas";
            if (filtros.getFechaInicio() != null && filtros.getFechaFinal() != null) {
                params.put("FECHA_INI", filtros.getFechaInicio());
                params.put("FECHA_FIN", filtros.getFechaFinal());
                fechaDesde = filtros.getFechaInicio().format(formato);
                fechaHasta = filtros.getFechaFinal().format(formato);

                titulo = titulo + " - Desde: " + fechaDesde + " Hasta: " + fechaHasta;
            }
        }


        List<CobroVenta> cobroVentas = cobroVentaMapper.listByParameterMap(params);
        List<PagoProveedor> pagoProveedors = pagoProveedorMapper.listByParameterMap(params);

        params.put("TIPO",Constantes.INGRESO_SALIDAS_CAJA_TIPO_INGRESO);
        List<IngresoSalidaCaja> ingresoOtros = ingresoSalidaCajaMapper.listByParameterMap(params);

        params.put("TIPO",Constantes.INGRESO_SALIDAS_CAJA_TIPO_SALIDA);
        List<IngresoSalidaCaja> salidaOtros = ingresoSalidaCajaMapper.listByParameterMap(params);


        cajaSucursalDTO.setIngresosVentas(new BigDecimal("0"));
        cajaSucursalDTO.setEgresosCompras(new BigDecimal("0"));
        cajaSucursalDTO.setIngresosOtros(new BigDecimal("0"));
        cajaSucursalDTO.setEgresosOtros(new BigDecimal("0"));

        cajaSucursalDTO.setCajaInicial(new BigDecimal("0"));

        cobroVentas.forEach(cobroVenta -> {
            cajaSucursalDTO.setIngresosVentas(cajaSucursalDTO.getIngresosVentas().add(cobroVenta.getImporte()));
        });

        pagoProveedors.forEach(pagoProveedor -> {
            cajaSucursalDTO.setEgresosCompras(cajaSucursalDTO.getEgresosCompras().add(pagoProveedor.getMontoReal()));
        });

        ingresoOtros.forEach(ingresoOtro -> {
            cajaSucursalDTO.setIngresosOtros(cajaSucursalDTO.getIngresosOtros().add(ingresoOtro.getMonto()));
        });

        salidaOtros.forEach(salidaOtro -> {
            cajaSucursalDTO.setEgresosOtros(cajaSucursalDTO.getEgresosOtros().add(salidaOtro.getMonto()));
        });

        cajaSucursalDTO.setIngresosTotal(cajaSucursalDTO.getIngresosVentas().add(cajaSucursalDTO.getIngresosOtros()));
        cajaSucursalDTO.setEgresosTotal(cajaSucursalDTO.getEgresosCompras().add(cajaSucursalDTO.getEgresosOtros()));

        cajaSucursalDTO.setCajaTotal(cajaSucursalDTO.getIngresosTotal().subtract(cajaSucursalDTO.getEgresosTotal()));



        Map<String, Object> parametros = new HashMap<>();
        parametros.put("titulo", titulo);
        parametros.put("sucursal", almacen);

        parametros.put("cajaInicial", cajaSucursalDTO.getCajaInicial());
        parametros.put("cajaTotal", cajaSucursalDTO.getCajaTotal());
        parametros.put("ingresosVentas", cajaSucursalDTO.getIngresosVentas());
        parametros.put("ingresosOtros", cajaSucursalDTO.getIngresosOtros());
        parametros.put("egresosCompras", cajaSucursalDTO.getEgresosCompras());
        parametros.put("egresosOtros", cajaSucursalDTO.getEgresosOtros());
        parametros.put("ingresosTotal", cajaSucursalDTO.getIngresosTotal());
        parametros.put("egresosTotal", cajaSucursalDTO.getEgresosTotal());
        
        return resumenCajaJasperService.exportToXls("data","reportResumenCaja", "hoja1", parametros);
    }
}

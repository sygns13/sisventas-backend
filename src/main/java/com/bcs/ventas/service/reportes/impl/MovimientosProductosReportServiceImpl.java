package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.mappers.RetiroEntradaProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.model.dto.RetiroEntradaProductoDTO;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Unidad;
import com.bcs.ventas.service.jasper.RetiroEntradaProductoJasperService;
import com.bcs.ventas.service.reportes.MovimientosProductosReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.reportbeans.MovimientosProductosReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MovimientosProductosReportServiceImpl implements MovimientosProductosReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private RetiroEntradaProductoJasperService retiroEntradaProductoJasperService;

    @Autowired
    private UnidadMapper unidadMapper;

    @Autowired
    private AlmacenDAO almacenDAO;

    @Autowired
    private RetiroEntradaProductoMapper retiroEntradaProductoMapper;

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public byte[] exportPdf(FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> parametros = new HashMap<String, String>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        // params.put("BUSCAR","%"+buscar+"%");

        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);


        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

        LocalDate fechaActual = LocalDate.now();
        params.put("FECHA_ACTUAL", fechaActual.toString());

        if(unidadG1.size() > 0 && unidadG1.get(0) != null && unidadG1.get(0).getId() != null){
            params.put("UNIDAD_ID", unidadG1.get(0).getId());
        }

        String almacen = "Todas las sucursales";
        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",filtros.getAlmacenId());
            Almacen almacen1 = almacenDAO.listarPorId(filtros.getAlmacenId());
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }
        parametros.put("sucursal", almacen);

        parametros.put("buscar", "");
        if(filtros.getPalabraClave() != null && !filtros.getPalabraClave().isEmpty()){
            params.put("BUSCAR","%"+filtros.getPalabraClave()+"%");
            parametros.put("buscar", filtros.getPalabraClave());
        }

        String tipoMovimiento = "Todos";
        if(filtros.getTipo() != null){
            params.put("TIPO",filtros.getTipo());

            switch (filtros.getTipo()){
                case 1:
                    tipoMovimiento = "Ingreso";
                    break;
                case 0:
                    tipoMovimiento = "Salida";
                    break;
                default:
                    tipoMovimiento = "Todos";
                    break;
            }
        }
        parametros.put("tipoMovimiento", tipoMovimiento);

        String fechaDesde = "Todas";
        if(filtros.getFechaInicio() != null){
            params.put("FECHA_INICIO", filtros.getFechaInicio().toString());
            fechaDesde = filtros.getFechaInicio().format(formato);
        }
        parametros.put("fechaDesde", fechaDesde);

        String fechaHasta = "Todas";
        if(filtros.getFechaFinal() != null){
            params.put("FECHA_FINAL", filtros.getFechaFinal().toString());
            fechaHasta = filtros.getFechaFinal().format(formato);
        }
        parametros.put("fechaHasta", fechaHasta);


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        

        List<RetiroEntradaProductoDTO> movimientosProductos = retiroEntradaProductoMapper.listByParameterMapMovientosProductos(params);

        List<MovimientosProductosReport> movimientosProductosReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        movimientosProductos.forEach(productosInventario -> {
            MovimientosProductosReport movimientosProductosReport = new MovimientosProductosReport();

            movimientosProductosReport.setNro(nro.get());
            movimientosProductosReport.setFecha(productosInventario.getRetiroEntradaProducto().getFecha().format(formato));
            movimientosProductosReport.setHora(productosInventario.getRetiroEntradaProducto().getHora().toString());
            movimientosProductosReport.setProducto(productosInventario.getProducto().getNombre());
            movimientosProductosReport.setCodigo(productosInventario.getProducto().getCodigoUnidad());
            movimientosProductosReport.setTipoProducto(productosInventario.getProducto().getTipoProducto().getTipo());
            movimientosProductosReport.setMarca(productosInventario.getProducto().getMarca().getNombre());
            movimientosProductosReport.setPresentacion(productosInventario.getProducto().getPresentacion().getPresentacion());
            movimientosProductosReport.setLote(productosInventario.getLote().getNombre());
            movimientosProductosReport.setTipoMovimiento(productosInventario.getRetiroEntradaProducto().getTipo() == 1 ? "Ingreso" : "Salida");
            movimientosProductosReport.setCantidad(productosInventario.getRetiroEntradaProducto().getCantidadReal());
            movimientosProductosReport.setMotivo(productosInventario.getRetiroEntradaProducto().getMotivo());
            movimientosProductosReport.setUsuario(productosInventario.getUser().getName());

            movimientosProductosReports.add(movimientosProductosReport);

            nro.set(nro.get() + 1L);
        });

        return retiroEntradaProductoJasperService.exportToPdf(movimientosProductosReports,"data","reportMovimientosProductos", parametros);
    }

    @Override
    public byte[] exportXls(FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> parametros = new HashMap<String, String>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        // params.put("BUSCAR","%"+buscar+"%");

        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);


        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

        LocalDate fechaActual = LocalDate.now();
        params.put("FECHA_ACTUAL", fechaActual.toString());

        if(unidadG1.size() > 0 && unidadG1.get(0) != null && unidadG1.get(0).getId() != null){
            params.put("UNIDAD_ID", unidadG1.get(0).getId());
        }

        String almacen = "Todas las sucursales";
        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",filtros.getAlmacenId());
            Almacen almacen1 = almacenDAO.listarPorId(filtros.getAlmacenId());
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }
        parametros.put("sucursal", almacen);

        parametros.put("buscar", "");
        if(filtros.getPalabraClave() != null && !filtros.getPalabraClave().isEmpty()){
            params.put("BUSCAR","%"+filtros.getPalabraClave()+"%");
            parametros.put("buscar", filtros.getPalabraClave());
        }

        String tipoMovimiento = "Todos";
        if(filtros.getTipo() != null){
            params.put("TIPO",filtros.getTipo());

            switch (filtros.getTipo()){
                case 1:
                    tipoMovimiento = "Ingreso";
                    break;
                case 0:
                    tipoMovimiento = "Salida";
                    break;
                default:
                    tipoMovimiento = "Todos";
                    break;
            }
        }
        parametros.put("tipoMovimiento", tipoMovimiento);

        String fechaDesde = "Todas";
        if(filtros.getFechaInicio() != null){
            params.put("FECHA_INICIO", filtros.getFechaInicio().toString());
            fechaDesde = filtros.getFechaInicio().format(formato);
        }
        parametros.put("fechaDesde", fechaDesde);

        String fechaHasta = "Todas";
        if(filtros.getFechaFinal() != null){
            params.put("FECHA_FINAL", filtros.getFechaFinal().toString());
            fechaHasta = filtros.getFechaFinal().format(formato);
        }
        parametros.put("fechaHasta", fechaHasta);


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);


        List<RetiroEntradaProductoDTO> movimientosProductos = retiroEntradaProductoMapper.listByParameterMapMovientosProductos(params);

        List<MovimientosProductosReport> movimientosProductosReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        movimientosProductos.forEach(productosInventario -> {
            MovimientosProductosReport movimientosProductosReport = new MovimientosProductosReport();

            movimientosProductosReport.setNro(nro.get());
            movimientosProductosReport.setFecha(productosInventario.getRetiroEntradaProducto().getFecha().format(formato));
            movimientosProductosReport.setHora(productosInventario.getRetiroEntradaProducto().getHora().toString());
            movimientosProductosReport.setProducto(productosInventario.getProducto().getNombre());
            movimientosProductosReport.setCodigo(productosInventario.getProducto().getCodigoUnidad());
            movimientosProductosReport.setTipoProducto(productosInventario.getProducto().getTipoProducto().getTipo());
            movimientosProductosReport.setMarca(productosInventario.getProducto().getMarca().getNombre());
            movimientosProductosReport.setPresentacion(productosInventario.getProducto().getPresentacion().getPresentacion());
            movimientosProductosReport.setLote(productosInventario.getLote().getNombre());
            movimientosProductosReport.setTipoMovimiento(productosInventario.getRetiroEntradaProducto().getTipo() == 1 ? "Ingreso" : "Salida");
            movimientosProductosReport.setCantidad(productosInventario.getRetiroEntradaProducto().getCantidadReal());
            movimientosProductosReport.setMotivo(productosInventario.getRetiroEntradaProducto().getMotivo());
            movimientosProductosReport.setUsuario(productosInventario.getUser().getName());

            movimientosProductosReports.add(movimientosProductosReport);

            nro.set(nro.get() + 1L);
        });

        return retiroEntradaProductoJasperService.exportToXls(movimientosProductosReports,"data","reportMovimientosProductos", "hoja1", parametros);
    }
}

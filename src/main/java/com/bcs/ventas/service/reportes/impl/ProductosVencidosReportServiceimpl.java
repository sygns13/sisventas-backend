package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.MarcaDAO;
import com.bcs.ventas.dao.PresentacionDAO;
import com.bcs.ventas.dao.TipoProductoDAO;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.model.dto.ProductoBajoStockDTO;
import com.bcs.ventas.model.dto.ProductoVencidoDTO;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Unidad;
import com.bcs.ventas.service.jasper.ProductosVencidosJasperService;
import com.bcs.ventas.service.reportes.ProductosVencidosReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.reportbeans.ProductosVencidosReport;
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
public class ProductosVencidosReportServiceimpl implements ProductosVencidosReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private ProductosVencidosJasperService productosVencidosJasperService;

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

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public byte[] exportPdf(FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> parametros = new HashMap<String, String>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        // params.put("BUSCAR","%"+buscar+"%");


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
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

        String tipoBusqueda = "";
        if(filtros.getTipo() != null){
            params.put("TIPO",filtros.getTipo());
            switch (filtros.getTipo())
            {
                case 0:
                    tipoBusqueda = "Productos vencidos";
                    break;
                case 1:
                    tipoBusqueda = "Productos que Vencen en 01 Semana";
                    break;
                case 2:
                    tipoBusqueda = "Productos que Vencen en 02 Semanas";
                    break;
                case 3:
                    tipoBusqueda = "Productos que Vencen en 03 Semanas";
                    break;
                case 4:
                    tipoBusqueda = "Productos que Vencen en 01 Mes";
                    break;
                case 5:
                    tipoBusqueda = "Productos que Vencen en 02 Meses";
                    break;
                case 6:
                    tipoBusqueda = "Productos que Vencen en 03 Meses";
                    break;
                case 7:
                    tipoBusqueda = "Productos por vencer ";
                    break;
                default:
                    tipoBusqueda = "Productos vencidos";
                    break;
            }

        }

        if(filtros.getFechaInicio() != null){
            params.put("FECHA_INICIO", filtros.getFechaInicio().toString());
            tipoBusqueda = tipoBusqueda + " desde " + filtros.getFechaInicio().format(formato);
        }

        if(filtros.getFechaFinal() != null){
            params.put("FECHA_FINAL", filtros.getFechaFinal().toString());
            tipoBusqueda = tipoBusqueda + " hasta " + filtros.getFechaFinal().format(formato);
        }
        parametros.put("tipoBusqueda", tipoBusqueda);


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);


        List<ProductoVencidoDTO> productosVencidos = productoMapper.listByParameterMapProductosVencidos(params);

        List<ProductosVencidosReport> productosVencidosReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        productosVencidos.forEach(dto -> {
            ProductosVencidosReport productosVencidosReport = new ProductosVencidosReport();

            productosVencidosReport.setNro(nro.get());
            productosVencidosReport.setCodigo(dto.getProducto().getCodigoUnidad());
            productosVencidosReport.setProducto(dto.getProducto().getNombre());
            productosVencidosReport.setTipoProducto(dto.getProducto().getTipoProducto().getTipo());
            productosVencidosReport.setMarca(dto.getProducto().getMarca().getNombre());
            productosVencidosReport.setPresentacion(dto.getProducto().getPresentacion().getPresentacion());
            productosVencidosReport.setComposicion(dto.getProducto().getComposicion());
            productosVencidosReport.setSucursal(dto.getAlmacen().getNombre());
            productosVencidosReport.setLote(dto.getLote().getNombre());
            productosVencidosReport.setStockLote(dto.getCantidadTotal());
            productosVencidosReport.setFechaVencimiento(dto.getLote().getFechaVencimiento().format(formato));

            productosVencidosReports.add(productosVencidosReport);

            nro.set(nro.get() + 1L);
        });


        return productosVencidosJasperService.exportToPdf(productosVencidosReports,"data","reportProductosVencidos", parametros);
    }

    @Override
    public byte[] exportXls(FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> parametros = new HashMap<String, String>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        // params.put("BUSCAR","%"+buscar+"%");


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
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

        String tipoBusqueda = "";
        if(filtros.getTipo() != null){
            params.put("TIPO",filtros.getTipo());
            switch (filtros.getTipo())
            {
                case 0:
                    tipoBusqueda = "Productos vencidos";
                    break;
                case 1:
                    tipoBusqueda = "Productos que Vencen en 01 Semana";
                    break;
                case 2:
                    tipoBusqueda = "Productos que Vencen en 02 Semanas";
                    break;
                case 3:
                    tipoBusqueda = "Productos que Vencen en 03 Semanas";
                    break;
                case 4:
                    tipoBusqueda = "Productos que Vencen en 01 Mes";
                    break;
                case 5:
                    tipoBusqueda = "Productos que Vencen en 02 Meses";
                    break;
                case 6:
                    tipoBusqueda = "Productos que Vencen en 03 Meses";
                    break;
                case 7:
                    tipoBusqueda = "Productos por vencer ";
                    break;
                default:
                    tipoBusqueda = "Productos vencidos";
                    break;
            }

        }

        if(filtros.getFechaInicio() != null){
            params.put("FECHA_INICIO", filtros.getFechaInicio().toString());
            tipoBusqueda = tipoBusqueda + " desde " + filtros.getFechaInicio().format(formato);
        }

        if(filtros.getFechaFinal() != null){
            params.put("FECHA_FINAL", filtros.getFechaFinal().toString());
            tipoBusqueda = tipoBusqueda + " hasta " + filtros.getFechaFinal().format(formato);
        }
        parametros.put("tipoBusqueda", tipoBusqueda);


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);


        List<ProductoVencidoDTO> productosVencidos = productoMapper.listByParameterMapProductosVencidos(params);

        List<ProductosVencidosReport> productosVencidosReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        productosVencidos.forEach(dto -> {
            ProductosVencidosReport productosVencidosReport = new ProductosVencidosReport();

            productosVencidosReport.setNro(nro.get());
            productosVencidosReport.setCodigo(dto.getProducto().getCodigoUnidad());
            productosVencidosReport.setProducto(dto.getProducto().getNombre());
            productosVencidosReport.setTipoProducto(dto.getProducto().getTipoProducto().getTipo());
            productosVencidosReport.setMarca(dto.getProducto().getMarca().getNombre());
            productosVencidosReport.setPresentacion(dto.getProducto().getPresentacion().getPresentacion());
            productosVencidosReport.setComposicion(dto.getProducto().getComposicion());
            productosVencidosReport.setSucursal(dto.getAlmacen().getNombre());
            productosVencidosReport.setLote(dto.getLote().getNombre());
            productosVencidosReport.setStockLote(dto.getCantidadTotal());
            productosVencidosReport.setFechaVencimiento(dto.getLote().getFechaVencimiento().format(formato));

            productosVencidosReports.add(productosVencidosReport);

            nro.set(nro.get() + 1L);
        });


        return productosVencidosJasperService.exportToXls(productosVencidosReports,"data","reportProductosVencidos", "hoja1", parametros);
    }
}

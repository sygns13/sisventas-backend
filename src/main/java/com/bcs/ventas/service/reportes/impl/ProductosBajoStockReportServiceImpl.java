package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.MarcaDAO;
import com.bcs.ventas.dao.PresentacionDAO;
import com.bcs.ventas.dao.TipoProductoDAO;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.model.dto.InventarioDTO;
import com.bcs.ventas.model.dto.ProductoBajoStockDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.ProductosBajoStockJasperService;
import com.bcs.ventas.service.reportes.ProductosBajoStockReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.beans.FiltroInventario;
import com.bcs.ventas.utils.reportbeans.ProductosBajoStockReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductosBajoStockReportServiceImpl implements ProductosBajoStockReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private ProductosBajoStockJasperService productosBajoStockJasperService;

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
    @Override
    public byte[] exportPdf(FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final
        Map<String, String> parametros = new HashMap<String, String>();

        // params.put("BUSCAR","%"+buscar+"%");

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

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


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);


        List<ProductoBajoStockDTO> productosBajoStock = productoMapper.listByParameterMapProductosBajoStock(params);

        List<ProductosBajoStockReport> productosBajoStockReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        productosBajoStock.forEach(dto -> {
            ProductosBajoStockReport productosBajoStockReport = new ProductosBajoStockReport();

            productosBajoStockReport.setNro(nro.get());
            productosBajoStockReport.setCodigo(dto.getProducto().getCodigoUnidad());
            productosBajoStockReport.setProducto(dto.getProducto().getNombre());
            productosBajoStockReport.setTipoProducto(dto.getProducto().getTipoProducto().getTipo());
            productosBajoStockReport.setMarca(dto.getProducto().getMarca().getNombre());
            productosBajoStockReport.setPresentacion(dto.getProducto().getPresentacion().getPresentacion());
            productosBajoStockReport.setComposicion(dto.getProducto().getComposicion());
            productosBajoStockReport.setSucursal(dto.getAlmacen().getNombre());
            productosBajoStockReport.setStockMinimo(dto.getProducto().getStockMinimo());
            productosBajoStockReport.setStockActual(dto.getStock().getCantidad());
            
            productosBajoStockReports.add(productosBajoStockReport);

            nro.set(nro.get() + 1L);
        });


        return productosBajoStockJasperService.exportToPdf(productosBajoStockReports,"data","reportProductosBajosStocks", parametros);
    }

    @Override
    public byte[] exportXls(FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final
        Map<String, String> parametros = new HashMap<String, String>();

        // params.put("BUSCAR","%"+buscar+"%");

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

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


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);


        List<ProductoBajoStockDTO> productosBajoStock = productoMapper.listByParameterMapProductosBajoStock(params);

        List<ProductosBajoStockReport> productosBajoStockReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        productosBajoStock.forEach(dto -> {
            ProductosBajoStockReport productosBajoStockReport = new ProductosBajoStockReport();

            productosBajoStockReport.setNro(nro.get());
            productosBajoStockReport.setCodigo(dto.getProducto().getCodigoUnidad());
            productosBajoStockReport.setProducto(dto.getProducto().getNombre());
            productosBajoStockReport.setTipoProducto(dto.getProducto().getTipoProducto().getTipo());
            productosBajoStockReport.setMarca(dto.getProducto().getMarca().getNombre());
            productosBajoStockReport.setPresentacion(dto.getProducto().getPresentacion().getPresentacion());
            productosBajoStockReport.setComposicion(dto.getProducto().getComposicion());
            productosBajoStockReport.setSucursal(dto.getAlmacen().getNombre());
            productosBajoStockReport.setStockMinimo(dto.getProducto().getStockMinimo());
            productosBajoStockReport.setStockActual(dto.getStock().getCantidad());

            productosBajoStockReports.add(productosBajoStockReport);

            nro.set(nro.get() + 1L);
        });


        return productosBajoStockJasperService.exportToXls(productosBajoStockReports,"data","reportProductosBajosStocks", "hoja1", parametros);
    }
}

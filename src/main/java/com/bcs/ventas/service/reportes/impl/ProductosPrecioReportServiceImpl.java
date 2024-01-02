package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.UnidadDAO;
import com.bcs.ventas.dao.mappers.DetalleUnidadProductoMapper;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.model.dto.InventarioDTO;
import com.bcs.ventas.model.dto.ProductosVentaDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.ProductosPrecioJasperService;
import com.bcs.ventas.service.reportes.ProductosPrecioReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroInventario;
import com.bcs.ventas.utils.reportbeans.ProductosPrecioReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductosPrecioReportServiceImpl implements ProductosPrecioReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private ProductosPrecioJasperService productosPrecioJasperService;

    @Autowired
    private UnidadMapper unidadMapper;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private AlmacenDAO almacenDAO;

    @Autowired
    private UnidadDAO unidadDAO;

    @Autowired
    private DetalleUnidadProductoMapper detalleUnidadProductoMapper;

    @Override
    public byte[] exportPdf(Long almacenId, Long unidadId) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> parametros = new HashMap<String, String>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID", EmpresaId);
        //params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);
        params.put("UNIDAD_ID", unidadId);
        params.put("ALMACEN_ID", almacenId);


        List<ProductosVentaDTO> productosVenta = productoMapper.listByParameterMapProductosVenta(params);

        productosVenta.forEach((p)-> {

            Map<String, Object> params1 = new HashMap<String, Object>();

            params1.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
            params1.put("EMPRESA_ID", EmpresaId);
            params1.put("PRODUCTO_ID",p.getProducto().getId());
            params1.put("PRODUCTO_PU",p.getProducto().getPrecioUnidad());
            params1.put("PRODUCTO_PC",p.getProducto().getPrecioCompra());
            params1.put("UNIDAD_ID", unidadId);
            params1.put("ALMACEN_ID", almacenId);

            List<DetalleUnidadProducto> detalleUnidadBD = detalleUnidadProductoMapper.listByParameterMapBaseUnidad(params1);

            if(detalleUnidadBD != null && !detalleUnidadBD.isEmpty() && detalleUnidadBD.get(0).getId() != null && detalleUnidadBD.get(0).getId() > 0){
                try {
                    Unidad unidad = unidadDAO.listarPorId(detalleUnidadBD.get(0).getUnidad().getId());
                    detalleUnidadBD.get(0).setUnidad(unidad);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                p.setDetalleUnidadProducto(detalleUnidadBD.get(0));
            }
        });

        List<ProductosPrecioReport> productosPrecioReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        productosVenta.forEach(productosInventario -> {
            ProductosPrecioReport productoPrecioReport = new ProductosPrecioReport();

            productoPrecioReport.setNro(nro.get());
            productoPrecioReport.setCodigo(productosInventario.getProducto().getCodigoUnidad());
            productoPrecioReport.setProducto(productosInventario.getProducto().getNombre());
            productoPrecioReport.setTipoProducto(productosInventario.getProducto().getTipoProducto().getTipo());
            productoPrecioReport.setMarca(productosInventario.getProducto().getMarca().getNombre());
            productoPrecioReport.setPrecioVenta(productosInventario.getDetalleUnidadProducto().getPrecio());
            productoPrecioReport.setCostoCompra(productosInventario.getDetalleUnidadProducto().getCostoCompra());
            productoPrecioReport.setUtilidad(productosInventario.getDetalleUnidadProducto().getPrecio().subtract(productosInventario.getDetalleUnidadProducto().getCostoCompra()));

            productosPrecioReport.add(productoPrecioReport);

            nro.set(nro.get() + 1L);
        });

        //Parametros
        String unidad = "Unidad";
        if(unidadId != null && unidadId.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            Unidad unidadG2 = unidadDAO.listarPorId(unidadId);
            unidad = unidadG2 != null ? unidadG2.getNombre() : "Unidad no encontrada";
        }
        parametros.put("unidad", unidad);

        String almacen = "Todas las sucursales";
        if(almacenId != null && almacenId.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            Almacen almacen1 = almacenDAO.listarPorId(almacenId);
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }
        parametros.put("sucursal", almacen);


        return productosPrecioJasperService.exportToPdf(productosPrecioReport,"data","reportProductosPrecio", parametros);
    }

    @Override
    public byte[] exportXls(Long almacenId, Long unidadId) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> parametros = new HashMap<String, String>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID", EmpresaId);
        //params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);
        params.put("UNIDAD_ID", unidadId);
        params.put("ALMACEN_ID", almacenId);


        List<ProductosVentaDTO> productosVenta = productoMapper.listByParameterMapProductosVenta(params);

        productosVenta.forEach((p)-> {

            Map<String, Object> params1 = new HashMap<String, Object>();

            params1.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
            params1.put("EMPRESA_ID", EmpresaId);
            params1.put("PRODUCTO_ID",p.getProducto().getId());
            params1.put("PRODUCTO_PU",p.getProducto().getPrecioUnidad());
            params1.put("PRODUCTO_PC",p.getProducto().getPrecioCompra());
            params1.put("UNIDAD_ID", unidadId);
            params1.put("ALMACEN_ID", almacenId);

            List<DetalleUnidadProducto> detalleUnidadBD = detalleUnidadProductoMapper.listByParameterMapBaseUnidad(params1);

            if(detalleUnidadBD != null && !detalleUnidadBD.isEmpty() && detalleUnidadBD.get(0).getId() != null && detalleUnidadBD.get(0).getId() > 0){
                try {
                    Unidad unidad = unidadDAO.listarPorId(detalleUnidadBD.get(0).getUnidad().getId());
                    detalleUnidadBD.get(0).setUnidad(unidad);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                p.setDetalleUnidadProducto(detalleUnidadBD.get(0));
            }
        });

        List<ProductosPrecioReport> productosPrecioReport = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        productosVenta.forEach(productosInventario -> {
            ProductosPrecioReport productoPrecioReport = new ProductosPrecioReport();

            productoPrecioReport.setNro(nro.get());
            productoPrecioReport.setCodigo(productosInventario.getProducto().getCodigoUnidad());
            productoPrecioReport.setProducto(productosInventario.getProducto().getNombre());
            productoPrecioReport.setTipoProducto(productosInventario.getProducto().getTipoProducto().getTipo());
            productoPrecioReport.setMarca(productosInventario.getProducto().getMarca().getNombre());
            productoPrecioReport.setPrecioVenta(productosInventario.getDetalleUnidadProducto().getPrecio());
            productoPrecioReport.setCostoCompra(productosInventario.getDetalleUnidadProducto().getCostoCompra());
            productoPrecioReport.setUtilidad(productosInventario.getDetalleUnidadProducto().getPrecio().subtract(productosInventario.getDetalleUnidadProducto().getCostoCompra()));

            productosPrecioReport.add(productoPrecioReport);

            nro.set(nro.get() + 1L);
        });

        //Parametros
        String unidad = "Unidad";
        if(unidadId != null && unidadId.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            Unidad unidadG2 = unidadDAO.listarPorId(unidadId);
            unidad = unidadG2 != null ? unidadG2.getNombre() : "Unidad no encontrada";
        }
        parametros.put("unidad", unidad);

        String almacen = "Todas las sucursales";
        if(almacenId != null && almacenId.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            Almacen almacen1 = almacenDAO.listarPorId(almacenId);
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }
        parametros.put("sucursal", almacen);


        return productosPrecioJasperService.exportToXls(productosPrecioReport,"data","reportProductosPrecio", "hoja1", parametros);
    }
}

package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.model.dto.InventarioDTO;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Unidad;
import com.bcs.ventas.service.jasper.ProductosSucursalJasperService;
import com.bcs.ventas.service.reportes.ProductosSucursalReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.reportbeans.ProductosSucursalReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductosSucursalReportServiceImpl implements ProductosSucursalReportService {

    @Autowired
    private ProductosSucursalJasperService productoSucursalJasperService;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private UnidadMapper unidadMapper;

    @Autowired
    private AlmacenDAO almacenDAO;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public byte[] exportPdf(Long almacenId) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

        if(unidadG1.size() > 0 && unidadG1.get(0) != null && unidadG1.get(0).getId() != null){
            params.put("UNIDAD_ID", unidadG1.get(0).getId());
        }

        String almacen = "Todas las sucursales";
        if(almacenId != null && almacenId.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID", almacenId);
            Almacen almacen1 = almacenDAO.listarPorId(almacenId);
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        List<InventarioDTO> inventario = productoMapper.listByParameterMapInventario(params);

        List<ProductosSucursalReport> productoSucursalReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        inventario.forEach(productoSucursal -> {
            ProductosSucursalReport productoSucursalReport = new ProductosSucursalReport();
            productoSucursalReport.setNro(nro.get());
            productoSucursalReport.setCodigo(productoSucursal.getProducto().getCodigoUnidad());
            productoSucursalReport.setProducto(productoSucursal.getProducto().getNombre());
            productoSucursalReport.setTipoProducto(productoSucursal.getProducto().getTipoProducto().getTipo());
            productoSucursalReport.setMarca(productoSucursal.getProducto().getMarca().getNombre());
            productoSucursalReport.setCantidad(productoSucursal.getStock().getCantidad());

            productoSucursalReports.add(productoSucursalReport);

            nro.set(nro.get() + 1L);
        });


        return productoSucursalJasperService.exportToPdf(productoSucursalReports, almacen,  "data","reportProductosSucursal");
    }

    @Override
    public byte[] exportXls(Long almacenId) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

        if(unidadG1.size() > 0 && unidadG1.get(0) != null && unidadG1.get(0).getId() != null){
            params.put("UNIDAD_ID", unidadG1.get(0).getId());
        }

        String almacen = "Todas las sucursales";
        if(almacenId != null && almacenId.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID", almacenId);
            Almacen almacen1 = almacenDAO.listarPorId(almacenId);
            almacen = almacen1 != null ? almacen1.getNombre() : "Sucursal no encontrada";
        }

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        List<InventarioDTO> inventario = productoMapper.listByParameterMapInventario(params);

        List<ProductosSucursalReport> productoSucursalReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        inventario.forEach(productoSucursal -> {
            ProductosSucursalReport productoSucursalReport = new ProductosSucursalReport();
            productoSucursalReport.setNro(nro.get());
            productoSucursalReport.setCodigo(productoSucursal.getProducto().getCodigoUnidad());
            productoSucursalReport.setProducto(productoSucursal.getProducto().getNombre());
            productoSucursalReport.setTipoProducto(productoSucursal.getProducto().getTipoProducto().getTipo());
            productoSucursalReport.setMarca(productoSucursal.getProducto().getMarca().getNombre());
            productoSucursalReport.setCantidad(productoSucursal.getStock().getCantidad());

            productoSucursalReports.add(productoSucursalReport);

            nro.set(nro.get() + 1L);
        });

        return productoSucursalJasperService.exportToXls(productoSucursalReports, almacen, "data", "reportProductosSucursal", "hoja1");
    }
}

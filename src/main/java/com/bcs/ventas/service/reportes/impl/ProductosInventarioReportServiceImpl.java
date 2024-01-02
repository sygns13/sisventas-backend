package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.MarcaDAO;
import com.bcs.ventas.dao.PresentacionDAO;
import com.bcs.ventas.dao.TipoProductoDAO;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.model.dto.InventarioDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.jasper.ProductosInventarioJasperService;
import com.bcs.ventas.service.reportes.ProductosInventarioReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroInventario;
import com.bcs.ventas.utils.reportbeans.ProductosInventarioReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductosInventarioReportServiceImpl implements ProductosInventarioReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private ProductosInventarioJasperService productosInventarioJasperService;

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
    public byte[] exportPdf(FiltroInventario filtros) throws Exception {

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
        /*else{
            params.put("ALMACEN_ID", Constantes.CANTIDAD_ZERO);
        }*/

        String tipoProducto = "Todos";
        if(filtros.getTipoProductoId() != null && filtros.getTipoProductoId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("TIPO_PRODUCTO_ID",filtros.getTipoProductoId());
            TipoProducto tipoProducto1 = tipoProductoDAO.listarPorId(filtros.getTipoProductoId());
            tipoProducto = tipoProducto1 != null ? tipoProducto1.getTipo() : "Tipo de producto no encontrado";
        }
        parametros.put("tipoProducto", tipoProducto);

        String marca = "Todas";
        if(filtros.getMarcaId() != null && filtros.getMarcaId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("MARCA_ID",filtros.getMarcaId());
            Marca marca1 = marcaDAO.listarPorId(filtros.getMarcaId());
            marca = marca1 != null ? marca1.getNombre() : "Marca no encontrada";
        }
        parametros.put("marca", marca);

        String presentacion = "Todas";
        if(filtros.getPresentacionId() != null && filtros.getPresentacionId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("PRESENTACION_ID",filtros.getPresentacionId());
            Presentacion presentacion1 = presentacionDAO.listarPorId(filtros.getPresentacionId());
            presentacion = presentacion1 != null ? presentacion1.getPresentacion() : "Presentacion no encontrada";
        }
        parametros.put("presentacion", presentacion);

        String prioridad = "Todas";
        if(filtros.getPrioridad() != null && filtros.getPrioridad().trim().length() > 0){
            params.put("PRIORIDAD", filtros.getPrioridad());
            switch (filtros.getPrioridad()) {
                case Constantes.PRIORIDAD_ALTA:
                    prioridad = "Alta";
                    break;
                case Constantes.PRIORIDAD_MEDIA:
                    prioridad = "Media";
                    break;
                case Constantes.PRIORIDAD_BAJA:
                    prioridad = "Baja";
                    break;
                default:
                    prioridad = "Sin prioridad";
                    break;
            }
        }
        parametros.put("prioridad", prioridad);

        String codigo = "Todos";
        if(filtros.getCodigo() != null && filtros.getCodigo().trim().length() > 0){
            params.put("CODIGO", "%"+filtros.getCodigo()+"%");
            codigo = filtros.getCodigo();
        }
        parametros.put("codigo", codigo);

        String producto = "Todos";
        if(filtros.getNombre() != null && filtros.getNombre().trim().length() > 0){
            params.put("NOMBRE", "%"+filtros.getNombre()+"%");
            producto = filtros.getNombre();
        }
        parametros.put("producto", producto);

        String composicion = "Todos";
        if(filtros.getComposicion() != null && filtros.getComposicion().trim().length() > 0){
            params.put("COMPOSICION", "%"+filtros.getComposicion()+"%");
            composicion = filtros.getComposicion();
        }
        parametros.put("composicion", composicion);

        String ubicacion = "Todas";
        if(filtros.getUbicacion() != null && filtros.getUbicacion().trim().length() > 0){
            params.put("UBICACION", "%"+filtros.getUbicacion()+"%");
            ubicacion = filtros.getUbicacion();
        }
        parametros.put("ubicacion", ubicacion);

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        List<InventarioDTO> inventario = productoMapper.listByParameterMapInventario(params);

        List<ProductosInventarioReport> productosInventarioReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        inventario.forEach(productosInventario -> {
            ProductosInventarioReport productosInventarioReport = new ProductosInventarioReport();

            productosInventarioReport.setNro(nro.get());
            productosInventarioReport.setCodigo(productosInventario.getProducto().getCodigoUnidad());
            productosInventarioReport.setProducto(productosInventario.getProducto().getNombre());
            productosInventarioReport.setTipoProducto(productosInventario.getProducto().getTipoProducto().getTipo());
            productosInventarioReport.setMarca(productosInventario.getProducto().getMarca().getNombre());
            productosInventarioReport.setPresentacion(productosInventario.getProducto().getPresentacion().getPresentacion());
            productosInventarioReport.setComposicion(productosInventario.getProducto().getComposicion());
            productosInventarioReport.setStockActual(productosInventario.getStock().getCantidad());
            productosInventarioReport.setStockMinimo(productosInventario.getProducto().getStockMinimo());
            productosInventarioReport.setPrecioVenta(productosInventario.getProducto().getPrecioUnidad());
            productosInventarioReport.setCostoCompra(productosInventario.getProducto().getPrecioCompra());

            switch (productosInventario.getProducto().getPrioridad()) {
                case Constantes.PRIORIDAD_ALTA:
                    productosInventarioReport.setPrioridad("Alta");
                    break;
                case Constantes.PRIORIDAD_MEDIA:
                    productosInventarioReport.setPrioridad("Media");
                    break;
                case Constantes.PRIORIDAD_BAJA:
                    productosInventarioReport.setPrioridad("Baja");
                    break;
                default:
                    productosInventarioReport.setPrioridad("Sin prioridad");
                    break;
            }

            productosInventarioReport.setUbicacion(productosInventario.getProducto().getUbicacion());

            switch (productosInventario.getProducto().getAfectoIgv()) {
                case 1:
                    productosInventarioReport.setIgv("Grabado");
                    break;
                case 0:
                    productosInventarioReport.setIgv("Inafecto");
                    break;
                case 2:
                    productosInventarioReport.setIgv("Exonerado");
                    break;
                default:
                    productosInventarioReport.setIgv("Sin afecto");
                    break;
            }

            switch (productosInventario.getProducto().getAfectoIsc()) {
                case 1:
                    productosInventarioReport.setIsc("Si");
                    break;
                case 0:
                    productosInventarioReport.setIsc("No");
                    break;
                default:
                    productosInventarioReport.setIsc("Sin afecto");
                    break;
            }

            switch (productosInventario.getProducto().getActivoLotes()) {
                case 1:
                    productosInventarioReport.setLotes("Si");
                    break;
                case 0:
                    productosInventarioReport.setLotes("No");
                    break;
                default:
                    productosInventarioReport.setLotes("Sin lotes");
                    break;
            }

            productosInventarioReports.add(productosInventarioReport);

            nro.set(nro.get() + 1L);
        });


        return productosInventarioJasperService.exportToPdf(productosInventarioReports,"data","reportProductosInventario", parametros);
    }

    @Override
    public byte[] exportXls(FiltroInventario filtros) throws Exception {

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
        /*else{
            params.put("ALMACEN_ID", Constantes.CANTIDAD_ZERO);
        }*/

        String tipoProducto = "Todos";
        if(filtros.getTipoProductoId() != null && filtros.getTipoProductoId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("TIPO_PRODUCTO_ID",filtros.getTipoProductoId());
            TipoProducto tipoProducto1 = tipoProductoDAO.listarPorId(filtros.getTipoProductoId());
            tipoProducto = tipoProducto1 != null ? tipoProducto1.getTipo() : "Tipo de producto no encontrado";
        }
        parametros.put("tipoProducto", tipoProducto);

        String marca = "Todas";
        if(filtros.getMarcaId() != null && filtros.getMarcaId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("MARCA_ID",filtros.getMarcaId());
            Marca marca1 = marcaDAO.listarPorId(filtros.getMarcaId());
            marca = marca1 != null ? marca1.getNombre() : "Marca no encontrada";
        }
        parametros.put("marca", marca);

        String presentacion = "Todas";
        if(filtros.getPresentacionId() != null && filtros.getPresentacionId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("PRESENTACION_ID",filtros.getPresentacionId());
            Presentacion presentacion1 = presentacionDAO.listarPorId(filtros.getPresentacionId());
            presentacion = presentacion1 != null ? presentacion1.getPresentacion() : "Presentacion no encontrada";
        }
        parametros.put("presentacion", presentacion);

        String prioridad = "Todas";
        if(filtros.getPrioridad() != null && filtros.getPrioridad().trim().length() > 0){
            params.put("PRIORIDAD", filtros.getPrioridad());
            switch (filtros.getPrioridad()) {
                case Constantes.PRIORIDAD_ALTA:
                    prioridad = "Alta";
                    break;
                case Constantes.PRIORIDAD_MEDIA:
                    prioridad = "Media";
                    break;
                case Constantes.PRIORIDAD_BAJA:
                    prioridad = "Baja";
                    break;
                default:
                    prioridad = "Sin prioridad";
                    break;
            }
        }
        parametros.put("prioridad", prioridad);

        String codigo = "Todos";
        if(filtros.getCodigo() != null && filtros.getCodigo().trim().length() > 0){
            params.put("CODIGO", "%"+filtros.getCodigo()+"%");
            codigo = filtros.getCodigo();
        }
        parametros.put("codigo", codigo);

        String producto = "Todos";
        if(filtros.getNombre() != null && filtros.getNombre().trim().length() > 0){
            params.put("NOMBRE", "%"+filtros.getNombre()+"%");
            producto = filtros.getNombre();
        }
        parametros.put("producto", producto);

        String composicion = "Todos";
        if(filtros.getComposicion() != null && filtros.getComposicion().trim().length() > 0){
            params.put("COMPOSICION", "%"+filtros.getComposicion()+"%");
            composicion = filtros.getComposicion();
        }
        parametros.put("composicion", composicion);

        String ubicacion = "Todas";
        if(filtros.getUbicacion() != null && filtros.getUbicacion().trim().length() > 0){
            params.put("UBICACION", "%"+filtros.getUbicacion()+"%");
            ubicacion = filtros.getUbicacion();
        }
        parametros.put("ubicacion", ubicacion);

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        List<InventarioDTO> inventario = productoMapper.listByParameterMapInventario(params);

        List<ProductosInventarioReport> productosInventarioReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        inventario.forEach(productosInventario -> {
            ProductosInventarioReport productosInventarioReport = new ProductosInventarioReport();

            productosInventarioReport.setNro(nro.get());
            productosInventarioReport.setCodigo(productosInventario.getProducto().getCodigoUnidad());
            productosInventarioReport.setProducto(productosInventario.getProducto().getNombre());
            productosInventarioReport.setTipoProducto(productosInventario.getProducto().getTipoProducto().getTipo());
            productosInventarioReport.setMarca(productosInventario.getProducto().getMarca().getNombre());
            productosInventarioReport.setPresentacion(productosInventario.getProducto().getPresentacion().getPresentacion());
            productosInventarioReport.setComposicion(productosInventario.getProducto().getComposicion());
            productosInventarioReport.setStockActual(productosInventario.getStock().getCantidad());
            productosInventarioReport.setStockMinimo(productosInventario.getProducto().getStockMinimo());
            productosInventarioReport.setPrecioVenta(productosInventario.getProducto().getPrecioUnidad());
            productosInventarioReport.setCostoCompra(productosInventario.getProducto().getPrecioCompra());

            switch (productosInventario.getProducto().getPrioridad()) {
                case Constantes.PRIORIDAD_ALTA:
                    productosInventarioReport.setPrioridad("Alta");
                    break;
                case Constantes.PRIORIDAD_MEDIA:
                    productosInventarioReport.setPrioridad("Media");
                    break;
                case Constantes.PRIORIDAD_BAJA:
                    productosInventarioReport.setPrioridad("Baja");
                    break;
                default:
                    productosInventarioReport.setPrioridad("Sin prioridad");
                    break;
            }

            productosInventarioReport.setUbicacion(productosInventario.getProducto().getUbicacion());

            switch (productosInventario.getProducto().getAfectoIgv()) {
                case 1:
                    productosInventarioReport.setIgv("Grabado");
                    break;
                case 0:
                    productosInventarioReport.setIgv("Inafecto");
                    break;
                case 2:
                    productosInventarioReport.setIgv("Exonerado");
                    break;
                default:
                    productosInventarioReport.setIgv("Sin afecto");
                    break;
            }

            switch (productosInventario.getProducto().getAfectoIsc()) {
                case 1:
                    productosInventarioReport.setIsc("Si");
                    break;
                case 0:
                    productosInventarioReport.setIsc("No");
                    break;
                default:
                    productosInventarioReport.setIsc("Sin afecto");
                    break;
            }

            switch (productosInventario.getProducto().getActivoLotes()) {
                case 1:
                    productosInventarioReport.setLotes("Si");
                    break;
                case 0:
                    productosInventarioReport.setLotes("No");
                    break;
                default:
                    productosInventarioReport.setLotes("Sin lotes");
                    break;
            }

            productosInventarioReports.add(productosInventarioReport);

            nro.set(nro.get() + 1L);
        });


        return productosInventarioJasperService.exportToXls(productosInventarioReports,"data","reportProductosInventario", "hoja1", parametros);
    }
}

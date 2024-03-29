package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.*;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.dto.InventarioDTO;
import com.bcs.ventas.model.dto.ProductoBajoStockDTO;
import com.bcs.ventas.model.dto.ProductoVencidoDTO;
import com.bcs.ventas.model.dto.ProductosVentaDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.ProductoService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.beans.FiltroInventario;
import com.bcs.ventas.utils.beans.FiltroProductosVenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoDAO productoDAO;

    @Autowired
    private TipoProductoDAO tipoProductoDAO;

    @Autowired
    private MarcaDAO marcaDAO;

    @Autowired
    private DetalleUnidadProductoDAO detalleUnidadProductoDAO;

    @Autowired
    private PresentacionDAO presentacionDAO;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private TipoProductoMapper tipoProductoMapper;

    @Autowired
    private MarcaMapper marcaMapper;

    @Autowired
    private PresentacionMapper presentacionMapper;

    @Autowired
    private UnidadMapper unidadMapper;

    @Autowired
    private DetalleUnidadProductoMapper detalleUnidadProductoMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private UnidadDAO unidadDAO;


    @Autowired
    private ClaimsAuthorization claimsAuthorization;


    @Override
    public Producto registrar(Producto p) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        p.setCreatedAt(fechaActualTime);
        p.setUpdatedAd(fechaActualTime);
        p.setFecha(fechaActual);

        //Oauth inicio
        p.setEmpresaId(claimsAuthorization.getEmpresaId());
        p.setUserId(claimsAuthorization.getUserId());
        //Oauth final

        p.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        p.setActivo(Constantes.REGISTRO_ACTIVO);

        Map<String, Object> params = new HashMap<String, Object>();

        if(p.getTipoProducto() == null || p.getTipoProducto().getId() == null || p.getTipoProducto().getId() == 0L){
            params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO_2_DEFAULT);
            params.put("EMPRESA_ID",p.getEmpresaId());

            List<TipoProducto> tipoProductos = tipoProductoMapper.listByParameterMap(params);
            if(tipoProductos.size() > 0)
                p.setTipoProducto(tipoProductos.get(0));
        }
        params.clear();

        if(p.getMarca() == null || p.getMarca().getId() == null || p.getMarca().getId() == 0L){
            params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO_2_DEFAULT);
            params.put("EMPRESA_ID",p.getEmpresaId());

            List<Marca> marcas = marcaMapper.listByParameterMap(params);
            if(marcas.size() > 0)
                p.setMarca(marcas.get(0));
        }
        params.clear();

        if(p.getPresentacion() == null || p.getPresentacion().getId() == null || p.getPresentacion().getId() == 0L) {
            params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO_2_DEFAULT);
            params.put("EMPRESA_ID",p.getEmpresaId());

            List<Presentacion> presentacions = presentacionMapper.listByParameterMap(params);
            if(presentacions.size() > 0)
                p.setPresentacion(presentacions.get(0));
        }
        params.clear();



        if(p.getNombre() == null)    p.setNombre("");
        if(p.getCodigoUnidad() == null)    p.setCodigoUnidad("");
        if(p.getComposicion() == null) p.setComposicion("");
        if(p.getStockMinimo() == null) p.setStockMinimo(Constantes.CANTIDAD_ZERO_DOUBLE);
        if(p.getPrioridad() == null) p.setPrioridad("");
        if(p.getUbicacion() == null) p.setUbicacion("");
        if(p.getActivoLotes() == null) p.setActivoLotes(Constantes.CANTIDAD_ZERO);
        if(p.getAfectoIgv() == null) p.setAfectoIgv(Constantes.REGISTRO_ACTIVO);
        if(p.getAfectoIsc() == null) p.setAfectoIsc(Constantes.REGISTRO_INACTIVO);
        if(p.getAfectoIsc().intValue() == Constantes.REGISTRO_INACTIVO.intValue()) {
            p.setTipoTasaIsc(Constantes.REGISTRO_INACTIVO);
            p.setTasaIsc(Constantes.CANTIDAD_ZERO_DOUBLE);
        }

        p.setCodigoProducto(p.getCodigoUnidad());


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(p, resultValidacion);

        if(validacion)
            return this.grabarRegistro(p);

        String errorValidacion = "Error de validación Método Registrar Producto";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public int modificar(Producto p) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        p.setUpdatedAd(fechaActual);

        //Oauth inicio
        p.setEmpresaId(claimsAuthorization.getEmpresaId());
        p.setUserId(claimsAuthorization.getUserId());
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();

        if(p.getTipoProducto() == null || p.getTipoProducto().getId() == null){
            params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO_2_DEFAULT);
            params.put("EMPRESA_ID",p.getEmpresaId());

            List<TipoProducto> tipoProductos = tipoProductoMapper.listByParameterMap(params);
            if(tipoProductos.size() > 0)
                p.setTipoProducto(tipoProductos.get(0));
        }
        params.clear();

        if(p.getMarca() == null || p.getMarca().getId() == null){
            params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO_2_DEFAULT);
            params.put("EMPRESA_ID",p.getEmpresaId());

            List<Marca> marcas = marcaMapper.listByParameterMap(params);
            if(marcas.size() > 0)
                p.setMarca(marcas.get(0));
        }
        params.clear();

        if(p.getPresentacion() == null || p.getPresentacion().getId() == null){
            params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO_2_DEFAULT);
            params.put("EMPRESA_ID",p.getEmpresaId());

            List<Presentacion> presentacions = presentacionMapper.listByParameterMap(params);
            if(presentacions.size() > 0)
                p.setPresentacion(presentacions.get(0));
        }
        params.clear();



        if(p.getNombre() == null)    p.setNombre("");
        if(p.getCodigoUnidad() == null)    p.setCodigoUnidad("");
        if(p.getComposicion() == null) p.setComposicion("");
        if(p.getStockMinimo() == null) p.setStockMinimo(Constantes.CANTIDAD_ZERO_DOUBLE);
        if(p.getPrioridad() == null) p.setPrioridad("");
        if(p.getUbicacion() == null) p.setUbicacion("");
        if(p.getActivoLotes() == null) p.setActivoLotes(Constantes.CANTIDAD_ZERO);
        if(p.getAfectoIgv() == null) p.setAfectoIgv(Constantes.REGISTRO_ACTIVO);
        if(p.getAfectoIsc() == null) p.setAfectoIsc(Constantes.REGISTRO_INACTIVO);
        if(p.getAfectoIsc().intValue() == Constantes.REGISTRO_INACTIVO.intValue()) {
            p.setTipoTasaIsc(Constantes.REGISTRO_INACTIVO);
            p.setTasaIsc(Constantes.CANTIDAD_ZERO_DOUBLE);
        }

        p.setCodigoProducto(p.getCodigoUnidad());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(p, resultValidacion);

        if(validacion)
            return this.grabarRectificar(p);

        String errorValidacion = "Error de validación Método Modificar Producto";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<Producto> listar() throws Exception {
        //return productoMapper.getAllEntities();
        //return productoDAO.listar();
        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);
        return productoMapper.listByParameterMap(params);
    }

    public Page<Producto> listar(Pageable page, String buscar) throws Exception {
        //return bancoDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = productoMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Producto> productos = productoMapper.listByParameterMap(params);

        return new PageImpl<>(productos, page, total);
    }

    @Override
    public Page<InventarioDTO> getInventario(Pageable page, FiltroInventario filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        // params.put("BUSCAR","%"+buscar+"%");

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

        if(unidadG1.size() > 0 && unidadG1.get(0) != null && unidadG1.get(0).getId() != null){
            params.put("UNIDAD_ID", unidadG1.get(0).getId());
        }

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",filtros.getAlmacenId());
        }
        /*else{
            params.put("ALMACEN_ID", Constantes.CANTIDAD_ZERO);
        }*/

        if(filtros.getTipoProductoId() != null && filtros.getTipoProductoId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("TIPO_PRODUCTO_ID",filtros.getTipoProductoId());
        }

        if(filtros.getMarcaId() != null && filtros.getMarcaId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("MARCA_ID",filtros.getMarcaId());
        }

        if(filtros.getPresentacionId() != null && filtros.getPresentacionId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("PRESENTACION_ID",filtros.getPresentacionId());
        }

        if(filtros.getPrioridad() != null && filtros.getPrioridad().trim().length() > 0){
            params.put("PRIORIDAD", filtros.getPrioridad());
        }

        if(filtros.getCodigo() != null && filtros.getCodigo().trim().length() > 0){
            params.put("CODIGO", "%"+filtros.getCodigo()+"%");
        }

        if(filtros.getNombre() != null && filtros.getNombre().trim().length() > 0){
            params.put("NOMBRE", "%"+filtros.getNombre()+"%");
        }

        if(filtros.getComposicion() != null && filtros.getComposicion().trim().length() > 0){
            params.put("COMPOSICION", "%"+filtros.getComposicion()+"%");
        }

        if(filtros.getUbicacion() != null && filtros.getUbicacion().trim().length() > 0){
            params.put("UBICACION", "%"+filtros.getUbicacion()+"%");
        }

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        Long total = productoMapper.getTotalElementsInventario(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<InventarioDTO> inventario = productoMapper.listByParameterMapInventario(params);

        return new PageImpl<>(inventario, page, total);

    }

    @Override
    public Page<ProductosVentaDTO> ProductosPrecioReport(Pageable page, Long almacenId, Long unidadId) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID", EmpresaId);
        //params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);
        params.put("UNIDAD_ID", unidadId);
        params.put("ALMACEN_ID", almacenId);

        Long total = productoMapper.getTotalElementsProductosVenta(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

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


        return new PageImpl<>(productosVenta, page, total);
    }

    @Override
    public Page<InventarioDTO> getProductosGestionLotes(Pageable page, FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        // params.put("BUSCAR","%"+buscar+"%");

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

        if(unidadG1.size() > 0 && unidadG1.get(0) != null && unidadG1.get(0).getId() != null){
            params.put("UNIDAD_ID", unidadG1.get(0).getId());
        }

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",filtros.getAlmacenId());
        }

        if(filtros.getPalabraClave() != null && !filtros.getPalabraClave().isEmpty()){
            params.put("BUSCAR","%"+filtros.getPalabraClave()+"%");
        }


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        Long total = productoMapper.getTotalElementsInventario(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<InventarioDTO> inventario = productoMapper.listByParameterMapInventario(params);

        return new PageImpl<>(inventario, page, total);

    }

    @Override
    public Page<ProductoBajoStockDTO> getProductosBajoStock(Pageable page, FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        // params.put("BUSCAR","%"+buscar+"%");

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

        if(unidadG1.size() > 0 && unidadG1.get(0) != null && unidadG1.get(0).getId() != null){
            params.put("UNIDAD_ID", unidadG1.get(0).getId());
        }

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",filtros.getAlmacenId());
        }

        if(filtros.getPalabraClave() != null && !filtros.getPalabraClave().isEmpty()){
            params.put("BUSCAR","%"+filtros.getPalabraClave()+"%");
        }


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        Long total = productoMapper.getTotalElementsBajoStock(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<ProductoBajoStockDTO> productosBajoStock = productoMapper.listByParameterMapProductosBajoStock(params);

        return new PageImpl<>(productosBajoStock, page, total);
    }


    @Override
    public Page<ProductoVencidoDTO> getProductosVencidos(Pageable page, FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

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

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",filtros.getAlmacenId());
        }

        if(filtros.getPalabraClave() != null && !filtros.getPalabraClave().isEmpty()){
            params.put("BUSCAR","%"+filtros.getPalabraClave()+"%");
        }

        if(filtros.getTipo() != null){
            params.put("TIPO",filtros.getTipo());
        }

        if(filtros.getFechaInicio() != null){
            params.put("FECHA_INICIO", filtros.getFechaInicio().toString());
        }

        if(filtros.getFechaFinal() != null){
            params.put("FECHA_FINAL", filtros.getFechaFinal().toString());
        }


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        Long total = productoMapper.getTotalElementsProductosVencidos(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<ProductoVencidoDTO> productosVencidos = productoMapper.listByParameterMapProductosVencidos(params);

        return new PageImpl<>(productosVencidos, page, total);
    }

    @Override
    public Page<ProductosVentaDTO> getProductosVentas(Pageable page, FiltroProductosVenta filtros) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        // params.put("BUSCAR","%"+buscar+"%");

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID", EmpresaId);
        //params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);
        params.put("UNIDAD_ID", filtros.getUnidadId());
        params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getPalabraClave() != null && !filtros.getPalabraClave().isEmpty()){
            params.put("BUSCAR","%"+filtros.getPalabraClave()+"%");
        }

        Long total = productoMapper.getTotalElementsProductosVenta(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<ProductosVentaDTO> productosVenta = productoMapper.listByParameterMapProductosVenta(params);

        productosVenta.forEach((p)-> {

            Map<String, Object> params1 = new HashMap<String, Object>();

            params1.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
            params1.put("EMPRESA_ID", EmpresaId);
            params1.put("PRODUCTO_ID",p.getProducto().getId());
            params1.put("PRODUCTO_PU",p.getProducto().getPrecioUnidad());
            params1.put("PRODUCTO_PC",p.getProducto().getPrecioCompra());
            params1.put("UNIDAD_ID", filtros.getUnidadId());
            params1.put("ALMACEN_ID", filtros.getAlmacenId());

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


        return new PageImpl<>(productosVenta, page, total);
    }

    @Override
    public Producto listarPorId(Long id) throws Exception {
       // Map<String, Object> params = new HashMap<String, Object>();
        //params.put("ID",id);
        //return productoMapper.listByParameterMap(params).get(0);
        //return productoDAO.listarPorId(id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Producto> productos = productoMapper.listByParameterMap(params);

        if(productos.size() > 0)
            return productos.get(0);
        else
            return null;
    }

    @Override
    public void eliminar(Long id) throws Exception {
       // productoDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else {
            String errorValidacion = "Error de validación Método Eliminar producto";

            if (resultValidacion.get("errors") != null) {
                List<String> errors = (List<String>) resultValidacion.get("errors");
                if (errors.size() > 0)
                    errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
            }

            throw new ValidationServiceException(errorValidacion);
        }
    }



    //Métodos de Grabado

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public Producto grabarRegistro(Producto p) throws Exception {

        Producto producto = productoDAO.registrar(p);

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",producto.getEmpresaId());
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);

        DetalleUnidadProducto detalleUnidad = new  DetalleUnidadProducto();

        detalleUnidad.setProductoId(producto.getId());
        detalleUnidad.setUnidad(unidadG1.get(0));
        detalleUnidad.setCodigoUnidad(producto.getCodigoUnidad());
        detalleUnidad.setPrecio(producto.getPrecioUnidad());
        detalleUnidad.setCostoCompra(producto.getPrecioCompra());
        detalleUnidad.setUserId(producto.getUserId());
        detalleUnidad.setEmpresaId(producto.getEmpresaId());
        detalleUnidad.setCreatedAt(producto.getCreatedAt());
        detalleUnidad.setUpdatedAd(producto.getUpdatedAd());
        detalleUnidad.setAlmacenId(Constantes.ALMACEN_GENERAL);

        detalleUnidad.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        detalleUnidad.setActivo(Constantes.REGISTRO_ACTIVO);

         detalleUnidadProductoDAO.registrar(detalleUnidad);

        return producto;

    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Producto p) throws Exception {
        //Producto producto = productoDAO.modificar(p);

        //Grabar Rectificacion Producto

        BigDecimal stockMinimo = new BigDecimal(p.getStockMinimo());
        stockMinimo.setScale(2, RoundingMode.HALF_UP);

        BigDecimal precioUnidad = p.getPrecioUnidad();
        precioUnidad.setScale(2, RoundingMode.HALF_UP);

        BigDecimal precioCompra = p.getPrecioCompra();
        precioCompra.setScale(2, RoundingMode.HALF_UP);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", p.getId());
        params.put("NOMBRE", p.getNombre());
        params.put("TIPO_PRODUCTO_ID", p.getTipoProducto().getId());
        params.put("MARCA_ID", p.getMarca().getId());
        params.put("STOCK_MINIMO", stockMinimo);
        params.put("PRECIO_UNIDAD", precioUnidad);
        params.put("PRECIO_COMPRA", precioCompra);
        params.put("FECHA", p.getFecha());
        params.put("CODIGO_UNIDAD", p.getCodigoUnidad());
        params.put("CODIGO_PRODUCTO", p.getCodigoProducto());
        params.put("PRESENTACION_ID", p.getPresentacion().getId());
        params.put("COMPOSICION", p.getComposicion());
        params.put("PRIORIDAD", p.getPrioridad());
        params.put("UBICACION", p.getUbicacion());
        params.put("ACTIVO_LOTES", p.getActivoLotes());
        params.put("AFECTO_ISC", p.getAfectoIsc());
        params.put("TIPO_TASA_ISC", p.getTipoTasaIsc());
        params.put("TASA_ISC", p.getTasaIsc());
        params.put("AFECTO_IGV", p.getAfectoIgv());
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", p.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int resultado = productoMapper.updateByPrimaryKeySelective(params);

        //Grabar Unidades
        params.clear();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);

        params.clear();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("PRODUCTO_ID",p.getId());
        params.put("UNIDAD_ID", unidadG1.get(0).getId());
        params.put("ALMACEN_ID", Constantes.ALMACEN_GENERAL);

        List<DetalleUnidadProducto> detalleUnidadBD = detalleUnidadProductoMapper.listByParameterMap(params);
        DetalleUnidadProducto detalleUnidad = new  DetalleUnidadProducto();

        if(detalleUnidadBD.size() > 0){

            detalleUnidad = detalleUnidadBD.get(0);

            params.clear();
            params.put("ID", detalleUnidad.getId());
            params.put("CODIGO_UNIDAD", p.getCodigoUnidad());
            params.put("PRECIO", p.getPrecioUnidad());
            params.put("COSTO_COMPRA", p.getPrecioCompra());
            params.put("USER_ID", claimsAuthorization.getUserId());
            params.put("UPDATED_AT", p.getUpdatedAd());

            params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
            int resultado2 = detalleUnidadProductoMapper.updateByPrimaryKeySelective(params);
        }

        else{
            detalleUnidad.setProductoId(p.getId());
            detalleUnidad.setUnidad(unidadG1.get(0));
            detalleUnidad.setCodigoUnidad(p.getCodigoUnidad());
            detalleUnidad.setPrecio(p.getPrecioUnidad());
            detalleUnidad.setCostoCompra(p.getPrecioCompra());
            detalleUnidad.setUserId(claimsAuthorization.getUserId());
            detalleUnidad.setEmpresaId(p.getEmpresaId());
            detalleUnidad.setCreatedAt(p.getCreatedAt());
            detalleUnidad.setUpdatedAd(p.getUpdatedAd());
            detalleUnidad.setAlmacenId(Constantes.ALMACEN_GENERAL);

            detalleUnidad.setBorrado(Constantes.REGISTRO_NO_BORRADO);
            detalleUnidad.setActivo(Constantes.REGISTRO_ACTIVO);

            detalleUnidadProductoDAO.registrar(detalleUnidad);
        }


        return resultado;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void grabarEliminar(Long id) {

        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT",fechaUpdate);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int res= productoMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Producto indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //Métodos de Validación

    @Override
    public boolean validacionRegistro(Producto p,  Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(p.getAfectoIsc().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            if(p.getTipoTasaIsc() == null){
                resultado = false;
                error = "Ingrese el Tipo de Tasa de ISC";
                errors.add(error);
            }
            if(p.getTasaIsc() == null || p.getTasaIsc() <= Constantes.CANTIDAD_ZERO_DOUBLE){
                resultado = false;
                error = "Ingrese la Tasa del ISC";
                errors.add(error);
            }

            if(p.getTipoTasaIsc() != null && p.getTipoTasaIsc().intValue() == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                if(p.getTasaIsc() != null && ( p.getTasaIsc() < Constantes.CANTIDAD_ZERO_DOUBLE ||
                        p.getTasaIsc() > Constantes.CANTIDAD_CIEN_DOUBLE)){
                    resultado = false;
                    error = "Si la Tasa de ISC es porcentual, el valor no puede ser menor a cero o mayor a 100";
                    errors.add(error);
                }
            }
        }
        else{
            p.setTasaIsc(null);
            p.setTipoTasaIsc(null);
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",p.getNombre());
        params.put("TIPO_PRODUCTO_ID",p.getTipoProducto().getId());
        params.put("MARCA_ID",p.getMarca().getId());
        params.put("PRESENTACION_ID",p.getPresentacion().getId());
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Producto> productosV1 = productoMapper.listByParameterMap(params);

        if(productosV1.size() > 0){
            resultado = false;
            error = "El nombre del Producto ingresado ya se encuentra registrado para el tipo de producto, marca y presentación seleccionado";
            errors.add(error);
        }
        params.clear();

        params.put("CODIGO_UNIDAD",p.getCodigoUnidad());
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Producto> productosV2 = productoMapper.listByParameterMap(params);
        boolean auxValidateCodigo = false;

        if(productosV2.size() > 0){
            resultado = false;
            error = "El código ingresado ya se encuentra registrado";
            errors.add(error);
            auxValidateCodigo = true;
        }

        params.clear();

        if(!auxValidateCodigo){
            params.put("CODIGO_UNIDAD",p.getCodigoUnidad());
            params.put("EMPRESA_ID",p.getEmpresaId());
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

            List<DetalleUnidadProducto> detalleUnidadProductosV3 = detalleUnidadProductoMapper.listByParameterMap(params);

            if(detalleUnidadProductosV3.size() > 0){
                resultado = false;
                error = "El código ingresado ya se encuentra registrado";
                errors.add(error);
            }
        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Producto p , Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(p.getAfectoIsc().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            if(p.getTipoTasaIsc() == null){
                resultado = false;
                error = "Ingrese el Tipo de Tasa de ISC";
                errors.add(error);
            }
            if(p.getTasaIsc() == null || p.getTasaIsc() <= Constantes.CANTIDAD_ZERO_DOUBLE){
                resultado = false;
                error = "Ingrese la Tasa del ISC";
                errors.add(error);
            }

            if(p.getTipoTasaIsc() != null && p.getTipoTasaIsc().intValue() == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                if(p.getTasaIsc() != null && ( p.getTasaIsc() < Constantes.CANTIDAD_ZERO_DOUBLE ||
                        p.getTasaIsc() > Constantes.CANTIDAD_CIEN_DOUBLE)){
                    resultado = false;
                    error = "Si la Tasa de ISC es porcentual, el valor no puede ser menor a cero o mayor a 100";
                    errors.add(error);
                }
            }
        }
        else{
            p.setTasaIsc(null);
            p.setTipoTasaIsc(null);
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_ID",p.getId());
        params.put("NOMBRE",p.getNombre());
        params.put("TIPO_PRODUCTO_ID",p.getTipoProducto().getId());
        params.put("MARCA_ID",p.getMarca().getId());
        params.put("PRESENTACION_ID",p.getPresentacion().getId());
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Producto> productosV1 = productoMapper.listByParameterMap(params);

        if(productosV1.size() > 0){
            resultado = false;
            error = "El nombre del Producto ingresado ya se encuentra registrado para el tipo de producto, marca y presentación seleccionado";
            errors.add(error);
        }
        params.clear();

        params.put("NO_ID",p.getId());
        params.put("CODIGO_UNIDAD",p.getCodigoUnidad());
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Producto> productosV2 = productoMapper.listByParameterMap(params);
        boolean auxValidateCodigo = false;

        if(productosV2.size() > 0){
            resultado = false;
            error = "El código ingresado ya se encuentra registrado";
            errors.add(error);
            auxValidateCodigo = true;
        }

        params.clear();

        if(!auxValidateCodigo){
            params.put("NO_PRODUCTO_ID",p.getId());
            params.put("CODIGO_UNIDAD",p.getCodigoUnidad());
            params.put("EMPRESA_ID",p.getEmpresaId());
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

            List<DetalleUnidadProducto> detalleUnidadProductosV3 = detalleUnidadProductoMapper.listByParameterMap(params);

            if(detalleUnidadProductosV3.size() > 0){
                resultado = false;
                error = "El código ingresado ya se encuentra registrado";
                errors.add(error);
            }
            params.clear();
        }

        Producto productoBD = productoDAO.listarPorId(p.getId());

        if(p.getActivoLotes().intValue() != productoBD.getActivoLotes().intValue()){
            params.put("PRODUCTO_ID",p.getId());
            params.put("EMPRESA_ID",p.getEmpresaId());
            params.put("CANTIDAD_MAS",Constantes.CANTIDAD_ZERO);
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

            List<Stock> stocksV4 = stockMapper.listByParameterMap(params);

            if(stocksV4.size() > 0){
                resultado = false;
                error = "No puede modificar la opción de \"Trabajo con Lotes\" Ya que cuenta con stocks registrados, primero debe vaciar los stocks dejándolos en cero";
                errors.add(error);
            }
            params.clear();

        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;

    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //Lógica de Validaciones para Eliminación Producto

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public List<TipoProducto> getTipoProductos() throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("NO_ACTIVO",Constantes.REGISTRO_INACTIVO);
        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        return productoMapper.getTipoProducto(params);
    }

    @Override
    public List<Marca> getMarcas() throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("NO_ACTIVO",Constantes.REGISTRO_INACTIVO);
        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        return productoMapper.getMarca(params);
    }

    @Override
    public List<Presentacion> getPresentaciones() throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("NO_ACTIVO",Constantes.REGISTRO_INACTIVO);
        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        return productoMapper.getPresentacion(params);
    }
}

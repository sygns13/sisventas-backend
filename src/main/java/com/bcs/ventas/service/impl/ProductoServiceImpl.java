package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.*;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.ProductoService;
import com.bcs.ventas.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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





    @Override
    public Producto registrar(Producto p) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        p.setCreatedAt(fechaActual);
        p.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        p.setEmpresaId(1L);
        p.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        p.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        p.setActivo(Constantes.REGISTRO_ACTIVO);

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

        //TODO: Temporal hasta incluir Oauth inicio
        p.setEmpresaId(1L);
        p.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

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
        return productoDAO.listar();
        /*Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        return productoMapper.listByParameterMap(params);*/
    }

    @Override
    public Producto listarPorId(Long id) throws Exception {
       // Map<String, Object> params = new HashMap<String, Object>();
        //params.put("ID",id);
        //return productoMapper.listByParameterMap(params).get(0);
        //return productoDAO.listarPorId(id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
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



    //TODO: Métodos de Grabado

    @Transactional
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
        detalleUnidad.setUnidadId(unidadG1.get(0).getId());
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

    @Transactional
    @Override
    public int grabarRectificar(Producto p) throws Exception {
        //Producto producto = productoDAO.modificar(p);

        //Grabar Rectificacion Producto

        BigDecimal stockMinimo = new BigDecimal(p.getStockMinimo());
        stockMinimo.setScale(2, RoundingMode.HALF_UP);

        BigDecimal precioUnidad = new BigDecimal(p.getPrecioUnidad());
        precioUnidad.setScale(2, RoundingMode.HALF_UP);

        BigDecimal precioCompra = new BigDecimal(p.getPrecioCompra());
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
        params.put("USER_ID", p.getUserId());
        params.put("UPDATED_AT", p.getUpdatedAd());

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

            detalleUnidad.setCodigoUnidad(p.getCodigoUnidad());
            detalleUnidad.setPrecio(p.getPrecioUnidad());
            detalleUnidad.setCostoCompra(p.getPrecioCompra());
            detalleUnidad.setUserId(p.getUserId());

            detalleUnidadProductoDAO.modificar(detalleUnidad);
        }

        else{
            detalleUnidad.setProductoId(p.getId());
            detalleUnidad.setUnidadId(unidadG1.get(0).getId());
            detalleUnidad.setCodigoUnidad(p.getCodigoUnidad());
            detalleUnidad.setPrecio(p.getPrecioUnidad());
            detalleUnidad.setCostoCompra(p.getPrecioCompra());
            detalleUnidad.setUserId(p.getUserId());
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

    @Transactional
    @Override
    public void grabarEliminar(Long id) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);


        int res= productoMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Producto indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //TODO: Métodos de Validación

    @Override
    public boolean validacionRegistro(Producto p,  Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(p.getAfectoIsc().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            if(p.getTipoTasaIsc() == null){
                error = "Ingrese el Tipo de Tasa de ISC";
                errors.add(error);
            }
            if(p.getTasaIsc() == null || p.getTasaIsc() <= Constantes.CANTIDAD_ZERO_DOUBLE){
                error = "Ingrese la Tasa del ISC";
                errors.add(error);
            }

            if(p.getTipoTasaIsc().intValue() == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                if(p.getTasaIsc() < Constantes.CANTIDAD_ZERO_DOUBLE ||
                        p.getTasaIsc() > Constantes.CANTIDAD_CIEN_DOUBLE){
                    error = "Si la Tasa de ISC es porcentual, el valor no puede ser menor a cero o mayor a 100";
                    errors.add(error);
                }
            }
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

        if(productosV2.size() > 0){
            resultado = false;
            error = "El código ingresado ya se encuentra registrado";
            errors.add(error);
        }

        params.clear();

        params.put("CODIGO_UNIDAD",p.getCodigoUnidad());
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<DetalleUnidadProducto> detalleUnidadProductosV3 = detalleUnidadProductoMapper.listByParameterMap(params);

        if(detalleUnidadProductosV3.size() > 0){
            resultado = false;
            error = "El código ingresado ya se encuentra registrado";
            errors.add(error);
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
                error = "Ingrese el Tipo de Tasa de ISC";
                errors.add(error);
            }
            if(p.getTasaIsc() == null || p.getTasaIsc() <= Constantes.CANTIDAD_ZERO_DOUBLE){
                error = "Ingrese la Tasa del ISC";
                errors.add(error);
            }

            if(p.getTipoTasaIsc().intValue() == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                if(p.getTasaIsc() < Constantes.CANTIDAD_ZERO_DOUBLE ||
                        p.getTasaIsc() > Constantes.CANTIDAD_CIEN_DOUBLE){
                    error = "Si la Tasa de ISC es porcentual, el valor no puede ser menor a cero o mayor a 100";
                    errors.add(error);
                }
            }
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

        if(productosV2.size() > 0){
            resultado = false;
            error = "El código ingresado ya se encuentra registrado";
            errors.add(error);
        }

        params.clear();

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
}

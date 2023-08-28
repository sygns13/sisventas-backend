package com.bcs.ventas.service.impl;


import com.bcs.ventas.dao.DetalleUnidadProductoDAO;
import com.bcs.ventas.dao.ProductoDAO;
import com.bcs.ventas.dao.UnidadDAO;
import com.bcs.ventas.dao.mappers.AlmacenMapper;
import com.bcs.ventas.dao.mappers.DetalleUnidadProductoMapper;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.DetalleUnidadProducto;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.Unidad;
import com.bcs.ventas.service.DetalleUnidadProductoService;
import com.bcs.ventas.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class DetalleUnidadProductoServiceImpl implements DetalleUnidadProductoService {

    @Autowired
    private AlmacenMapper almacenMapper;

    @Autowired
    private DetalleUnidadProductoMapper detalleUnidadProductoMapper;

    @Autowired
    private ProductoDAO productoDAO;

    @Autowired
    private UnidadMapper unidadMapper;

    @Autowired
    private UnidadDAO unidadDAO;

    @Autowired
    private DetalleUnidadProductoDAO detalleUnidadProductoDAO;

    @Autowired
    private ProductoMapper productoMapper;


    @Override
    public List<Almacen> getAlmacens(Long idEmpresa) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", idEmpresa);
        return almacenMapper.listByParameterMapOrderId(params);
    }

    @Override
    public List<DetalleUnidadProducto> listar(Long idAlmacen, Long idProducto, Long idEmpresa) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        Producto producto = productoDAO.listarPorId(idProducto);

        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO", Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID", idEmpresa);
        params.put("PRODUCTO_ID", idProducto);
        params.put("ALMACEN_ID", idAlmacen);
        params.put("PRODUCTO", producto);

        return detalleUnidadProductoMapper.listByParameterMapBaseUnidad(params);
    }

    @Override
    public DetalleUnidadProducto registrar(DetalleUnidadProducto detalleUnidadProducto) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        detalleUnidadProducto.setCreatedAt(fechaActual);
        detalleUnidadProducto.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        detalleUnidadProducto.setEmpresaId(1L);
        detalleUnidadProducto.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        detalleUnidadProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        detalleUnidadProducto.setActivo(Constantes.REGISTRO_ACTIVO);

        if(detalleUnidadProducto.getCodigoUnidad() == null)    detalleUnidadProducto.setCodigoUnidad("");

        detalleUnidadProducto.setCodigoUnidad(detalleUnidadProducto.getCodigoUnidad().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(detalleUnidadProducto, resultValidacion);

        if(validacion)
            return this.grabarRegistro(detalleUnidadProducto);

        String errorValidacion = "Error de validación Método Registrar Unidades de Venta de Productos";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(DetalleUnidadProducto detalleUnidadProducto) throws Exception {
        return 0;
    }

    @Override
    public List<DetalleUnidadProducto> listar() throws Exception {
        return null;
    }

    @Override
    public DetalleUnidadProducto listarPorId(Long id) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<DetalleUnidadProducto> detalleUnidadProducto = detalleUnidadProductoMapper.listByParameterMap(params);

        if(detalleUnidadProducto.size() > 0)
            return detalleUnidadProducto.get(0);
        else
            return null;
    }

    @Override
    public void eliminar(Long id) throws Exception {

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else{
            String errorValidacion = "Error de validación Método Eliminar Detalle Unidad Producto";

            if(resultValidacion.get("errors") != null){
                List<String> errors =   (List<String>) resultValidacion.get("errors");
                if(errors.size() >0)
                    errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
            }

            throw new ValidationServiceException(errorValidacion);
        }

    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public DetalleUnidadProducto grabarRegistro(DetalleUnidadProducto detalleUnidadProducto) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        //Unidad unidadG1 = unidadDAO.listarPorId(detalleUnidadProducto.getUnidad().getId());

        if(detalleUnidadProducto.getId() != null && !detalleUnidadProducto.getId().equals(Constantes.CANTIDAD_ZERO_LONG)){

            params.put("ID", detalleUnidadProducto.getId());
            params.put("CODIGO_UNIDAD", detalleUnidadProducto.getCodigoUnidad());
            params.put("PRECIO", detalleUnidadProducto.getPrecio());
            params.put("COSTO_COMPRA", detalleUnidadProducto.getCostoCompra());
            params.put("USER_ID", detalleUnidadProducto.getUserId());
            params.put("UPDATED_AT", detalleUnidadProducto.getUpdatedAd());

            int resultado = detalleUnidadProductoMapper.updateByPrimaryKeySelective(params);

        }else{
            detalleUnidadProducto = detalleUnidadProductoDAO.registrar(detalleUnidadProducto);
        }

        if(detalleUnidadProducto.getUnidad().getCantidad().equals(Constantes.CANTIDAD_UNIDAD_DOUBLE) &&
                detalleUnidadProducto.getAlmacenId().equals(Constantes.CANTIDAD_ZERO_LONG)){

            params.clear();

            params.put("ID", detalleUnidadProducto.getProductoId());
            params.put("CODIGO_UNIDAD", detalleUnidadProducto.getCodigoUnidad());
            params.put("CODIGO_PRODUCTO", detalleUnidadProducto.getCodigoUnidad());
            params.put("PRECIO_UNIDAD", detalleUnidadProducto.getPrecio());
            params.put("PRECIO_COMPRA", detalleUnidadProducto.getCostoCompra());
            params.put("UPDATED_AT", detalleUnidadProducto.getUpdatedAd());

            int resultado = productoMapper.updateByPrimaryKeySelective(params);
        }


        return detalleUnidadProducto;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(DetalleUnidadProducto detalleUnidadProducto) throws Exception {
        return 0;
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
        params.put("UPDATED_AT",fechaUpdate);

        int res= detalleUnidadProductoMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Codigo de Unidad Registrado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }

    @Override
    public boolean validacionRegistro(DetalleUnidadProducto detalleUnidadProducto, Map<String, Object> resultValidacion) {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CODIGO_UNIDAD",detalleUnidadProducto.getCodigoUnidad());
        params.put("EMPRESA_ID",detalleUnidadProducto.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        if(detalleUnidadProducto.getId() != null && !detalleUnidadProducto.getId().equals(Constantes.CANTIDAD_ZERO_LONG)){
            params.put("NO_ID",detalleUnidadProducto.getId());
        }

        List<DetalleUnidadProducto> detallesV1 = detalleUnidadProductoMapper.listByParameterMap(params);

        if(detallesV1.size() > 0){
            resultado = false;
            error = "El código ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        if(detalleUnidadProducto.getUnidad() == null || detalleUnidadProducto.getUnidad().getId() == null){
            resultado = false;
            error = "Debe de remitir una unidad a registrar";
            errors.add(error);
        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(DetalleUnidadProducto detalleUnidadProducto, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //Lógica de Validaciones para Eliminación Detalle Unidad Producto

        DetalleUnidadProducto detalleUnidadProducto = detalleUnidadProductoDAO.listarPorId(id);

        if(detalleUnidadProducto.getUnidad().getCantidad().equals(Constantes.CANTIDAD_UNIDAD_DOUBLE) &&
                detalleUnidadProducto.getAlmacenId().equals(Constantes.CANTIDAD_ZERO_LONG)){
            resultado = false;
            error = "El Codigo de Venta no se puede eliminar, corresponde a la unidad";
            errors.add(error);

        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

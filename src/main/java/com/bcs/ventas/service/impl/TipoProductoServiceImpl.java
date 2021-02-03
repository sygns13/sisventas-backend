package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.TipoProductoDAO;
import com.bcs.ventas.dao.mappers.TipoProductoMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.TipoProducto;
import com.bcs.ventas.service.TipoProductoService;
import com.bcs.ventas.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TipoProductoServiceImpl implements TipoProductoService {

    @Autowired
    private TipoProductoDAO tipoProductoDAO;

    @Autowired
    private TipoProductoMapper tipoProductoMapper;

    @Override
    public TipoProducto registrar(TipoProducto a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        if(a.getTipo() == null)    a.setTipo("");

        a.setTipo(a.getTipo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Tipo de Producto";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public TipoProducto modificar(TipoProducto a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getTipo() == null)    a.setTipo("");

        a.setTipo(a.getTipo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Tipo de Producto";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<TipoProducto> listar() throws Exception {
        return tipoProductoMapper.getAllEntities();
        //return tipoProductoDAO.listar();
    }

    @Override
    public TipoProducto listarPorId(Long id) throws Exception {
       // Map<String, Object> params = new HashMap<String, Object>();
        //params.put("ID",id);
        //return tipoProductoMapper.listByParameterMap(params).get(0);
        return tipoProductoDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
       // tipoProductoDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion)
            this.grabarEliminar(id);

        String errorValidacion = "Error de validación Método Eliminar Tipo de Producto";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }



    //TODO: Métodos de Grabado

    @Transactional
    @Override
    public TipoProducto grabarRegistro(TipoProducto a) throws Exception {
        return tipoProductoDAO.registrar(a);
    }

    @Transactional
    @Override
    public TipoProducto grabarRectificar(TipoProducto a) throws Exception {
        return tipoProductoDAO.modificar(a);
    }

    @Transactional
    @Override
    public void grabarEliminar(Long id) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);

        int res= tipoProductoMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Tipo de Producto indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //TODO: Métodos de Validación

    @Override
    public boolean validacionRegistro(TipoProducto a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("TIPO",a.getTipo());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<TipoProducto> tipoProductosV1 = tipoProductoMapper.listByParameterMap(params);



        if(tipoProductosV1.size() > 0){
            resultado = false;
            error = "El nombre del Tipo de Producto ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(TipoProducto a , Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_ID",a.getId());
        params.put("TIPO",a.getTipo());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);


        List<TipoProducto> tipoProductosV1 = tipoProductoMapper.listByParameterMap(params);

        if(tipoProductosV1.size() > 0){
            resultado = false;
            error = "El nombre del Tipo de Producto ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

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

        //Lógica de Validaciones para Eliminación TipoProducto

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

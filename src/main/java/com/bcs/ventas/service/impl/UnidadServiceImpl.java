package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.UnidadDAO;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Unidad;
import com.bcs.ventas.service.UnidadService;
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
public class UnidadServiceImpl implements UnidadService {

    @Autowired
    private UnidadDAO unidadDAO;

    @Autowired
    private UnidadMapper unidadMapper;

    @Override
    public Unidad registrar(Unidad a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getAbreviatura() == null)    a.setAbreviatura("");

        a.setNombre(a.getNombre().trim());
        a.setAbreviatura(a.getAbreviatura().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Unidad";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public Unidad modificar(Unidad a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getAbreviatura() == null)    a.setAbreviatura("");

        a.setNombre(a.getNombre().trim());
        a.setAbreviatura(a.getAbreviatura().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Unidad";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<Unidad> listar() throws Exception {
        return unidadMapper.getAllEntities();
        //return unidadDAO.listar();
    }

    @Override
    public Unidad listarPorId(Long id) throws Exception {
       // Map<String, Object> params = new HashMap<String, Object>();
        //params.put("ID",id);
        //return unidadMapper.listByParameterMap(params).get(0);
        return unidadDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
       // unidadDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion)
            this.grabarEliminar(id);

        String errorValidacion = "Error de validación Método Eliminar unidad";

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
    public Unidad grabarRegistro(Unidad a) throws Exception {
        return unidadDAO.registrar(a);
    }

    @Transactional
    @Override
    public Unidad grabarRectificar(Unidad a) throws Exception {
        return unidadDAO.modificar(a);
    }

    @Transactional
    @Override
    public void grabarEliminar(Long id) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);

        int res= unidadMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar la Unidad indicada, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //TODO: Métodos de Validación

    @Override
    public boolean validacionRegistro(Unidad a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Unidad> unidadsV1 = unidadMapper.listByParameterMap(params);

        if(unidadsV1.size() > 0){
            resultado = false;
            error = "El nombre del Unidad ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();


        params.put("ABREVIATURA",a.getAbreviatura());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Unidad> unidadsV2 = unidadMapper.listByParameterMap(params);

        if(unidadsV2.size() > 0){
            resultado = false;
            error = "La Abreviatura de la Unidad ingresada ya se encuentra registrada";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Unidad a , Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_ID",a.getId());
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);


        List<Unidad> unidadsV1 = unidadMapper.listByParameterMap(params);

        if(unidadsV1.size() > 0){
            resultado = false;
            error = "El nombre del Unidad ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();
        params.put("NO_ID",a.getId());
        params.put("ABREVIATURA",a.getAbreviatura());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Unidad> unidadsV2 = unidadMapper.listByParameterMap(params);

        if(unidadsV2.size() > 0){
            resultado = false;
            error = "La Abreviatura de la Unidad ingresada ya se encuentra registrada";
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

        //Lógica de Validaciones para Eliminación Unidad

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

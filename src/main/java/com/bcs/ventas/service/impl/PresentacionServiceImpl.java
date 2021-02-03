package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.PresentacionDAO;
import com.bcs.ventas.dao.mappers.PresentacionMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Presentacion;
import com.bcs.ventas.service.PresentacionService;
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
public class PresentacionServiceImpl implements PresentacionService {

    @Autowired
    private PresentacionDAO presentacionDAO;

    @Autowired
    private PresentacionMapper presentacionMapper;

    @Override
    public Presentacion registrar(Presentacion a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        if(a.getPresentacion() == null)    a.setPresentacion("");

        a.setPresentacion(a.getPresentacion().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Presentacion";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public Presentacion modificar(Presentacion a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getPresentacion() == null)    a.setPresentacion("");

        a.setPresentacion(a.getPresentacion().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Presentacion";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<Presentacion> listar() throws Exception {
        return presentacionMapper.getAllEntities();
        //return presentacionDAO.listar();
    }

    @Override
    public Presentacion listarPorId(Long id) throws Exception {
       // Map<String, Object> params = new HashMap<String, Object>();
        //params.put("ID",id);
        //return presentacionMapper.listByParameterMap(params).get(0);
        return presentacionDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
       // presentacionDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion)
            this.grabarEliminar(id);

        String errorValidacion = "Error de validación Método Eliminar presentacion";

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
    public Presentacion grabarRegistro(Presentacion a) throws Exception {
        return presentacionDAO.registrar(a);
    }

    @Transactional
    @Override
    public Presentacion grabarRectificar(Presentacion a) throws Exception {
        return presentacionDAO.modificar(a);
    }

    @Transactional
    @Override
    public void grabarEliminar(Long id) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);

        int res= presentacionMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Presentacion indicada, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //TODO: Métodos de Validación

    @Override
    public boolean validacionRegistro(Presentacion a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PRESENTACION",a.getPresentacion());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Presentacion> presentacionsV1 = presentacionMapper.listByParameterMap(params);



        if(presentacionsV1.size() > 0){
            resultado = false;
            error = "El nombre del Presentacion ingresada ya se encuentra registrada";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Presentacion a , Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_ID",a.getId());
        params.put("PRESENTACION",a.getPresentacion());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);


        List<Presentacion> presentacionsV1 = presentacionMapper.listByParameterMap(params);

        if(presentacionsV1.size() > 0){
            resultado = false;
            error = "El nombre del Presentacion ingresada ya se encuentra registrada";
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

        //Lógica de Validaciones para Eliminación Presentacion

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

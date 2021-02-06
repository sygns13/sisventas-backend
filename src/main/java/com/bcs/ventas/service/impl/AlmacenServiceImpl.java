package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.mappers.AlmacenMapper;
import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.service.AlmacenService;
import com.bcs.ventas.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlmacenServiceImpl implements AlmacenService {

    @Autowired
    private AlmacenDAO almacenDAO;

    @Autowired
    private AlmacenMapper almacenMapper;

    @Override
    public Almacen registrar(Almacen a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        a.setEmpresaId(1L);
        a.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDireccion() == null) a.setDireccion("");
        if(a.getCodigo() == null)    a.setCodigo("");

        a.setNombre(a.getNombre().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setCodigo(a.getCodigo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Local";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public int modificar(Almacen a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDireccion() == null) a.setDireccion("");
        if(a.getCodigo() == null)    a.setCodigo("");

        a.setNombre(a.getNombre().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setCodigo(a.getCodigo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Eliminar Local";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<Almacen> listar() throws Exception {
        //return almacenMapper.getAllEntities();
        //return almacenDAO.listar();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        return almacenMapper.listByParameterMap(params);
    }

    @Override
    public Almacen listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Almacen> almacens = almacenMapper.listByParameterMap(params);

        if(almacens.size() > 0)
            return almacens.get(0);
        else
            return null;

        //return almacenDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
       // almacenDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else{
            String errorValidacion = "Error de validación Método Modificar Local";

            if(resultValidacion.get("errors") != null){
                List<String> errors =   (List<String>) resultValidacion.get("errors");
                if(errors.size() >0)
                    errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
            }

            throw new ValidationServiceException(errorValidacion);
        }
    }



    //TODO: Métodos de Grabado

    @Transactional
    @Override
    public Almacen grabarRegistro(Almacen a) throws Exception {
        return almacenDAO.registrar(a);
    }

    @Transactional
    @Override
    public int grabarRectificar(Almacen a) throws Exception {
        //return almacenDAO.modificar(a);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", a.getId());
        params.put("NOMBRE", a.getNombre());
        params.put("DIRECCION", a.getDireccion());
        params.put("CODIGO", a.getCodigo());
        params.put("DISTRITO_ID", a.getDistritoId());
        params.put("USER_ID", a.getUserId());
        params.put("UPDATED_AT", a.getUpdatedAd());

        return almacenMapper.updateByPrimaryKeySelective(params);
    }

    @Transactional
    @Override
    public void grabarEliminar(Long id) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);

        int res= almacenMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Local indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //TODO: Métodos de Validación

    @Override
    public boolean validacionRegistro(Almacen a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Almacen> almacensV1 = almacenMapper.listByParameterMap(params);



        if(almacensV1.size() > 0){
            resultado = false;
            error = "El nombre del local ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        params.put("CODIGO",a.getCodigo());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            resultado = false;
            error = "El código del local ingresado ya se encuentra registrado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Almacen a , Map<String, Object> resultValidacion){

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


        List<Almacen> almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            resultado = false;
            error = "El nombre del local ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        params.put("NO_ID",a.getId());
        params.put("CODIGO",a.getCodigo());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            resultado = false;
            error = "El código del local ingresado ya se encuentra registrado";
            errors.add(error);
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

        //Lógica de Validaciones para Eliminación Almacén

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

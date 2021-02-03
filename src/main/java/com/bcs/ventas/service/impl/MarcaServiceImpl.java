package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.MarcaDAO;
import com.bcs.ventas.dao.mappers.MarcaMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Marca;
import com.bcs.ventas.service.MarcaService;
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
public class MarcaServiceImpl implements MarcaService {

    @Autowired
    private MarcaDAO marcaDAO;

    @Autowired
    private MarcaMapper marcaMapper;

    @Override
    public Marca registrar(Marca a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        if(a.getNombre() == null)    a.setNombre("");

        a.setNombre(a.getNombre().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Marca";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public Marca modificar(Marca a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");

        a.setNombre(a.getNombre().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Marca";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<Marca> listar() throws Exception {
        return marcaMapper.getAllEntities();
        //return marcaDAO.listar();
    }

    @Override
    public Marca listarPorId(Long id) throws Exception {
       // Map<String, Object> params = new HashMap<String, Object>();
        //params.put("ID",id);
        //return marcaMapper.listByParameterMap(params).get(0);
        return marcaDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
       // marcaDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion)
            this.grabarEliminar(id);

        String errorValidacion = "Error de validación Método Eliminar marca";

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
    public Marca grabarRegistro(Marca a) throws Exception {
        return marcaDAO.registrar(a);
    }

    @Transactional
    @Override
    public Marca grabarRectificar(Marca a) throws Exception {
        return marcaDAO.modificar(a);
    }

    @Transactional
    @Override
    public void grabarEliminar(Long id) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);

        int res= marcaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Marca indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //TODO: Métodos de Validación

    @Override
    public boolean validacionRegistro(Marca a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Marca> marcasV1 = marcaMapper.listByParameterMap(params);



        if(marcasV1.size() > 0){
            resultado = false;
            error = "El nombre del Marca ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Marca a , Map<String, Object> resultValidacion){

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


        List<Marca> marcasV1 = marcaMapper.listByParameterMap(params);

        if(marcasV1.size() > 0){
            resultado = false;
            error = "El nombre del Marca ingresado ya se encuentra registrado";
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

        //Lógica de Validaciones para Eliminación Marca

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.PresentacionDAO;
import com.bcs.ventas.dao.mappers.PresentacionMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Presentacion;
import com.bcs.ventas.service.PresentacionService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class PresentacionServiceImpl implements PresentacionService {

    @Autowired
    private PresentacionDAO presentacionDAO;

    @Autowired
    private PresentacionMapper presentacionMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public Presentacion registrar(Presentacion a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //Oauth inicio
        a.setEmpresaId(claimsAuthorization.getEmpresaId());
        a.setUserId(claimsAuthorization.getUserId());
        //Oauth final

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
    public int modificar(Presentacion a) throws Exception {
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
       // return presentacionMapper.getAllEntities();
        //return presentacionDAO.listar();
        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        return presentacionMapper.listByParameterMap(params);
    }

    public Page<Presentacion> listar(Pageable page, String buscar) throws Exception {
        //return bancoDAO.listar();
        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = presentacionMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Presentacion> presentacions = presentacionMapper.listByParameterMap(params);

        return new PageImpl<>(presentacions, page, total);
    }

    @Override
    public Presentacion listarPorId(Long id) throws Exception {
       // Map<String, Object> params = new HashMap<String, Object>();
        //params.put("ID",id);
        //return presentacionMapper.listByParameterMap(params).get(0);
        //return presentacionDAO.listarPorId(id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Presentacion> presentacions = presentacionMapper.listByParameterMap(params);

        if(presentacions.size() > 0)
            return presentacions.get(0);
        else
            return null;
    }

    @Override
    public void eliminar(Long id) throws Exception {
       // presentacionDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else {
            String errorValidacion = "Error de validación Método Eliminar presentacion";

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
    public Presentacion grabarRegistro(Presentacion a) throws Exception {
        return presentacionDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Presentacion p) throws Exception {
        //return presentacionDAO.modificar(a);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", p.getId());
        params.put("PRESENTACION", p.getPresentacion());
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", p.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        return presentacionMapper.updateByPrimaryKeySelective(params);
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
        int res= presentacionMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Presentacion indicada, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void altabaja(Long id, Integer valor) throws Exception {

        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("ACTIVO", valor);
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT",fechaUpdate);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int res= presentacionMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }




    //Métodos de Validación

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

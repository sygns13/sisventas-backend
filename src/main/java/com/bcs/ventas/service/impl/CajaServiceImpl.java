package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.CajaDAO;
import com.bcs.ventas.dao.CajaUserDAO;
import com.bcs.ventas.dao.mappers.CajaMapper;
import com.bcs.ventas.dao.mappers.CajaUserMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Caja;
import com.bcs.ventas.model.entities.CajaUser;
import com.bcs.ventas.service.CajaService;
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
public class CajaServiceImpl implements CajaService {

    @Autowired
    private CajaDAO cajaDAO;

    @Autowired
    private CajaUserDAO cajaUserDAO;

    @Autowired
    private CajaMapper cajaMapper;
    @Autowired
    private CajaUserMapper cajaUserMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public Caja registrar(Caja a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //FIXME: Cambiar cuando se integre currencys
        a.setCurrencyCode("PEN");

        //Oauth inicio
        a.setEmpresaId(claimsAuthorization.getEmpresaId());
        a.setIsCreatedBy(claimsAuthorization.getUserId());
        //Oauth final

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);
        a.setEstado(Constantes.REGISTRO_ACTIVO);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getCurrencyCode() == null) a.setCurrencyCode("");

        a.setNombre(a.getNombre().trim());
        a.setCurrencyCode(a.getCurrencyCode().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Caja";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public int modificar(Caja a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getCurrencyCode() == null) a.setCurrencyCode("");

        a.setNombre(a.getNombre().trim());
        a.setCurrencyCode(a.getCurrencyCode().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Caja";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<Caja> listar() throws Exception {
        //return cajaDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);
        return cajaMapper.listByParameterMap(params);
    }

    public Page<Caja> listar(Pageable page, String buscar, Long idAlmacen) throws Exception {
        //return cajaDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        if(idAlmacen != null && idAlmacen > 0)
            params.put("ALMACEN_ID",idAlmacen);


        Long total = cajaMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Caja> cajas = cajaMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return new PageImpl<>(cajas, page, total);
    }

    public List<Caja> AllByAlmacen(String buscar, Long idAlmacen) throws Exception {
        //return cajaDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        if(idAlmacen != null && idAlmacen > 0)
            params.put("ALMACEN_ID",idAlmacen);


        List<Caja> cajas = cajaMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return cajas;
    }

    public List<CajaUser> AllByAlmacenAndUsers(String buscar, Long idAlmacen, Long idUser) throws Exception {
        //return cajaDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("CU_NO_BORRADO_LEFT",Constantes.REGISTRO_BORRADO);
        params.put("CU_ACTIVO_LEFT",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        if(idAlmacen != null && idAlmacen > 0)
            params.put("ALMACEN_ID",idAlmacen);

        if(idUser != null && idUser > 0)
            params.put("CU_USER_ID_ASIGNADO_LEFT",idUser);


        List<CajaUser> cajasUser = cajaUserMapper.listCajasAsignedsByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return cajasUser;
    }



    @Override
    public Caja listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Caja> cajas = cajaMapper.listByParameterMap(params);

        if(cajas.size() > 0)
            return cajas.get(0);
        else
            return null;

        //return cajaDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
        // cajaDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else{
            String errorValidacion = "Error de validación Método Eliminar caja";

            if(resultValidacion.get("errors") != null){
                List<String> errors =   (List<String>) resultValidacion.get("errors");
                if(errors.size() >0)
                    errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
            }

            throw new ValidationServiceException(errorValidacion);
        }

    }

    //Métodos de Grabado

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

        int res= cajaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public Caja grabarRegistro(Caja a) throws Exception {
        return cajaDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Caja b) throws Exception {
        //return cajaDAO.modificar(a);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", b.getId());
        params.put("NOMBRE", b.getNombre());
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        return cajaMapper.updateByPrimaryKeySelective(params);
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
        params.put("ESTADO",Constantes.REGISTRO_BORRADO);
        params.put("LAST_USED_BY", claimsAuthorization.getUserId());
        params.put("LAST_USED",fechaUpdate);
        params.put("UPDATED_AT",fechaUpdate);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        int res= cajaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Caja indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //Métodos de Validación

    @Override
    public boolean validacionRegistro(Caja a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Caja> cajasV1 = cajaMapper.listByParameterMap(params);



        if(cajasV1.size() > 0){
            resultado = false;
            error = "El nombre del Caja ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        if(a.getAlmacen() == null || a.getAlmacen().getId() == null || a.getAlmacen().getId() <= 0){
            resultado = false;
            error = "Debe seleccionar una Sucursal";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Caja a , Map<String, Object> resultValidacion){

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


        List<Caja> cajasV1 = cajaMapper.listByParameterMap(params);

        if(cajasV1.size() > 0){
            resultado = false;
            error = "El nombre del Caja ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        if(a.getAlmacen() == null || a.getAlmacen().getId() == null || a.getAlmacen().getId() <= 0){
            resultado = false;
            error = "Debe seleccionar una Sucursal";
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

        //Lógica de Validaciones para Eliminación Caja
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", id);
        params.put("EMPRESA_ID", claimsAuthorization.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<CajaUser> cajaUsers = cajaUserMapper.listByParameterMap(params);

        if(cajaUsers != null && cajaUsers.size() > 0){
            resultado = false;
            error = "No se puede eliminar el Caja, ya que tiene un usuario asignado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }


    //Funcions Caja Asignacion

    @Override
    public CajaUser AsignarCaja(CajaUser a) throws Exception {
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //Oauth inicio
        a.setEmpresaId(claimsAuthorization.getEmpresaId());
        a.setUserId(claimsAuthorization.getUserId());
        //Oauth final

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionAsignacionCaja(a, resultValidacion);


        if(validacion)
            return this.grabarAsignacionCaja(a);

        String errorValidacion = "Error de validación Método Asignar Caja a Usuario";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    private boolean validacionAsignacionCaja(CajaUser a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        if(a.getUser() == null || a.getUser().getId() == null || a.getUser().getId() <= 0){
            resultado = false;
            error = "Debe remitir un usuario Válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getCaja() == null || a.getCaja().getId() == null || a.getCaja().getId() <= 0){
            resultado = false;
            error = "Debe remitir una Caja Válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",a.getCaja().getId());
        params.put("CU_USER_ID_ASIGNADO",a.getUser().getId());

        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("CU_ACTIVO",Constantes.REGISTRO_ACTIVO);

        List<CajaUser> cajasAsignesV1 = cajaUserMapper.listByParameterMap(params);



        if(cajasAsignesV1.size() > 0){
            resultado = false;
            error = "El nombre del Caja ingresado ya se encuentra registrado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private CajaUser grabarAsignacionCaja(CajaUser a) throws Exception {

        a.setFechaRegistro(LocalDateTime.now());
        a.setFechaFin(null);

        return cajaUserDAO.registrar(a);
    }



    @Override
    public CajaUser EliminarAsignacionCaja(CajaUser a) throws Exception {

        a = cajaUserDAO.listarPorId(a.getId());
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        //Oauth inicio
        a.setUserId(claimsAuthorization.getUserId());
        //Oauth final

        a.setBorrado(Constantes.REGISTRO_BORRADO);
        a.setActivo(Constantes.REGISTRO_INACTIVO);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRemoverAsignacionCaja(a, resultValidacion);


        if(validacion)
            return this.grabarEliminacionAsignacionCaja(a);

        String errorValidacion = "Error de validación Método Eliminar Asignación de Caja a Usuario";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    private boolean validacionRemoverAsignacionCaja(CajaUser a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        if(a.getUser() == null || a.getUser().getId() == null || a.getUser().getId() <= 0){
            resultado = false;
            error = "Debe remitir un usuario Válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getCaja() == null || a.getCaja().getId() == null || a.getCaja().getId() <= 0){
            resultado = false;
            error = "Debe remitir una Caja Válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getId() == null || a.getId() <= 0){
            resultado = false;
            error = "Debe remitir una Asignación Válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",a.getCaja().getId());
        params.put("CU_ID",a.getId());
        params.put("CU_USER_ID_ASIGNADO",a.getUser().getId());

        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("CU_ACTIVO",Constantes.REGISTRO_ACTIVO);

        List<CajaUser> cajasAsignesV1 = cajaUserMapper.listByParameterMap(params);



        if(cajasAsignesV1.isEmpty()){
            resultado = false;
            error = "Debe remitir una Asignación Válida";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private CajaUser grabarEliminacionAsignacionCaja(CajaUser a) throws Exception {

        //a.setFechaRegistro(LocalDateTime.now());
        a.setFechaFin(LocalDateTime.now());

        return cajaUserDAO.modificar(a);
    }
}

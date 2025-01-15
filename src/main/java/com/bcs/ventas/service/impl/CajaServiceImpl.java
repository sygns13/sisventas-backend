package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.CajaAccionMapper;
import com.bcs.ventas.dao.mappers.CajaDatoMapper;
import com.bcs.ventas.dao.mappers.CajaMapper;
import com.bcs.ventas.dao.mappers.CajaUserMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.CajaService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private UserDAO userDAO;

    @Autowired
    private CajaDatoMapper cajaDatoMapper;

    @Autowired
    private CajaDatoDAO cajaDatoDAO;
    @Autowired
    private CajaAccionDAO cajaAccionDAO;

    @Autowired
    private CajaAccionMapper cajaAccionMapper;

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

    public List<CajaUser> CajasByAlmacenUser(String buscar, Long idAlmacen, Long idUser) throws Exception {
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

    @Override
    public CajaDato IniciarCaja(Long idCaja, Long idUser, BigDecimal monto, String sustento) throws Exception {

       Caja caja = cajaDAO.listarPorId(idCaja);
       User user  = userDAO.listarPorId(idUser);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionAccionCaja(caja, user, resultValidacion);


        if(validacion){
            boolean validacion2 = this.validacionInicioCaja(caja, user, monto, sustento, resultValidacion);

            if(validacion2) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("ID", caja.getId());
                params.put("CD_ESTADO",Constantes.CAJA_DATO_CERRADO);

                params.put("EMPRESA_ID",caja.getEmpresaId());
                params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
                params.put("ESTADO",Constantes.REGISTRO_ACTIVO);
                params.put("ORDER_DESC",Constantes.REGISTRO_ACTIVO);

                List<CajaDato> cajaDatoLastV1 = cajaDatoMapper.listByParameterMap(params);
                CajaDato cajaDatoLast = !cajaDatoLastV1.isEmpty() ? cajaDatoLastV1.get(0) : null;

                return this.grabarInicioCaja(caja, user, cajaDatoLast, monto, sustento);
            }
        }


        String errorValidacion = "Error de validación Método Inicio de Caja de Usuario";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public CajaDato CerrarCaja(Long idCaja, Long idUser, BigDecimal monto) throws Exception {

       Caja caja = cajaDAO.listarPorId(idCaja);
       User user  = userDAO.listarPorId(idUser);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionAccionCaja(caja, user, resultValidacion);


        if(validacion){
            boolean validacion2 = this.validacionCierreCaja(caja, user, monto, resultValidacion);

            if(validacion2) {
                CajaDato cajaDato = resultValidacion.get("cajaDato") != null ? (CajaDato) resultValidacion.get("cajaDato") : new CajaDato();
                return this.grabarCierreCaja(caja, user, cajaDato, monto);
            }
        }


        String errorValidacion = "Error de validación Método Cierre de Caja de Usuario";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public CajaDato getLastCajaCerrada(Long idCaja) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", idCaja);
        //params.put("CD_ESTADO",Constantes.CAJA_DATO_INICIADO);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ESTADO",Constantes.REGISTRO_ACTIVO);
        params.put("ORDER_DESC",Constantes.REGISTRO_ACTIVO);

        List<CajaDato> cajaDatoLastV1 = cajaDatoMapper.listByParameterMap(params);

        //CajaDato cajaDato = !cajaDatoLastV1.isEmpty() ? cajaDatoLastV1.get(0) : new CajaDato();

        return !cajaDatoLastV1.isEmpty() ? cajaDatoLastV1.get(0) : new CajaDato();
    }

    @Override
    public CajaDato getCajaIniciadaByUserSession() throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CD_LAST_USER_ID", claimsAuthorization.getUserId());
        params.put("CD_ESTADO",Constantes.CAJA_DATO_INICIADO);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ESTADO",Constantes.REGISTRO_ACTIVO);
        params.put("ORDER_DESC",Constantes.REGISTRO_ACTIVO);

        List<CajaDato> cajaDatoLastV1 = cajaDatoMapper.listByParameterMap(params);

        CajaDato cajaDato = !cajaDatoLastV1.isEmpty() ? cajaDatoLastV1.get(0) : new CajaDato();
        if(!cajaDatoLastV1.isEmpty()){
            params.clear();
            params.put("CAJA_DATO_ID", cajaDato.getId());
            params.put("ACCION",Constantes.CAJA_ACCION_MOVIMIENTO_INGRESO);

            params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
            params.put("ESTADO",Constantes.REGISTRO_ACTIVO);

            List<CajaAccion> cajaAccionsIngresos = cajaAccionMapper.listByParameterMap(params);

            params.clear();
            params.put("CAJA_DATO_ID", cajaDato.getId());
            params.put("ACCION",Constantes.CAJA_ACCION_MOVIMIENTO_SALIDA);

            params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
            params.put("ESTADO",Constantes.REGISTRO_ACTIVO);

            List<CajaAccion> cajaAccionsSalidas = cajaAccionMapper.listByParameterMap(params);

            BigDecimal montosIngresos = cajaAccionsIngresos.stream().map(CajaAccion::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal montosSalidas = cajaAccionsSalidas.stream().map(CajaAccion::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);

            cajaDato.setMontoTemporal(cajaDato.getMontoInicio().add(montosIngresos.subtract(montosSalidas)));
        }

        return cajaDato;
    }



    private boolean validacionAccionCaja(Caja caja, User user, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        if(user == null || user.getId() == null || user.getId() <= 0){
            resultado = false;
            error = "Debe remitir un usuario Válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(!user.getActivo().equals(Constantes.REGISTRO_ACTIVO)){
            resultado = false;
            error = "El Usuario se encuentra Inactivo";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(user.getBorrado().equals(Constantes.REGISTRO_BORRADO)){
            resultado = false;
            error = "El Usuario se encuentra Borrrado";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }


        if(caja == null || caja.getId() == null || caja.getId() <= 0){
            resultado = false;
            error = "Debe remitir una Caja Válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(!caja.getActivo().equals(Constantes.REGISTRO_ACTIVO) || !caja.getEstado().equals(Constantes.REGISTRO_ACTIVO)){
            resultado = false;
            error = "La caja se encuentra Inactiva";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(caja.getBorrado().equals(Constantes.REGISTRO_BORRADO)){
            resultado = false;
            error = "La caja se encuentra Borrada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", caja.getId());
        params.put("CU_USER_ID_ASIGNADO",user.getId());

        params.put("EMPRESA_ID",caja.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("CU_ACTIVO",Constantes.REGISTRO_ACTIVO);

        List<CajaUser> cajasAsignesV1 = cajaUserMapper.listByParameterMap(params);


        if(cajasAsignesV1.isEmpty()){
            resultado = false;
            error = "El Usuario debe de estar asignado a la caja";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    private boolean validacionInicioCaja(Caja caja, User user, BigDecimal monto, String sustento, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(monto == null || monto.compareTo(BigDecimal.ZERO) < 0){
            resultado = false;
            error = "Debe remitir un monto Válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }



        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", caja.getId());
        params.put("CD_ESTADO",Constantes.CAJA_DATO_INICIADO);

        params.put("EMPRESA_ID",caja.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ESTADO",Constantes.REGISTRO_ACTIVO);
        params.put("ORDER_DESC",Constantes.REGISTRO_ACTIVO);

        List<CajaDato> cajaDatoLastV1 = cajaDatoMapper.listByParameterMap(params);


        if(!cajaDatoLastV1.isEmpty()){

            User userLast = userDAO.listarPorId(cajaDatoLastV1.get(0).getLastUserId());
            resultado = false;
            error = "La Cajá ya se encuentra Iniciada por el Usuario: "+userLast.getName() + " en la fecha: "+cajaDatoLastV1.get(0).getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    private boolean validacionCierreCaja(Caja caja, User user, BigDecimal monto, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(monto == null || monto.compareTo(BigDecimal.ZERO) < 0){
            resultado = false;
            error = "Debe remitir un monto Válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }



        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", caja.getId());
        params.put("CD_ESTADO", Constantes.CAJA_DATO_INICIADO);
        params.put("CD_LAST_USER_ID", user.getId());

        params.put("EMPRESA_ID", caja.getEmpresaId());
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("ESTADO", Constantes.REGISTRO_ACTIVO);
        params.put("ORDER_DESC", Constantes.REGISTRO_ACTIVO);

        List<CajaDato> cajaDatoLastV1 = cajaDatoMapper.listByParameterMap(params);


        if(cajaDatoLastV1.isEmpty()){

            resultado = false;
            error = "La Cajá no se encuentra Iniciada por el Usuario";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        resultValidacion.put("cajaDato",cajaDatoLastV1.get(0));
        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
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

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private CajaDato grabarInicioCaja(Caja caja, User user, CajaDato cajaDatoLast, BigDecimal monto, String sustento) throws Exception {

        LocalDateTime fechaHora = LocalDateTime.now();
        LocalDate fecha = LocalDate.now();
        CajaDato cajaDato = new CajaDato();

        cajaDato.setCaja(caja);
        cajaDato.setFecha(fecha);
        cajaDato.setFechaInicio(fechaHora);
        cajaDato.setFechaFinal(null);
        cajaDato.setMontoInicio(monto);
        cajaDato.setMontoFinal(null);
        cajaDato.setLastUserId(user.getId());
        cajaDato.setAccessCount(Constantes.CANTIDAD_ZERO);
        cajaDato.setCreatedAt(fechaHora);
        cajaDato.setUpdatedAd(fechaHora);
        cajaDato.setEstado(Constantes.CAJA_DATO_INICIADO);
        cajaDato.setEmpresaId(caja.getEmpresaId());
        cajaDato.setSustentoInicio(sustento);


        cajaDato = cajaDatoDAO.registrar(cajaDato);

        CajaAccion cajaAccion = new CajaAccion();

        cajaAccion.setCajaDato(cajaDato);
        cajaAccion.setAccion(Constantes.CAJA_ACCION_APERTURA);
        cajaAccion.setFecha(fechaHora);

        if(cajaDatoLast != null){
            cajaAccion.setMonto(monto.subtract(cajaDatoLast.getMontoFinal()));
            cajaAccion.setDescripcion("Apertura de Caja con Monto: " + String.format("%.2f", monto) + "Monto Anterior " + String.format("%.2f",cajaDatoLast.getMontoFinal()));
        }
        else{
            cajaAccion.setMonto(monto);
            cajaAccion.setDescripcion("Apertura de Caja con Monto: " + String.format("%.2f", monto));
        }

        cajaAccion.setUserId(user.getId());
        cajaAccion.setEmpresaId(caja.getEmpresaId());
        cajaAccion.setActivo(Constantes.REGISTRO_ACTIVO);
        cajaAccion.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        cajaAccion.setCreatedAt(fechaHora);
        cajaAccion.setUpdatedAd(fechaHora);

        cajaAccionDAO.registrar(cajaAccion);


        return cajaDato;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private CajaDato grabarCierreCaja(Caja caja, User user, CajaDato cajaDato, BigDecimal monto) throws Exception {

        LocalDateTime fechaHora = LocalDateTime.now();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",cajaDato.getId());
        params.put("FECHA_FINAL", fechaHora);
        params.put("MONTO_FINAL", monto);
        params.put("LAST_USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT",fechaHora);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        int res= cajaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

        CajaAccion cajaAccion = new CajaAccion();

        cajaAccion.setCajaDato(cajaDato);
        cajaAccion.setAccion(Constantes.CAJA_ACCION_CIERRE);
        cajaAccion.setFecha(fechaHora);
        cajaAccion.setMonto(new BigDecimal(0));
        cajaAccion.setDescripcion("Cierre de Caja con Monto: " + String.format("%.2f", monto));

        cajaAccion.setUserId(user.getId());
        cajaAccion.setEmpresaId(caja.getEmpresaId());
        cajaAccion.setActivo(Constantes.REGISTRO_ACTIVO);
        cajaAccion.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        cajaAccion.setCreatedAt(fechaHora);
        cajaAccion.setUpdatedAd(fechaHora);

        cajaAccionDAO.registrar(cajaAccion);


        return cajaDato;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public CajaAccion ingresoSalidaCaja(Long userId, CajaDato cajaDato, BigDecimal monto, Integer type, String descripcion) throws Exception {

        LocalDateTime fechaHora = LocalDateTime.now();
        CajaAccion cajaAccion = new CajaAccion();

        cajaAccion.setCajaDato(cajaDato);
        cajaAccion.setAccion(type);
        cajaAccion.setFecha(fechaHora);
        cajaAccion.setMonto(monto);
        cajaAccion.setDescripcion(descripcion);

        cajaAccion.setUserId(userId);
        cajaAccion.setEmpresaId(cajaDato.getEmpresaId());
        cajaAccion.setActivo(Constantes.REGISTRO_ACTIVO);
        cajaAccion.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        cajaAccion.setCreatedAt(fechaHora);
        cajaAccion.setUpdatedAd(fechaHora);

        cajaAccion = cajaAccionDAO.registrar(cajaAccion);


        return cajaAccion;
    }

    @Override
    public CajaAccion registrarCajaAccion(CajaDato cajaDato, CajaAccion a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        Caja caja = cajaDAO.listarPorId(cajaDato.getCaja().getId());
        User user  = userDAO.listarPorId(claimsAuthorization.getUserId());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionAccionCaja(caja, user, resultValidacion);

        if(validacion)
            return this.ingresoSalidaCaja(user.getUserId(), cajaDato, a.getMonto(), a.getAccion(), a.getDescripcion());

        String errorValidacion = "Error de validación Método Registrar Caja Acción";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }
}

package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.DatosUserDAO;
import com.bcs.ventas.dao.UserDAO;
import com.bcs.ventas.dao.TipoDocumentoDAO;
import com.bcs.ventas.dao.mappers.DatosUserMapper;
import com.bcs.ventas.dao.mappers.UserMapper;
import com.bcs.ventas.dao.mappers.TipoDocumentoMapper;
import com.bcs.ventas.dao.mappers.VentaMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.UserService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DatosUserDAO datosUserDAO;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DatosUserMapper datosUserMapper;

    @Autowired
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Autowired
    private TipoDocumentoMapper tipoDocumentoMapper;

    @Autowired
    private VentaMapper ventaMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private AlmacenDAO almacenDAO;

    //@Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<User> listar(Pageable page, String buscar) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = userMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<User> users = userMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        users.forEach(user -> {
            if(user.getAlmacenId() == null || user.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
                user.setAlmacen("General - Todas");
            } else {
                try {
                    Almacen almacen = almacenDAO.listarPorId(user.getAlmacenId());
                    user.setAlmacen(almacen.getNombre());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return new PageImpl<>(users, page, total);
    }

    @Override
    public User registrar(User a) throws Exception {
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

        if(a.getName() == null)    a.setName("");
        if(a.getEmail() == null)    a.setEmail("");
        if(a.getPassword() == null)    a.setPassword("");

        a.setName(a.getName().trim());
        a.setName(a.getName().trim());
        a.setEmail(a.getEmail().trim());

        if(a.getDatos() != null){
            a.getDatos().setEmpresaId(claimsAuthorization.getEmpresaId());
            a.getDatos().setUserGestionaId(claimsAuthorization.getUserId());

            if(a.getDatos().getNombres() == null)    a.getDatos().setNombres("");
            if(a.getDatos().getApellidoPaterno() == null)    a.getDatos().setApellidoPaterno("");
            if(a.getDatos().getApellidoMaterno() == null)    a.getDatos().setApellidoMaterno("");
            if(a.getDatos().getDireccion() == null) a.getDatos().setDireccion("");
            if(a.getDatos().getTelefono() == null) a.getDatos().setTelefono("");
            if(a.getDatos().getDocumento() == null) a.getDatos().setDocumento("");
            //if(a.getDatos().getEmail() == null) a.getDatos().setEmail("");

            a.getDatos().setNombres(a.getDatos().getNombres().trim());
            a.getDatos().setApellidoPaterno(a.getDatos().getApellidoPaterno().trim());
            a.getDatos().setApellidoMaterno(a.getDatos().getApellidoMaterno().trim());
            a.getDatos().setDireccion(a.getDatos().getDireccion().trim());
            a.getDatos().setTelefono(a.getDatos().getTelefono().trim());
            a.getDatos().setDocumento(a.getDatos().getDocumento().trim());
            a.getDatos().setEmail(a.getEmail());
        }


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar User";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(User a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getName() == null)    a.setName("");
        if(a.getEmail() == null)    a.setEmail("");
        if(a.getPassword() == null)    a.setPassword("");

        a.setName(a.getName().trim());
        a.setName(a.getName().trim());
        a.setEmail(a.getEmail().trim());

        if(a.getDatos() != null){
            a.getDatos().setEmpresaId(claimsAuthorization.getEmpresaId());
            a.getDatos().setUserGestionaId(claimsAuthorization.getUserId());

            if(a.getDatos().getNombres() == null)    a.getDatos().setNombres("");
            if(a.getDatos().getApellidoPaterno() == null)    a.getDatos().setApellidoPaterno("");
            if(a.getDatos().getApellidoMaterno() == null)    a.getDatos().setApellidoMaterno("");
            if(a.getDatos().getDireccion() == null) a.getDatos().setDireccion("");
            if(a.getDatos().getTelefono() == null) a.getDatos().setTelefono("");
            if(a.getDatos().getDocumento() == null) a.getDatos().setDocumento("");
            //if(a.getDatos().getEmail() == null) a.getDatos().setEmail("");

            a.getDatos().setNombres(a.getDatos().getNombres().trim());
            a.getDatos().setApellidoPaterno(a.getDatos().getApellidoPaterno().trim());
            a.getDatos().setApellidoMaterno(a.getDatos().getApellidoMaterno().trim());
            a.getDatos().setDireccion(a.getDatos().getDireccion().trim());
            a.getDatos().setTelefono(a.getDatos().getTelefono().trim());
            a.getDatos().setDocumento(a.getDatos().getDocumento().trim());
            a.getDatos().setEmail(a.getEmail());
        }

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar User";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<User> listar() throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO", Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        List<User> users = userMapper.listByParameterMap(params);

        users.forEach(user -> {
            if(user.getAlmacenId() == null || user.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
                user.setAlmacen("General - Todas");
            } else {
                try {
                    Almacen almacen = almacenDAO.listarPorId(user.getAlmacenId());
                    user.setAlmacen(almacen.getNombre());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return users;
    }

    @Override
    public User listarPorId(Long id) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<User> users = userMapper.listByParameterMap(params);

        if(users.size() > 0) {
            User user = users.get(0);

            if (user.getAlmacenId() == null || user.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0) {
                user.setAlmacen("General - Todas");
            } else {
                try {
                    Almacen almacen = almacenDAO.listarPorId(user.getAlmacenId());
                    user.setAlmacen(almacen.getNombre());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return user;
        }
        else
            return null;
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
        int res= userMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }


    @Override
    public User getByDocument(String document) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("DOCUMENTO",document.trim());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<User> users = userMapper.listByParameterMap(params);

        if(users.size() > 0) {
            User user = users.get(0);

            if (user.getAlmacenId() == null || user.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0) {
                user.setAlmacen("General - Todas");
            } else {
                try {
                    Almacen almacen = almacenDAO.listarPorId(user.getAlmacenId());
                    user.setAlmacen(almacen.getNombre());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return user;
        }
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
            String errorValidacion = "Error de validación Método Eliminar User";

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
    public User grabarRegistro(User a) throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPw = bCryptPasswordEncoder.encode(a.getPassword());

        a.setPassword(encodedPw);
        User user = userDAO.registrar(a);
        DatosUser datosUser = user.getDatos();
        datosUser.setUserId(user.getId());
        datosUser = datosUserDAO.registrar(datosUser);
        user.setDatos(datosUser);
        return user;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(User b) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("ID", b.getId());
        params.put("NAME", b.getName());
        params.put("EMAIL", b.getEmail());

        if(b.getModificarPassword().compareTo(Constantes.REGISTRO_ACTIVO) == 0){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encodedPw = bCryptPasswordEncoder.encode(b.getPassword());
            params.put("PASSWORD", encodedPw);
        }


        params.put("TIPO_USER_ID", b.getTipoUser().getId());
        params.put("USER_ID", b.getUserId());
        params.put("ACTIVO", b.getActivo());
        params.put("ALMACEN_ID", b.getAlmacenId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        userMapper.updateByPrimaryKeySelective(params);

        params.clear();
        params.put("ID", b.getDatos().getId());
        params.put("NOMBRES", b.getDatos().getNombres());
        params.put("APELLIDO_PATERNO", b.getDatos().getApellidoPaterno());
        params.put("APELLIDO_MATERNO", b.getDatos().getApellidoMaterno());
        params.put("DIRECCION", b.getDatos().getDireccion());
        params.put("TELEFONO", b.getDatos().getTelefono());
        params.put("TIPO_DOCUMENTO_ID", b.getDatos().getTipoDocumento().getId());
        params.put("DOCUMENTO", b.getDatos().getDocumento());
        params.put("EMAIL", b.getDatos().getEmail());
        params.put("UPDATED_AT", b.getDatos().getUpdatedAd());

        params.put("USER_GESTIONA_ID", claimsAuthorization.getUserId());
        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        return datosUserMapper.updateByPrimaryKeySelective(params);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void grabarEliminar(Long id) throws Exception {
        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT",fechaUpdate);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        int res= userMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el User indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Override
    public boolean validacionRegistro(User a, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(a.getName() == null || a.getName().isEmpty()){
            resultado = false;
            error = "El campo Username es obligatorio";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getEmail() == null || a.getEmail().isEmpty()){
            resultado = false;
            error = "El campo Email es obligatorio";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getPassword() == null || a.getPassword().isEmpty()){
            resultado = false;
            error = "El campo Password es obligatorio";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getTipoUser() == null || a.getTipoUser().getId() == null || a.getTipoUser().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Debe de seleccionar el Tipo de Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getDatos() == null){
            resultado = false;
            error = "Debe de ingresar los datos del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getDatos().getNombres() == null || a.getDatos().getNombres().isEmpty()){
            resultado = false;
            error = "Debe de ingresar los nombres del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getDatos().getApellidoPaterno() == null || a.getDatos().getApellidoPaterno().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el apellido paterno del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        /*
        if(a.getDatos().getApellidoMaterno() == null || a.getDatos().getApellidoMaterno().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el apellido materno del Usuario ";
            errors.add(error);
        }

        if(a.getDatos().getDireccion() == null || a.getDatos().getDireccion().isEmpty()){
            resultado = false;
            error = "Debe de ingresar la dirección del Usuario ";
            errors.add(error);
        }

        if(a.getDatos().getTelefono() == null || a.getDatos().getTelefono().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el teléfono del Usuario ";
            errors.add(error);
        }*/

        if(a.getDatos().getDocumento() == null || a.getDatos().getDocumento().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el documento del Usuario ";
            errors.add(error);
        }


        if(a.getDatos().getEmail() == null || a.getDatos().getEmail().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el email del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getDatos().getTipoDocumento() == null || a.getDatos().getTipoDocumento().getId() == null || a.getDatos().getTipoDocumento().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Debe de seleccionar el Tipo de Documento de Identidad del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NAME",a.getName());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<User> userV1 = userMapper.listByParameterMap(params);

        if(userV1.size() > 0){
            resultado = false;
            error = "Ya se encuentra registrado un Usuario con el username "+a.getName();
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        params.clear();
        params.put("EMAIL",a.getEmail());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<User> userV2 = userMapper.listByParameterMap(params);

        if(userV2.size() > 0){
            resultado = false;
            error = "Ya se encuentra registrado un Usuario con el email "+a.getEmail();
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(User a, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(a.getName() == null || a.getName().isEmpty()){
            resultado = false;
            error = "El campo Username es obligatorio";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getEmail() == null || a.getEmail().isEmpty()){
            resultado = false;
            error = "El campo Email es obligatorio";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getModificarPassword().compareTo(Constantes.REGISTRO_ACTIVO) == 0){
            if(a.getPassword() == null || a.getPassword().isEmpty()){
                resultado = false;
                error = "El campo Password es obligatorio";
                errors.add(error);

                resultValidacion.put("errors",errors);
                resultValidacion.put("warnings",warnings);

                return resultado;
            }
        }

        if(a.getTipoUser() == null || a.getTipoUser().getId() == null || a.getTipoUser().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Debe de seleccionar el Tipo de Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getDatos() == null){
            resultado = false;
            error = "Debe de ingresar los datos del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getDatos().getNombres() == null || a.getDatos().getNombres().isEmpty()){
            resultado = false;
            error = "Debe de ingresar los nombres del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getDatos().getApellidoPaterno() == null || a.getDatos().getApellidoPaterno().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el apellido paterno del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        /*
        if(a.getDatos().getApellidoMaterno() == null || a.getDatos().getApellidoMaterno().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el apellido materno del Usuario ";
            errors.add(error);
        }

        if(a.getDatos().getDireccion() == null || a.getDatos().getDireccion().isEmpty()){
            resultado = false;
            error = "Debe de ingresar la dirección del Usuario ";
            errors.add(error);
        }

        if(a.getDatos().getTelefono() == null || a.getDatos().getTelefono().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el teléfono del Usuario ";
            errors.add(error);
        }*/

        if(a.getDatos().getDocumento() == null || a.getDatos().getDocumento().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el documento del Usuario ";
            errors.add(error);
        }


        if(a.getDatos().getEmail() == null || a.getDatos().getEmail().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el email del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(a.getDatos().getTipoDocumento() == null || a.getDatos().getTipoDocumento().getId() == null || a.getDatos().getTipoDocumento().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Debe de seleccionar el Tipo de Documento de Identidad del Usuario ";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_ID",a.getId());
        params.put("NAME",a.getName());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<User> userV1 = userMapper.listByParameterMap(params);

        if(userV1.size() > 0){
            resultado = false;
            error = "Ya se encuentra registrado un Usuario con el username "+a.getName();
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        params.clear();
        params.put("NO_ID",a.getId());
        params.put("EMAIL",a.getEmail());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<User> userV2 = userMapper.listByParameterMap(params);

        if(userV2.size() > 0){
            resultado = false;
            error = "Ya se encuentra registrado un Usuario con el email "+a.getEmail();
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionEliminacion(Long idUser, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        /*
        User user = userDAO.listarPorId(idUser);

        //Lógica de Validaciones para Eliminación User
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CLIENTE_ID", idUser);
        params.put("EMPRESA_ID",user.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Venta> ventasV1 = ventaMapper.listByParameterMap(params);

        if(ventasV1.size() > 0){
            resultado = false;
            error = "Por integridad de Datos no se puede eliminar este user ya que tiene ventas asociadas a él";
            errors.add(error);
        }
        params.clear();
        */

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

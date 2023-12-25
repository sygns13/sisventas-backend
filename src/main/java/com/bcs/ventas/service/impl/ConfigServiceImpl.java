package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.ConfigDAO;
import com.bcs.ventas.dao.ConfigDAO;
import com.bcs.ventas.dao.mappers.ConfigMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Config;
import com.bcs.ventas.model.entities.Config;
import com.bcs.ventas.service.ConfigService;
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
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDAO configDAO;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public Config registrar(Config a) throws Exception {
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

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getValor() == null) a.setValor("");

        a.setNombre(a.getNombre().trim());
        a.setValor(a.getValor().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Config";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public int modificar(Config a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getValor() == null) a.setValor("");

        a.setNombre(a.getNombre().trim());
        a.setValor(a.getValor().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Config";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificarList(Config[] configs) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        for (Config a: configs) {
            a.setUpdatedAd(fechaActual);

            if(a.getNombre() == null)    a.setNombre("");
            if(a.getValor() == null) a.setValor("");

            a.setNombre(a.getNombre().trim());
            a.setValor(a.getValor().trim());

            boolean validacion = this.validacionModificado(a, resultValidacion);

            if(!validacion){
                String errorValidacion = "Error de validación Método Modificar Config";

                if(resultValidacion.get("errors") != null){
                    List<String> errors =   (List<String>) resultValidacion.get("errors");
                    if(errors.size() >0)
                        errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
                }

                throw new ValidationServiceException(errorValidacion);
            }
        }

        for (Config a: configs) {
             this.grabarRectificar(a);
        }
        return 1;
    }

    public List<Config> listar() throws Exception {
        //return configDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);
        return configMapper.listByParameterMap(params);
    }

    public Page<Config> listar(Pageable page, String buscar) throws Exception {
        //return configDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = configMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Config> configs = configMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return new PageImpl<>(configs, page, total);
    }

    @Override
    public Config listarPorId(String id) throws Exception {
        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Config> configs = configMapper.listByParameterMap(params);

        if(configs.size() > 0)
            return configs.get(0);
        else
            return null;

        //return configDAO.listarPorId(id);
    }

    @Override
    public void eliminar(String id) throws Exception {
        // configDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else{
            String errorValidacion = "Error de validación Método Eliminar config";

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
    public void altabaja(String id, Integer valor) throws Exception {

        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("ACTIVO", valor);
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT",fechaUpdate);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        int res= configMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public Config grabarRegistro(Config a) throws Exception {
        return configDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Config b) throws Exception {
        //return configDAO.modificar(a);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", b.getId());
        params.put("NOMBRE", b.getNombre());
        params.put("VALOR", b.getValor());
        //params.put("NIVEL", b.getNivel());
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        return configMapper.updateByPrimaryKeySelective(params);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void grabarEliminar(String id) {

        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT",fechaUpdate);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        int res= configMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Config indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //Métodos de Validación

    @Override
    public boolean validacionRegistro(Config a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Config> configsV1 = configMapper.listByParameterMap(params);

        if(configsV1.size() > 0){
            resultado = false;
            error = "El nombre del Config ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        //Validacion Cajas Enabled
        if(a.getId().equals(Constantes.CONFIG_CAJAS_ENABLED)){
            if(!(a.getValor().equals("1") || a.getValor().equals("0"))){
                resultado = false;
                error = "El estado de la Configuración de Uso de Cajas ingresado no es válido";
                errors.add(error);
            }
        }

        //Validacion Cantidad de Copias
        if(a.getId().equals(Constantes.CONFIG_CANTIDAD_COPIAS_COMPROBANTES)){
            Integer cantidadCopias = null;
            try {
                cantidadCopias = Integer.parseInt(a.getValor());
            }catch (Exception e){
                cantidadCopias = null;
            }
            if(cantidadCopias == null || cantidadCopias < 0 || cantidadCopias > 5){
                resultado = false;
                error = "La cantidad de copias de comprobantes ingresada no es válida debe ser un número entero entre 0 y 5";
                errors.add(error);
            }
        }

        //Validacion Tasa de Cambio PEN a USD
        if(a.getId().equals(Constantes.CONFIG_TASA_CAMBIO_PEN_USD)){
            Double tasaCambio = null;
            try {
                tasaCambio = Double.parseDouble(a.getValor());
            }catch (Exception e){
                tasaCambio = null;
            }
            if(tasaCambio == null || tasaCambio < 0){
                resultado = false;
                error = "La tasa de cambio ingresada no es válida debe ser un valor mayor a 0";
                errors.add(error);
            }
        }

        //Validacion IGV PERU
        if(a.getId().equals(Constantes.CONFIG_IGV_PERU)){
            Double igv = null;
            try {
                igv = Double.parseDouble(a.getValor());
            }catch (Exception e){
                igv = null;
            }
            if(igv == null || igv < 0 || igv > 100  ){
                resultado = false;
                error = "El valor del IGV ingresado no es válido debe ser un valor entre 0 y 100";
                errors.add(error);
            }
        }

        //Validacion Tipo de Impresora de Ventas
        if(a.getId().equals(Constantes.CONFIG_TIPO_IMPRESORA_VENTAS)){
            if(!(a.getValor().equals("1") || a.getValor().equals("2"))){
                resultado = false;
                error = "El tipo de impresora de ventas ingresado no es válido";
                errors.add(error);
            }
        }

        //Validacion Vista Previa de Comprobantes
        if(a.getId().equals(Constantes.CONFIG_VISTA_PREVIA_COMPROBANTE_ENABLED)){
            if(!(a.getValor().equals("1") || a.getValor().equals("0"))){
                resultado = false;
                error = "El estado de la Configuración de Vista Previa de Comprobantes ingresado no es válido";
                errors.add(error);
            }
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Config a , Map<String, Object> resultValidacion){

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


        List<Config> configsV1 = configMapper.listByParameterMap(params);

        if(configsV1.size() > 0){
            resultado = false;
            error = "El nombre del Config ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        //Validacion Cajas Enabled
        if(a.getId().equals(Constantes.CONFIG_CAJAS_ENABLED)){
            if(!(a.getValor().equals("1") || a.getValor().equals("0"))){
                resultado = false;
                error = "El estado de la Configuración de Uso de Cajas ingresado no es válido";
                errors.add(error);
            }
        }

        //Validacion Cantidad de Copias
        if(a.getId().equals(Constantes.CONFIG_CANTIDAD_COPIAS_COMPROBANTES)){
            Integer cantidadCopias = null;
            try {
                cantidadCopias = Integer.parseInt(a.getValor());
            }catch (Exception e){
                cantidadCopias = null;
            }
            if(cantidadCopias == null || cantidadCopias < 0 || cantidadCopias > 5){
                resultado = false;
                error = "La cantidad de copias de comprobantes ingresada no es válida debe ser un número entero entre 0 y 5";
                errors.add(error);
            }
        }

        //Validacion Tasa de Cambio PEN a USD
        if(a.getId().equals(Constantes.CONFIG_TASA_CAMBIO_PEN_USD)){
            Double tasaCambio = null;
            try {
                tasaCambio = Double.parseDouble(a.getValor());
            }catch (Exception e){
                tasaCambio = null;
            }
            if(tasaCambio == null || tasaCambio < 0){
                resultado = false;
                error = "La tasa de cambio ingresada no es válida debe ser un valor mayor a 0";
                errors.add(error);
            }
        }

        //Validacion IGV PERU
        if(a.getId().equals(Constantes.CONFIG_IGV_PERU)){
            Double igv = null;
            try {
                igv = Double.parseDouble(a.getValor());
            }catch (Exception e){
                igv = null;
            }
            if(igv == null || igv < 0 || igv > 100  ){
                resultado = false;
                error = "El valor del IGV ingresado no es válido debe ser un valor entre 0 y 100";
                errors.add(error);
            }
        }

        //Validacion Tipo de Impresora de Ventas
        if(a.getId().equals(Constantes.CONFIG_TIPO_IMPRESORA_VENTAS)){
            if(!(a.getValor().equals("1") || a.getValor().equals("2"))){
                resultado = false;
                error = "El tipo de impresora de ventas ingresado no es válido";
                errors.add(error);
            }
        }

        //Validacion Vista Previa de Comprobantes
        if(a.getId().equals(Constantes.CONFIG_VISTA_PREVIA_COMPROBANTE_ENABLED)){
            if(!(a.getValor().equals("1") || a.getValor().equals("0"))){
                resultado = false;
                error = "El estado de la Configuración de Vista Previa de Comprobantes ingresado no es válido";
                errors.add(error);
            }
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;

    }

    @Override
    public boolean validacionEliminacion(String id, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //Lógica de Validaciones para Eliminación Config

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

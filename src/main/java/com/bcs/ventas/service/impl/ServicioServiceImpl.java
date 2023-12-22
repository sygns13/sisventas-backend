package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.ServicioDAO;
import com.bcs.ventas.dao.mappers.ServicioMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.ServicioService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    private ServicioDAO servicioDAO;

    @Autowired
    private ServicioMapper servicioMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;


    @Override
    public Servicio registrar(Servicio a) throws Exception {
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
        if(a.getDescripcion() == null) a.setDescripcion("");
        if(a.getPrecioUnidad() == null) a.setPrecioUnidad(Constantes.CANTIDAD_UNIDAD_DOUBLE);
        if(a.getCodigo() == null)    a.setCodigo("");
        if(a.getAfectoIgv() == null) a.setAfectoIgv(Constantes.REGISTRO_ACTIVO);
        if(a.getAfectoIsc() == null) a.setAfectoIsc(Constantes.REGISTRO_INACTIVO);
        if(a.getAfectoIsc().intValue() == Constantes.REGISTRO_INACTIVO.intValue()) {
            a.setTipoTasaIsc(Constantes.REGISTRO_INACTIVO);
            a.setTasaIsc(Constantes.CANTIDAD_ZERO_DOUBLE);
        }

        a.setNombre(a.getNombre().trim());
        a.setDescripcion(a.getDescripcion().trim());
        a.setCodigo(a.getCodigo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Servicio";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public int modificar(Servicio a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDescripcion() == null) a.setDescripcion("");
        if(a.getPrecioUnidad() == null) a.setPrecioUnidad(Constantes.CANTIDAD_UNIDAD_DOUBLE);
        if(a.getCodigo() == null)    a.setCodigo("");
        if(a.getAfectoIgv() == null) a.setAfectoIgv(Constantes.REGISTRO_ACTIVO);
        if(a.getAfectoIsc() == null) a.setAfectoIsc(Constantes.REGISTRO_INACTIVO);
        if(a.getAfectoIsc().intValue() == Constantes.REGISTRO_INACTIVO.intValue()) {
            a.setTipoTasaIsc(Constantes.REGISTRO_INACTIVO);
            a.setTasaIsc(Constantes.CANTIDAD_ZERO_DOUBLE);
        }

        a.setNombre(a.getNombre().trim());
        a.setDescripcion(a.getDescripcion().trim());
        a.setCodigo(a.getCodigo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Servicio";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<Servicio> listar() throws Exception {
        //return servicioDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        return servicioMapper.listByParameterMap(params);
    }

    @Override
    public Servicio listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Servicio> servicios = servicioMapper.listByParameterMap(params);

        if(servicios.size() > 0)
            return servicios.get(0);
        else
            return null;

        //return bancoDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
        // servicioDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else{
            String errorValidacion = "Error de validación Método Eliminar Servicio";

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
    public Servicio grabarRegistro(Servicio a) throws Exception {
        return servicioDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Servicio b) throws Exception {

        BigDecimal precioUnidad = new BigDecimal(b.getPrecioUnidad());
        precioUnidad.setScale(2, RoundingMode.HALF_UP);
        //return bancoDAO.modificar(a);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", b.getId());
        params.put("NOMBRE", b.getNombre());
        params.put("DESCRIPCION", b.getDescripcion());
        params.put("PRECIO_UNIDAD", precioUnidad);
        params.put("CODIGO", b.getCodigo());
        params.put("AFECTO_ISC", b.getAfectoIsc());
        params.put("TIPO_TASA_ISC", b.getTipoTasaIsc());
        params.put("TASA_ISC", b.getTasaIsc());
        params.put("AFECTO_IGV", b.getAfectoIgv());
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        return servicioMapper.updateByPrimaryKeySelective(params);
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
        int res= servicioMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Servicio indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }

    //Métodos de Validación

    @Override
    public boolean validacionRegistro(Servicio p, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(p.getAfectoIsc().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            if(p.getTipoTasaIsc() == null){
                resultado = false;
                error = "Ingrese el Tipo de Tasa de ISC";
                errors.add(error);
            }
            if(p.getTasaIsc() == null || p.getTasaIsc() <= Constantes.CANTIDAD_ZERO_DOUBLE){
                resultado = false;
                error = "Ingrese la Tasa del ISC";
                errors.add(error);
            }

            if(p.getTipoTasaIsc() != null && p.getTipoTasaIsc().intValue() == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                if(p.getTasaIsc() != null && ( p.getTasaIsc() < Constantes.CANTIDAD_ZERO_DOUBLE ||
                        p.getTasaIsc() > Constantes.CANTIDAD_CIEN_DOUBLE)){
                    resultado = false;
                    error = "Si la Tasa de ISC es porcentual, el valor no puede ser menor a cero o mayor a 100";
                    errors.add(error);
                }
            }
        }
        else{
            p.setTasaIsc(null);
            p.setTipoTasaIsc(null);
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",p.getNombre());
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Servicio> serviciosV1 = servicioMapper.listByParameterMap(params);

        if(serviciosV1.size() > 0){
            resultado = false;
            error = "El nombre del Servicio ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        params.put("CODIGO",p.getCodigo());
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Servicio> serviciosV2 = servicioMapper.listByParameterMap(params);

        if(serviciosV2.size() > 0){
            resultado = false;
            error = "El código ingresado ya se encuentra registrado";
            errors.add(error);
        }

        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Servicio p , Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(p.getAfectoIsc().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            if(p.getTipoTasaIsc() == null){
                resultado = false;
                error = "Ingrese el Tipo de Tasa de ISC";
                errors.add(error);
            }
            if(p.getTasaIsc() == null || p.getTasaIsc() <= Constantes.CANTIDAD_ZERO_DOUBLE){
                resultado = false;
                error = "Ingrese la Tasa del ISC";
                errors.add(error);
            }

            if(p.getTipoTasaIsc() != null && p.getTipoTasaIsc().intValue() == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                if(p.getTasaIsc() != null && ( p.getTasaIsc() < Constantes.CANTIDAD_ZERO_DOUBLE ||
                        p.getTasaIsc() > Constantes.CANTIDAD_CIEN_DOUBLE)){
                    resultado = false;
                    error = "Si la Tasa de ISC es porcentual, el valor no puede ser menor a cero o mayor a 100";
                    errors.add(error);
                }
            }
        }
        else{
            p.setTasaIsc(null);
            p.setTipoTasaIsc(null);
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_ID",p.getId());
        params.put("NOMBRE",p.getNombre());
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Servicio> serviciosV1 = servicioMapper.listByParameterMap(params);

        if(serviciosV1.size() > 0){
            resultado = false;
            error = "El nombre del Servicio ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        params.put("NO_ID",p.getId());
        params.put("CODIGO",p.getCodigo());
        params.put("EMPRESA_ID",p.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Servicio> serviciosV2 = servicioMapper.listByParameterMap(params);

        if(serviciosV2.size() > 0){
            resultado = false;
            error = "El código ingresado ya se encuentra registrado";
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

        //Lógica de Validaciones para Eliminación Producto

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    public Page<Servicio> listar(Pageable page, String buscar) throws Exception {
        //return servicioDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = servicioMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Servicio> servicios = servicioMapper.listByParameterMap(params);

        return new PageImpl<>(servicios, page, total);
    }
}

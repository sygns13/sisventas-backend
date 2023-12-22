package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.InitComprobanteDAO;
import com.bcs.ventas.dao.mappers.InitComprobanteMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.InitComprobante;
import com.bcs.ventas.service.InitComprobanteService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import org.apache.commons.lang.StringUtils;
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
public class InitComprobanteServiceImpl implements InitComprobanteService {

    @Autowired
    InitComprobanteDAO initComprobanteDAO;

    @Autowired
    InitComprobanteMapper initComprobanteMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;


    @Override
    public InitComprobante registrar(InitComprobante a) throws Exception {
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //Oauth inicio
        a.setEmpresaId(claimsAuthorization.getEmpresaId());
        a.setUserId(claimsAuthorization.getUserId());
        //Oauth final

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        a.setCantidadDigitos(8);
        a.setNumeroActual(a.getNumero());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Inicio de Comprobante";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(InitComprobante a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Inicio de Comprobante";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<InitComprobante> listar() throws Exception {
        //return bancoDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        List<InitComprobante> initComprobantes = initComprobanteMapper.listByParameterMap(params);

        initComprobantes.forEach((initComprobante) -> {
            initComprobante.setLetraSerieStr(initComprobante.getTipoComprobante().getPrefix() + initComprobante.getLetraSerie());
            initComprobante.setNumSerieStr(StringUtils.leftPad(initComprobante.getNumSerie().toString(), 2, "0"));
            initComprobante.setNumeroStr(StringUtils.leftPad(initComprobante.getNumero().toString(), 8, "0"));
            initComprobante.setNumeroActualStr(StringUtils.leftPad(initComprobante.getNumeroActual().toString(), 8, "0"));
        });

        return initComprobantes;
    }

    @Override
    public List<InitComprobante> listar(Long tipoComprobante , Long almacenId) throws Exception {
        //return bancoDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("ALMACEN_ID_GENERAL",Constantes.CANTIDAD_ZERO_LONG);

        if(tipoComprobante.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("TIPO_COMPROBANTE_ID",tipoComprobante);
        }

        if(almacenId.compareTo(Constantes.CANTIDAD_ZERO_LONG) >= 0){
            params.put("ALMACEN_ID",almacenId);
        }

        List<InitComprobante> initComprobantes = initComprobanteMapper.listByParameterMap(params);

        initComprobantes.forEach((initComprobante) -> {
            initComprobante.setLetraSerieStr(initComprobante.getTipoComprobante().getPrefix() + initComprobante.getLetraSerie());
            initComprobante.setNumSerieStr(StringUtils.leftPad(initComprobante.getNumSerie().toString(), 2, "0"));
            initComprobante.setNumeroStr(StringUtils.leftPad(initComprobante.getNumero().toString(), 8, "0"));
            initComprobante.setNumeroActualStr(StringUtils.leftPad(initComprobante.getNumeroActual().toString(), 8, "0"));
        });

        return initComprobantes;
    }

    public Page<InitComprobante> listar(Pageable page, String buscar, Long tipoComprobante , Long almacenId) throws Exception {
        //return bancoDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");
        params.put("ALMACEN_ID_GENERAL",Constantes.CANTIDAD_ZERO_LONG);

        if(tipoComprobante.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("TIPO_COMPROBANTE_ID",tipoComprobante);
        }


        if(almacenId.compareTo(Constantes.CANTIDAD_ZERO_LONG) >= 0){
            params.put("ALMACEN_ID",almacenId);
        }

        Long total = initComprobanteMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<InitComprobante> initComprobantes = initComprobanteMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        initComprobantes.forEach((initComprobante) -> {
            initComprobante.setLetraSerieStr(initComprobante.getTipoComprobante().getPrefix() + initComprobante.getLetraSerie());
            initComprobante.setNumSerieStr(StringUtils.leftPad(initComprobante.getNumSerie().toString(), 2, "0"));
            initComprobante.setNumeroStr(StringUtils.leftPad(initComprobante.getNumero().toString(), 8, "0"));
            initComprobante.setNumeroActualStr(StringUtils.leftPad(initComprobante.getNumeroActual().toString(), 8, "0"));
        });

        return new PageImpl<>(initComprobantes, page, total);
    }

    @Override
    public InitComprobante listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<InitComprobante> initComprobantes = initComprobanteMapper.listByParameterMap(params);

        initComprobantes.forEach((initComprobante) -> {
            initComprobante.setLetraSerieStr(initComprobante.getTipoComprobante().getPrefix() + initComprobante.getLetraSerie());
            initComprobante.setNumSerieStr(StringUtils.leftPad(initComprobante.getNumSerie().toString(), 2, "0"));
            initComprobante.setNumeroStr(StringUtils.leftPad(initComprobante.getNumero().toString(), 8, "0"));
            initComprobante.setNumeroActualStr(StringUtils.leftPad(initComprobante.getNumeroActual().toString(), 8, "0"));
        });

        if(initComprobantes.size() > 0)
            return initComprobantes.get(0);
        else
            return null;

        //return bancoDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
        // bancoDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else{
            String errorValidacion = "Error de validación Método Eliminar Inicio de Comprobante";

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
    public InitComprobante grabarRegistro(InitComprobante a) throws Exception {
        a.setLetraSerie(a.getLetraSerie().toUpperCase());
        return initComprobanteDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(InitComprobante b) throws Exception {

        b.setLetraSerie(b.getLetraSerie().toUpperCase());
        //return bancoDAO.modificar(a);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", b.getId());
        params.put("NUM_SERIE", b.getNumSerie());
        params.put("LETRA_SERIE", b.getLetraSerie());
        params.put("NUMERO", b.getNumero());
        params.put("NUMERO_ACTUAL", b.getNumeroActual());
        params.put("CANTIDAD_DIGITOS", b.getCantidadDigitos());
        params.put("ALMACEN_ID", b.getAlmacenId());

        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        return initComprobanteMapper.updateByPrimaryKeySelective(params);
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
        int res= initComprobanteMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Banco indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Override
    public boolean validacionRegistro(InitComprobante a, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(a.getLetraSerie() == null || a.getLetraSerie().trim().isEmpty()){
            resultado = false;
            error = "Ingrese el componente de letra de la serie del comprobante";
            errors.add(error);
        }

        if(a.getNumSerie() == null || a.getNumSerie().intValue() < Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
            resultado = false;
            error = "Ingrese el componente numérico de la serie del comprobante";
            errors.add(error);
        }

        if(a.getNumero() == null || a.getNumero().intValue() < Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
            resultado = false;
            error = "Ingrese el numérico inicial del comprobante";
            errors.add(error);
        }

        Long tipoComprobante = 0L;
        if(a.getTipoComprobante() == null || a.getTipoComprobante().getId() == null || a.getTipoComprobante().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Seleccione el tipo de comprobante";
            errors.add(error);
        }
        else{
            tipoComprobante = a.getTipoComprobante().getId();
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NUM_SERIE", a.getNumSerie());
        params.put("TIPO_COMPROBANTE_ID", tipoComprobante);
        params.put("LETRA_SERIE", a.getLetraSerie());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", a.getEmpresaId());

        List<InitComprobante> initComprobantes = initComprobanteMapper.listByParameterMap(params);

        if(initComprobantes.size() > 0){
            resultado = false;
            error = "El registro de Inicio de Comprobante ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(InitComprobante a, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(a.getLetraSerie() == null || a.getLetraSerie().trim().isEmpty()){
            resultado = false;
            error = "Ingrese el componente de letra de la serie del comprobante";
            errors.add(error);
        }

        if(a.getNumSerie() == null || a.getNumSerie().intValue() < Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
            resultado = false;
            error = "Ingrese el componente numérico de la serie del comprobante";
            errors.add(error);
        }

        if(a.getNumero() == null || a.getNumero().intValue() < Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
            resultado = false;
            error = "Ingrese el numérico inicial del comprobante";
            errors.add(error);
        }

        Long tipoComprobante = 0L;
        if(a.getTipoComprobante() == null || a.getTipoComprobante().getId() == null || a.getTipoComprobante().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Seleccione el tipo de comprobante";
            errors.add(error);
        }
        else{
            tipoComprobante = a.getTipoComprobante().getId();
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_ID",a.getId());
        params.put("NUM_SERIE", a.getNumSerie());
        params.put("TIPO_COMPROBANTE_ID", tipoComprobante);
        params.put("LETRA_SERIE", a.getLetraSerie());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", a.getEmpresaId());

        List<InitComprobante> initComprobantes = initComprobanteMapper.listByParameterMap(params);

        if(initComprobantes.size() > 0){
            resultado = false;
            error = "El registro de Inicio de Comprobante ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionEliminacion(Long aLong, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //Lógica de Validaciones para Eliminación Banco

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void altabaja(Long id, Integer valor, Long userId) throws Exception {
        LocalDateTime fechaActual = LocalDateTime.now();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", id);
        params.put("ACTIVO", valor);

        params.put("USER_ID", userId);
        params.put("UPDATED_AT", fechaActual);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        initComprobanteMapper.updateByPrimaryKeySelective(params);
    }

}

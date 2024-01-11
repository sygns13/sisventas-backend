package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.IngresoSalidaCajaDAO;
import com.bcs.ventas.dao.mappers.IngresoSalidaCajaMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.dto.EgresosOtrosDTO;
import com.bcs.ventas.model.dto.IngresosOtrosDTO;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.model.entities.User;
import com.bcs.ventas.service.IngresoSalidaCajaService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class IngresoSalidaCajaServiceImpl implements IngresoSalidaCajaService {


    @Autowired
    private IngresoSalidaCajaDAO ingresoSalidaCajaDAO;

    @Autowired
    private IngresoSalidaCajaMapper ingresoSalidaCajaMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public IngresoSalidaCaja registrar(IngresoSalidaCaja a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //Oauth inicio
        a.setEmpresaId(claimsAuthorization.getEmpresaId());
        User user = new User();
        user.setId(claimsAuthorization.getUserId());
        a.setUser(user);
        //Oauth final

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        if(a.getConcepto() == null)    a.setConcepto("");
        if(a.getComprobante() == null) a.setComprobante("");

        a.setConcepto(a.getConcepto().trim());
        a.setComprobante(a.getComprobante().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar IngresoSalidaCaja";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public int modificar(IngresoSalidaCaja a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getConcepto() == null)    a.setConcepto("");
        if(a.getComprobante() == null) a.setComprobante("");

        a.setConcepto(a.getConcepto().trim());
        a.setComprobante(a.getComprobante().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar IngresoSalidaCaja";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<IngresoSalidaCaja> listar() throws Exception {
        //return ingresoSalidaCajaDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);

        return ingresoSalidaCajaMapper.listByParameterMap(params);
    }

    @Override
    public Page<IngresoSalidaCaja> listar(Pageable page, String buscar, Long almacenId) throws Exception {
        //return ingresoSalidaCajaDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);

        if(almacenId != null && almacenId.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",almacenId);

        Long total = ingresoSalidaCajaMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<IngresoSalidaCaja> ingresoSalidaCajas = ingresoSalidaCajaMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return new PageImpl<>(ingresoSalidaCajas, page, total);
    }


    @Override
    public IngresosOtrosDTO listarIngresosReporte(Pageable page, FiltroGeneral filtros) throws Exception {
        //return ingresoSalidaCajaDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        //params.put("BUSCAR","%"+buscar+"%");
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);
        params.put("TIPO", Constantes.TIPO_ENTRADA_PRODUCTOS);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getFechaInicio() != null && filtros.getFechaFinal() != null){
            params.put("FECHA_INI", filtros.getFechaInicio());
            params.put("FECHA_FIN", filtros.getFechaFinal());
        }

        if(filtros.getTipoComprobanteOtros() != null && filtros.getTipoComprobanteOtros().compareTo(Constantes.CANTIDAD_UNIDAD_NEGATIVE_INTEGER) > 0)
            params.put("TIPO_COMPROBANTE", filtros.getTipoComprobanteOtros());

        Long total = ingresoSalidaCajaMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        BigDecimal totalMonto = ingresoSalidaCajaMapper.getTotalIngresosOtros(params);
        if(totalMonto == null) totalMonto = Constantes.CANTIDAD_ZERO_BIG_DECIMAL;

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<IngresoSalidaCaja> ingresoSalidaCajas = ingresoSalidaCajaMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        IngresosOtrosDTO ingresosOtrosDTO = new IngresosOtrosDTO();

        ingresosOtrosDTO.setIngresos(new PageImpl<>(ingresoSalidaCajas, page, total));
        ingresosOtrosDTO.setMontoTotal(totalMonto);

        return ingresosOtrosDTO;
    }

    @Override
    public EgresosOtrosDTO listarEgresosReporte(Pageable page, FiltroGeneral filtros) throws Exception {
        //return ingresoSalidaCajaDAO.listar();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        //params.put("BUSCAR","%"+buscar+"%");
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);
        params.put("TIPO", Constantes.TIPO_RETIRO_PRODUCTOS);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getFechaInicio() != null && filtros.getFechaFinal() != null){
            params.put("FECHA_INI", filtros.getFechaInicio());
            params.put("FECHA_FIN", filtros.getFechaFinal());
        }

        if(filtros.getTipoComprobanteOtros() != null && filtros.getTipoComprobanteOtros().compareTo(Constantes.CANTIDAD_UNIDAD_NEGATIVE_INTEGER) > 0)
            params.put("TIPO_COMPROBANTE", filtros.getTipoComprobanteOtros());

        Long total = ingresoSalidaCajaMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        BigDecimal totalMonto = ingresoSalidaCajaMapper.getTotalIngresosOtros(params);
        if(totalMonto == null) totalMonto = Constantes.CANTIDAD_ZERO_BIG_DECIMAL;

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<IngresoSalidaCaja> ingresoSalidaCajas = ingresoSalidaCajaMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        EgresosOtrosDTO egresosOtrosDTO = new EgresosOtrosDTO();

        egresosOtrosDTO.setEgresos(new PageImpl<>(ingresoSalidaCajas, page, total));
        egresosOtrosDTO.setMontoTotal(totalMonto);

        return egresosOtrosDTO;
    }

    @Override
    public IngresoSalidaCaja listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<IngresoSalidaCaja> ingresoSalidaCajas = ingresoSalidaCajaMapper.listByParameterMap(params);

        if(ingresoSalidaCajas.size() > 0)
            return ingresoSalidaCajas.get(0);
        else
            return null;

        //return ingresoSalidaCajaDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
        // ingresoSalidaCajaDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else{
            String errorValidacion = "Error de validación Método Eliminar ingresoSalidaCaja";

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
    public IngresoSalidaCaja grabarRegistro(IngresoSalidaCaja a) throws Exception {
        return ingresoSalidaCajaDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(IngresoSalidaCaja b) throws Exception {
        //return ingresoSalidaCajaDAO.modificar(a);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", b.getId());
        params.put("MONTO", b.getMonto());
        params.put("CONCEPTO", b.getConcepto());
        params.put("COMPROBANTE", b.getComprobante());
        params.put("FECHA", b.getFecha());
        params.put("ALMACEN_ID", b.getAlmacen().getId());
        params.put("TIPO", b.getTipo());
        params.put("TIPO_COMPROBANTE", b.getTipoComprobante());
        params.put("HORA", b.getHora());

        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        return ingresoSalidaCajaMapper.updateByPrimaryKeySelective(params);
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
        int res= ingresoSalidaCajaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el IngresoSalidaCaja indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //Métodos de Validación

    @Override
    public boolean validacionRegistro(IngresoSalidaCaja a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        if(a.getMonto() == null || ( a.getMonto().compareTo(Constantes.CANTIDAD_ZERO_BIG_DECIMAL) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el Monto del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getConcepto() == null || a.getConcepto().isEmpty()){
            resultado = false;
            error = "Debe de remitir correctamente el Concepto del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getFecha() == null){
            resultado = false;
            error = "Debe de remitir correctamente la Fecha del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getHora() == null){
            resultado = false;
            error = "Debe de remitir correctamente la Hora del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getAlmacen() == null || a.getAlmacen().getId() == null || ( a.getAlmacen().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el Local o Sucursal del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getTipo() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Tipo del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getTipoComprobante() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Tipo de Comprobante del Ingreso o Salida de Caja";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(IngresoSalidaCaja a , Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(a.getMonto() == null || ( a.getMonto().compareTo(Constantes.CANTIDAD_ZERO_BIG_DECIMAL) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el Monto del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getConcepto() == null || a.getConcepto().isEmpty()){
            resultado = false;
            error = "Debe de remitir correctamente el Concepto del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getFecha() == null){
            resultado = false;
            error = "Debe de remitir correctamente la Fecha del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getHora() == null){
            resultado = false;
            error = "Debe de remitir correctamente la Hora del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getAlmacen() == null || a.getAlmacen().getId() == null || ( a.getAlmacen().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el Local o Sucursal del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getTipo() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Tipo del Ingreso o Salida de Caja";
            errors.add(error);
        }
        if(a.getTipoComprobante() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Tipo de Comprobante del Ingreso o Salida de Caja";
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

        //Lógica de Validaciones para Eliminación IngresoSalidaCaja

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

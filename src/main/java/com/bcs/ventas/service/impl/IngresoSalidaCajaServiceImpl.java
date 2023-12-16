package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.IngresoSalidaCajaDAO;
import com.bcs.ventas.dao.mappers.IngresoSalidaCajaMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.model.entities.User;
import com.bcs.ventas.service.IngresoSalidaCajaService;
import com.bcs.ventas.utils.Constantes;
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
public class IngresoSalidaCajaServiceImpl implements IngresoSalidaCajaService {


    @Autowired
    private IngresoSalidaCajaDAO ingresoSalidaCajaDAO;

    @Autowired
    private IngresoSalidaCajaMapper ingresoSalidaCajaMapper;

    @Override
    public IngresoSalidaCaja registrar(IngresoSalidaCaja a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        a.setEmpresaId(1L);
        User user = new User();
        user.setId(2L);
        a.setUser(user);
        //Todo: Temporal hasta incluir Oauth final

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

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

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

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

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

    //TODO: Métodos de Grabado
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

        params.put("USER_ID", b.getUser().getId());
        params.put("UPDATED_AT", b.getUpdatedAd());

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
        params.put("UPDATED_AT",fechaUpdate);

        int res= ingresoSalidaCajaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el IngresoSalidaCaja indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //TODO: Métodos de Validación

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

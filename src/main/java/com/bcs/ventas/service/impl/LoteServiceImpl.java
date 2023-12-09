package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.LoteMapper;
import com.bcs.ventas.dao.mappers.StockMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.dto.LotesChangeOrdenDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.LoteService;
import com.bcs.ventas.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class LoteServiceImpl implements LoteService {

    @Autowired
    private UnidadMapper unidadMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private LoteMapper loteMapper;

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private LoteDAO loteDAO;

    @Autowired
    private RetiroEntradaProductoDAO retiroEntradaProductoDAO;

    @Autowired
    private UnidadDAO unidadDAO;

    @Autowired
    private ProductoDAO productoDAO;


    @Override
    public Lote registrar(Lote lote) throws Exception {
        LocalDateTime fechaActual = LocalDateTime.now();
        lote.setCreatedAt(fechaActual);
        lote.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        lote.setEmpresaId(1L);
        lote.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        lote.setActivo(Constantes.REGISTRO_ACTIVO);
        lote.setBorrado(Constantes.REGISTRO_NO_BORRADO);

        if(lote.getNombre() == null)    lote.setNombre("");
        lote.setNombre(lote.getNombre());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(lote, resultValidacion);

        if(validacion){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
            params.put("EMPRESA_ID",lote.getEmpresaId());
            params.put("ALMACEN_ID", lote.getAlmacenId());
            params.put("PRODUCTO_ID", lote.getProductoId());

            Long cantLotes = loteMapper.getTotalElements(params);
            cantLotes ++;
            lote.setOrden(cantLotes);

            return this.grabarRegistro(lote);
        }

        String errorValidacion = "Error de validación Método Registrar Lote";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public Lote registrarNuevoLote(Lote lote) throws Exception {
        LocalDateTime fechaActual = LocalDateTime.now();
        lote.setCreatedAt(fechaActual);
        lote.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        lote.setEmpresaId(1L);
        lote.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        lote.setActivo(Constantes.REGISTRO_ACTIVO);
        lote.setBorrado(Constantes.REGISTRO_NO_BORRADO);

        if(lote.getNombre() == null)    lote.setNombre("");
        lote.setNombre(lote.getNombre());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistroNuevoLote(lote, resultValidacion);

        if(validacion){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
            params.put("EMPRESA_ID",lote.getEmpresaId());
            params.put("ALMACEN_ID", lote.getAlmacenId());
            params.put("PRODUCTO_ID", lote.getProductoId());

            Long cantLotes = loteMapper.getTotalElements(params);
            cantLotes ++;
            lote.setOrden(cantLotes);

            return this.grabarRegistroNuevoLote(lote);
        }

        String errorValidacion = "Error de validación Método Registrar Lote";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public Lote registrarOnlyNuevoLote(Lote lote) throws Exception {
        LocalDateTime fechaActual = LocalDateTime.now();
        lote.setCreatedAt(fechaActual);
        lote.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        lote.setEmpresaId(1L);
        lote.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        lote.setActivo(Constantes.REGISTRO_ACTIVO_2);
        lote.setBorrado(Constantes.REGISTRO_NO_BORRADO);

        if(lote.getNombre() == null)    lote.setNombre("");
        lote.setNombre(lote.getNombre());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistroNuevoLote(lote, resultValidacion);

        if(validacion){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
            params.put("EMPRESA_ID",lote.getEmpresaId());
            params.put("ALMACEN_ID", lote.getAlmacenId());
            params.put("PRODUCTO_ID", lote.getProductoId());

            Long cantLotes = loteMapper.getTotalElements(params);
            cantLotes ++;
            lote.setOrden(cantLotes);

            return this.grabarRegistroOnlyNuevoLote(lote);
        }

        String errorValidacion = "Error de validación Método Registrar Lote";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public void modificarOrden(LotesChangeOrdenDTO lotes) throws Exception {

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificarOrden(lotes, resultValidacion);

        if(validacion){
            this.grabarModificarOrden(lotes);
            return;
        }

        String errorValidacion = "Error de validación Método Modificar Orden de Lote";

        if (resultValidacion.get("errors") != null) {
            List<String> errors = (List<String>) resultValidacion.get("errors");
            if (errors.size() > 0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);


    }


    @Override
    public int modificar(Lote lote) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        lote.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        lote.setEmpresaId(1L);
        lote.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        if(lote.getNombre() == null)    lote.setNombre("");
        lote.setNombre(lote.getNombre());


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(lote, resultValidacion);

        if(validacion)
            return this.grabarRectificar(lote);

        String errorValidacion = "Error de validación Método Modificar Lote";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<Lote> listar() throws Exception {
        return null;
    }

    @Override
    public Lote listarPorId(Long id) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Lote> lotes = loteMapper.listByParameterMap(params);

        if(lotes.size() > 0)
            return lotes.get(0);
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
        else {
            String errorValidacion = "Error de validación Método Eliminar producto";

            if (resultValidacion.get("errors") != null) {
                List<String> errors = (List<String>) resultValidacion.get("errors");
                if (errors.size() > 0)
                    errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
            }

            throw new ValidationServiceException(errorValidacion);
        }

    }

    @Override
    public void eliminarOnlyLote(Long id) throws Exception {

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminarDeletedLote(id);
        }
        else {
            String errorValidacion = "Error de validación Método Eliminar Lote";

            if (resultValidacion.get("errors") != null) {
                List<String> errors = (List<String>) resultValidacion.get("errors");
                if (errors.size() > 0)
                    errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
            }

            throw new ValidationServiceException(errorValidacion);
        }

    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public Lote grabarRegistro(Lote lote) throws Exception {

        if(lote.getActivoVencimiento().intValue() == Constantes.REGISTRO_INACTIVO.intValue()){
            lote.setFechaVencimiento(null);
        }

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",lote.getEmpresaId());
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);

        lote.setUnidadId(unidadG1.get(0).getId());

        lote = loteDAO.registrar(lote);

        params.clear();
        params.put("PRODUCTO_ID",lote.getProductoId());
        params.put("ALMACEN_ID",lote.getAlmacenId());
        params.put("EMPRESA_ID",lote.getEmpresaId());

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        if(stockG1.size()>0){
            Stock stockModificar = stockG1.get(0);
            stockModificar.setCantidad(stockModificar.getCantidad() + lote.getCantidad());
            stockModificar.setUpdatedAd(lote.getUpdatedAd());

            stockDAO.modificar(stockModificar);
        }
        else{
            Stock stockRegistrar = new Stock();
            stockRegistrar.setAlmacenId(lote.getAlmacenId());
            stockRegistrar.setProductoId(lote.getProductoId());
            stockRegistrar.setCantidad(lote.getCantidad());
            stockRegistrar.setActivo(Constantes.REGISTRO_ACTIVO);
            stockRegistrar.setBorrado(Constantes.REGISTRO_NO_BORRADO);
            stockRegistrar.setUserId(lote.getUserId());
            stockRegistrar.setEmpresaId(lote.getEmpresaId());
            stockRegistrar.setCreatedAt(lote.getCreatedAt());
            stockRegistrar.setUpdatedAd(lote.getUpdatedAd());

            stockDAO.registrar(stockRegistrar);
        }

        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        LocalDateTime fechaActualDateTime = LocalDateTime.now();

        RetiroEntradaProducto retiroEntradaProducto = new RetiroEntradaProducto();

        retiroEntradaProducto.setFecha(fechaActual);
        retiroEntradaProducto.setMotivo(Constantes.MOTIVO_INGRESO_CREACION_LOTE);
        retiroEntradaProducto.setLoteId(lote.getId());
        retiroEntradaProducto.setHora(horaActual);
        retiroEntradaProducto.setCantidadReal(lote.getCantidad());
        retiroEntradaProducto.setUserId(lote.getUserId());
        retiroEntradaProducto.setEmpresaId(lote.getEmpresaId());
        retiroEntradaProducto.setActivo(Constantes.REGISTRO_ACTIVO);
        retiroEntradaProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        retiroEntradaProducto.setTipo(Constantes.TIPO_ENTRADA_PRODUCTOS);
        retiroEntradaProducto.setAlmacenId(lote.getAlmacenId());
        retiroEntradaProducto.setProductoId(lote.getProductoId());
        retiroEntradaProducto.setCreatedAt(fechaActualDateTime);
        retiroEntradaProducto.setUpdatedAd(fechaActualDateTime);


        retiroEntradaProductoDAO.registrar(retiroEntradaProducto);

        return lote;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private Lote grabarRegistroOnlyNuevoLote(Lote lote) throws Exception {

        if(lote.getActivoVencimiento().intValue() == Constantes.REGISTRO_INACTIVO.intValue()){
            lote.setFechaVencimiento(null);
        }

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",lote.getEmpresaId());
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);

        lote.setUnidadId(unidadG1.get(0).getId());

        lote = loteDAO.registrar(lote);

        return lote;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private Lote grabarRegistroNuevoLote(Lote lote) throws Exception {

        if(lote.getActivoVencimiento().intValue() == Constantes.REGISTRO_INACTIVO.intValue()){
            lote.setFechaVencimiento(null);
        }

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",lote.getEmpresaId());
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);

        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);

        lote.setUnidadId(unidadG1.get(0).getId());

        lote = loteDAO.registrar(lote);

        params.clear();
        params.put("PRODUCTO_ID",lote.getProductoId());
        params.put("ALMACEN_ID",lote.getAlmacenId());
        params.put("EMPRESA_ID",lote.getEmpresaId());

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        if(stockG1.size()>0){
            Stock stockModificar = stockG1.get(0);
            stockModificar.setCantidad(stockModificar.getCantidad() + lote.getCantidad());
            stockModificar.setUpdatedAd(lote.getUpdatedAd());

            stockDAO.modificar(stockModificar);
        }
        else{
            Stock stockRegistrar = new Stock();
            stockRegistrar.setAlmacenId(lote.getAlmacenId());
            stockRegistrar.setProductoId(lote.getProductoId());
            stockRegistrar.setCantidad(lote.getCantidad());
            stockRegistrar.setActivo(Constantes.REGISTRO_ACTIVO);
            stockRegistrar.setBorrado(Constantes.REGISTRO_NO_BORRADO);
            stockRegistrar.setUserId(lote.getUserId());
            stockRegistrar.setEmpresaId(lote.getEmpresaId());
            stockRegistrar.setCreatedAt(lote.getCreatedAt());
            stockRegistrar.setUpdatedAd(lote.getUpdatedAd());

            stockDAO.registrar(stockRegistrar);
        }

        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        LocalDateTime fechaActualDateTime = LocalDateTime.now();

        RetiroEntradaProducto retiroEntradaProducto = new RetiroEntradaProducto();

        retiroEntradaProducto.setFecha(fechaActual);
        retiroEntradaProducto.setMotivo(lote.getMotivo());
        retiroEntradaProducto.setLoteId(lote.getId());
        retiroEntradaProducto.setHora(horaActual);
        retiroEntradaProducto.setCantidadReal(lote.getCantidad());
        retiroEntradaProducto.setUserId(lote.getUserId());
        retiroEntradaProducto.setEmpresaId(lote.getEmpresaId());
        retiroEntradaProducto.setActivo(Constantes.REGISTRO_ACTIVO);
        retiroEntradaProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        retiroEntradaProducto.setTipo(Constantes.TIPO_ENTRADA_PRODUCTOS);
        retiroEntradaProducto.setAlmacenId(lote.getAlmacenId());
        retiroEntradaProducto.setProductoId(lote.getProductoId());
        retiroEntradaProducto.setCreatedAt(fechaActualDateTime);
        retiroEntradaProducto.setUpdatedAd(fechaActualDateTime);


        retiroEntradaProductoDAO.registrar(retiroEntradaProducto);

        return lote;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Lote lote) throws Exception {

        LocalDateTime fechaActualUpdated = LocalDateTime.now();

        if(lote.getActivoVencimiento().intValue() == Constantes.REGISTRO_INACTIVO.intValue()){
            lote.setFechaVencimiento(null);
        }

        Lote loteBD = loteDAO.listarPorId(lote.getId());

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("PRODUCTO_ID",lote.getProductoId());
        params.put("ALMACEN_ID",lote.getAlmacenId());
        params.put("EMPRESA_ID",lote.getEmpresaId());

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        if(stockG1.size()>0){
            Stock stockModificar = stockG1.get(0);
            stockModificar.setCantidad(stockModificar.getCantidad() + lote.getCantidad() - loteBD.getCantidad());
            stockModificar.setUpdatedAd(fechaActualUpdated);

            stockDAO.modificar(stockModificar);
        }
        else{
            Stock stockRegistrar = new Stock();
            stockRegistrar.setAlmacenId(lote.getAlmacenId());
            stockRegistrar.setProductoId(lote.getProductoId());
            stockRegistrar.setCantidad(lote.getCantidad());
            stockRegistrar.setActivo(Constantes.REGISTRO_ACTIVO);
            stockRegistrar.setBorrado(Constantes.REGISTRO_NO_BORRADO);
            stockRegistrar.setUserId(lote.getUserId());
            stockRegistrar.setEmpresaId(lote.getEmpresaId());
            stockRegistrar.setCreatedAt(lote.getCreatedAt());
            stockRegistrar.setUpdatedAd(lote.getUpdatedAd());

            stockDAO.registrar(stockRegistrar);
        }

        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        double cantDiferida = lote.getCantidad() - loteBD.getCantidad();

        if(cantDiferida > 0){
            LocalDateTime fechaActualDateTime = LocalDateTime.now();
            RetiroEntradaProducto retiroEntradaProducto = new RetiroEntradaProducto();

            retiroEntradaProducto.setFecha(fechaActual);
            retiroEntradaProducto.setMotivo(Constantes.MOTIVO_INGRESO_MODIFICACION_LOTE);
            retiroEntradaProducto.setLoteId(lote.getId());
            retiroEntradaProducto.setHora(horaActual);
            retiroEntradaProducto.setCantidadReal(cantDiferida);
            retiroEntradaProducto.setUserId(lote.getUserId());
            retiroEntradaProducto.setEmpresaId(lote.getEmpresaId());
            retiroEntradaProducto.setActivo(Constantes.REGISTRO_ACTIVO);
            retiroEntradaProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);
            retiroEntradaProducto.setTipo(Constantes.TIPO_ENTRADA_PRODUCTOS);
            retiroEntradaProducto.setAlmacenId(lote.getAlmacenId());
            retiroEntradaProducto.setProductoId(lote.getProductoId());
            retiroEntradaProducto.setCreatedAt(fechaActualDateTime);
            retiroEntradaProducto.setUpdatedAd(fechaActualDateTime);

            retiroEntradaProductoDAO.registrar(retiroEntradaProducto);

        }else if(cantDiferida < 0){
            LocalDateTime fechaActualDateTime = LocalDateTime.now();
            RetiroEntradaProducto retiroEntradaProducto = new RetiroEntradaProducto();

            retiroEntradaProducto.setFecha(fechaActual);
            retiroEntradaProducto.setMotivo(Constantes.MOTIVO_SALIDA_MODIFICACION_LOTE);
            retiroEntradaProducto.setLoteId(lote.getId());
            retiroEntradaProducto.setHora(horaActual);
            retiroEntradaProducto.setCantidadReal(Math.abs(cantDiferida));
            retiroEntradaProducto.setUserId(lote.getUserId());
            retiroEntradaProducto.setEmpresaId(lote.getEmpresaId());
            retiroEntradaProducto.setActivo(Constantes.REGISTRO_ACTIVO);
            retiroEntradaProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);
            retiroEntradaProducto.setTipo(Constantes.TIPO_RETIRO_PRODUCTOS);
            retiroEntradaProducto.setAlmacenId(lote.getAlmacenId());
            retiroEntradaProducto.setProductoId(lote.getProductoId());
            retiroEntradaProducto.setCreatedAt(fechaActualDateTime);
            retiroEntradaProducto.setUpdatedAd(fechaActualDateTime);

            retiroEntradaProductoDAO.registrar(retiroEntradaProducto);
        }



        loteBD.setNombre(lote.getNombre());
        loteBD.setCantidad(lote.getCantidad());
        loteBD.setFechaVencimiento(lote.getFechaVencimiento());
        loteBD.setFechaIngreso(lote.getFechaIngreso());
        loteBD.setActivoVencimiento(lote.getActivoVencimiento());
        loteBD.setObservacion(lote.getObservacion());
        loteBD.setUpdatedAd(fechaActualUpdated);

        Lote loteEdited = loteDAO.modificar(loteBD);

        if(loteEdited != null){

            return Constantes.CANTIDAD_UNIDAD_INTEGER;
        }

        return Constantes.CANTIDAD_ZERO;

    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void grabarEliminar(Long id) throws Exception {

        LocalDateTime fechaActualUpdated = LocalDateTime.now();

        Lote lote = loteDAO.listarPorId(id);
        Unidad unidad = unidadDAO.listarPorId(lote.getUnidadId());

        lote.setUpdatedAd(fechaActualUpdated);

        Map<String, Object> params = new HashMap<String, Object>();

        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        lote.setBorrado(Constantes.REGISTRO_BORRADO);

        params.put("PRODUCTO_ID",lote.getProductoId());
        params.put("ALMACEN_ID",lote.getAlmacenId());
        params.put("EMPRESA_ID",lote.getEmpresaId());

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        Stock stockModificar = stockG1.get(0);

        double cantidadRestar = lote.getCantidad() * unidad.getCantidad();

        double cantidadFinal = stockModificar.getCantidad() - cantidadRestar;

        if(cantidadFinal < 0){
            cantidadFinal = 0;
        }

        stockModificar.setCantidad(cantidadFinal);
        stockModificar.setUpdatedAd(lote.getUpdatedAd());

        stockDAO.modificar(stockModificar);

        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        RetiroEntradaProducto retiroEntradaProducto = new RetiroEntradaProducto();

        retiroEntradaProducto.setFecha(fechaActual);
        retiroEntradaProducto.setMotivo(Constantes.MOTIVO_SALIDA_ELIMINACION_LOTE);
        retiroEntradaProducto.setLoteId(lote.getId());
        retiroEntradaProducto.setHora(horaActual);
        retiroEntradaProducto.setCantidadReal(Math.abs(cantidadRestar));
        retiroEntradaProducto.setUserId(lote.getUserId());
        retiroEntradaProducto.setEmpresaId(lote.getEmpresaId());
        retiroEntradaProducto.setActivo(Constantes.REGISTRO_ACTIVO);
        retiroEntradaProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        retiroEntradaProducto.setTipo(Constantes.TIPO_RETIRO_PRODUCTOS);
        retiroEntradaProducto.setAlmacenId(lote.getAlmacenId());
        retiroEntradaProducto.setProductoId(lote.getProductoId());
        retiroEntradaProducto.setCreatedAt(fechaActualDateTime);
        retiroEntradaProducto.setUpdatedAd(fechaActualDateTime);

        retiroEntradaProductoDAO.registrar(retiroEntradaProducto);

        loteDAO.modificar(lote);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void grabarEliminarDeletedLote(Long id) throws Exception {
        loteDAO.eliminar(id);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void grabarModificarOrden(LotesChangeOrdenDTO lotes) throws Exception {

        LocalDateTime fechaActualUpdated = LocalDateTime.now();

        Lote lote1 = loteDAO.listarPorId(lotes.getLote1().getId());
        Lote lote2 = loteDAO.listarPorId(lotes.getLote2().getId());

        lotes.getLote1().setOrden(lote1.getOrden());
        lotes.getLote2().setOrden(lote2.getOrden());

        lote1.setUpdatedAd(fechaActualUpdated);
        lote2.setUpdatedAd(fechaActualUpdated);

        lote1.setOrden(lotes.getLote2().getOrden());
        lote2.setOrden(lotes.getLote1().getOrden());

        loteDAO.modificar(lote1);
        loteDAO.modificar(lote2);
    }

    @Override
    public boolean validacionRegistro(Lote lote, Map<String, Object> resultValidacion) {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(lote.getActivoVencimiento().intValue() == Constantes.REGISTRO_ACTIVO.intValue() && lote.getFechaVencimiento() == null)
        {
            resultado = false;
            error = "Debe de ingresar la fecha de Vencimiento si indicó que el vencimiento del lote está activo.";
            errors.add(error);
        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }


    private boolean validacionRegistroNuevoLote(Lote lote, Map<String, Object> resultValidacion) {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(lote.getActivoVencimiento().intValue() == Constantes.REGISTRO_ACTIVO.intValue() && lote.getFechaVencimiento() == null)
        {
            resultado = false;
            error = "Debe de ingresar la fecha de Vencimiento si indicó que el vencimiento del lote está activo.";
            errors.add(error);
        }

        if(lote.getMotivo() == null || lote.getMotivo().isEmpty()){
            resultado = false;
            error = "Debe de ingresar el motivo del Ingreso del Nuevo Lote.";
            errors.add(error);
        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Lote lote, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(lote.getId() == null){
            resultado = false;
            error = "Debe de enviar el ID del Lote";
            errors.add(error);
        }

        if(lote.getActivoVencimiento().intValue() == Constantes.REGISTRO_ACTIVO.intValue() && lote.getFechaVencimiento() == null)
        {
            resultado = false;
            error = "Debe de ingresar la fecha de Vencimiento si indicó que el vencimiento del lote está activo.";
            errors.add(error);
        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion) throws Exception {
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

    public boolean validacionModificarOrden(LotesChangeOrdenDTO lotes, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(lotes.getLote1() == null || lotes.getLote1().getId() == null){
            resultado = false;
            String nameLote = lotes.getLote1() != null && lotes.getLote1().getNombre() != null ? lotes.getLote1().getNombre() : "";
            error = "Debe de enviar el ID del Lote " + nameLote;
            errors.add(error);
        }

        /*if(lotes.getLote1() != null && (lotes.getLote1().getOrden() == null || lotes.getLote1().getOrden().intValue() == Constantes.CANTIDAD_ZERO_LONG.intValue())){
            resultado = false;
            String nameLote = lotes.getLote1() != null && lotes.getLote1().getNombre() != null ? lotes.getLote1().getNombre() : "";
            error = "Debe de enviar el Orden del Lote " + nameLote;
            errors.add(error);
        }*/

        if(lotes.getLote2() == null || lotes.getLote2().getId() == null){
            resultado = false;
            String nameLote = lotes.getLote2() != null && lotes.getLote2().getNombre() != null ? lotes.getLote2().getNombre() : "";
            error = "Debe de enviar el ID del Lote " + nameLote;
            errors.add(error);
        }

        /*if(lotes.getLote2() != null && (lotes.getLote2().getOrden() == null || lotes.getLote2().getOrden().intValue() == Constantes.CANTIDAD_ZERO_LONG.intValue())){
            resultado = false;
            String nameLote = lotes.getLote2() != null && lotes.getLote2().getNombre() != null ? lotes.getLote2().getNombre() : "";
            error = "Debe de enviar el Orden del Lote " + nameLote;
            errors.add(error);
        }*/

        Lote lote1 = this.listarPorId(lotes.getLote1().getId());
        Lote lote2 = this.listarPorId(lotes.getLote2().getId());

        if(lote1 == null){
            resultado = false;
            String nameLote = lotes.getLote1() != null && lotes.getLote1().getNombre() != null ? lotes.getLote1().getNombre() : "";
            error = "EL ID del Lote " + nameLote + " no corresponde a un ID válido";
            errors.add(error);
        }

        if(lote2 == null){
            resultado = false;
            String nameLote = lotes.getLote2() != null && lotes.getLote2().getNombre() != null ? lotes.getLote2().getNombre() : "";
            error = "EL ID del Lote " + nameLote + " no corresponde a un ID válido";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

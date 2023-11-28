package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.CobroVentaDAO;
import com.bcs.ventas.dao.mappers.CobroVentaMapper;
import com.bcs.ventas.dao.mappers.VentaMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.CobroVentaService;
import com.bcs.ventas.service.VentaService;
import com.bcs.ventas.utils.Constantes;
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
public class CobroVentaServiceImpl implements CobroVentaService {

    @Autowired
    private CobroVentaDAO cobroVentaDAO;

    @Autowired
    private CobroVentaMapper cobroVentaMapper;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaMapper ventaMapper;
    @Override
    public void altabaja(Long id, Integer valor) throws Exception {
        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("ACTIVO", valor);
        params.put("UPDATED_AT",fechaUpdate);

        int res= cobroVentaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Override
    public Page<CobroVenta> listar(Pageable page, String buscar) throws Exception {
        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = cobroVentaMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<CobroVenta> cobroVentas = cobroVentaMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return new PageImpl<>(cobroVentas, page, total);
    }

    @Override
    public CobroVenta registrar(CobroVenta cobroVenta) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        Venta v = ventaService.listarPorId(cobroVenta.getVenta().getId());

        v.setUpdatedAd(fechaActualTime);
        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final
        v.setUser(user);

        cobroVenta.setVenta(v);
        cobroVenta.setCreatedAt(fechaActualTime);
        cobroVenta.setUpdatedAd(fechaActualTime);

        //TODO: Temporal hasta incluir Oauth inicio
        cobroVenta.setEmpresaId(1L);
        cobroVenta.setUserId(2L);
        //user.setEmpresaId(1L);
        //user = userDAO.listarPorId(user.getId());
        //Todo: Temporal hasta incluir Oauth final

        cobroVenta.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        cobroVenta.setActivo(Constantes.REGISTRO_ACTIVO);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(cobroVenta, resultValidacion);

        if(validacion){

            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CASH)){
                cobroVenta.setTipoTarjeta(Constantes.VOID);
                cobroVenta.setSiglaTarjeta(Constantes.VOID);
                cobroVenta.setNumeroTarjeta(Constantes.VOID);

                cobroVenta.setBanco(Constantes.VOID);
                cobroVenta.setNumeroCuenta(Constantes.VOID);
                cobroVenta.setCodigoOperacion(Constantes.VOID);

                cobroVenta.setNumeroCelular(Constantes.VOID);
                cobroVenta.setNumeroCheque(Constantes.VOID);
            }
            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){
                cobroVenta.setBanco(Constantes.VOID);
                cobroVenta.setNumeroCuenta(Constantes.VOID);
                cobroVenta.setCodigoOperacion(Constantes.VOID);

                cobroVenta.setNumeroCelular(Constantes.VOID);
                cobroVenta.setNumeroCheque(Constantes.VOID);
            }
            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){
                cobroVenta.setTipoTarjeta(Constantes.VOID);
                cobroVenta.setSiglaTarjeta(Constantes.VOID);
                cobroVenta.setNumeroTarjeta(Constantes.VOID);

                cobroVenta.setNumeroCelular(Constantes.VOID);
                cobroVenta.setNumeroCheque(Constantes.VOID);
            }
            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){
                cobroVenta.setTipoTarjeta(Constantes.VOID);
                cobroVenta.setSiglaTarjeta(Constantes.VOID);
                cobroVenta.setNumeroTarjeta(Constantes.VOID);

                cobroVenta.setBanco(Constantes.VOID);
                cobroVenta.setNumeroCuenta(Constantes.VOID);

                cobroVenta.setNumeroCheque(Constantes.VOID);
            }
            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){
                cobroVenta.setTipoTarjeta(Constantes.VOID);
                cobroVenta.setSiglaTarjeta(Constantes.VOID);
                cobroVenta.setNumeroTarjeta(Constantes.VOID);

                cobroVenta.setNumeroCelular(Constantes.VOID);
                cobroVenta.setNumeroCuenta(Constantes.VOID);
                cobroVenta.setCodigoOperacion(Constantes.VOID);
            }


            return this.grabarRegistro(cobroVenta);
        }


        String errorValidacion = "Error de validación Método Registrar Pago";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(CobroVenta cobroVenta) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        Venta v = ventaService.listarPorId(cobroVenta.getVenta().getId());

        v.setUpdatedAd(fechaActualTime);
        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final
        v.setUser(user);

        cobroVenta.setVenta(v);
        cobroVenta.setUpdatedAd(fechaActualTime);

        //TODO: Temporal hasta incluir Oauth inicio
        cobroVenta.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(cobroVenta, resultValidacion);

        if(validacion){

            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CASH)){
                cobroVenta.setTipoTarjeta(Constantes.VOID);
                cobroVenta.setSiglaTarjeta(Constantes.VOID);
                cobroVenta.setNumeroTarjeta(Constantes.VOID);

                cobroVenta.setBanco(Constantes.VOID);
                cobroVenta.setNumeroCuenta(Constantes.VOID);
                cobroVenta.setCodigoOperacion(Constantes.VOID);

                cobroVenta.setNumeroCelular(Constantes.VOID);
                cobroVenta.setNumeroCheque(Constantes.VOID);
            }
            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){
                cobroVenta.setBanco(Constantes.VOID);
                cobroVenta.setNumeroCuenta(Constantes.VOID);
                cobroVenta.setCodigoOperacion(Constantes.VOID);

                cobroVenta.setNumeroCelular(Constantes.VOID);
                cobroVenta.setNumeroCheque(Constantes.VOID);
            }
            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){
                cobroVenta.setTipoTarjeta(Constantes.VOID);
                cobroVenta.setSiglaTarjeta(Constantes.VOID);
                cobroVenta.setNumeroTarjeta(Constantes.VOID);

                cobroVenta.setNumeroCelular(Constantes.VOID);
                cobroVenta.setNumeroCheque(Constantes.VOID);
            }
            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){
                cobroVenta.setTipoTarjeta(Constantes.VOID);
                cobroVenta.setSiglaTarjeta(Constantes.VOID);
                cobroVenta.setNumeroTarjeta(Constantes.VOID);

                cobroVenta.setBanco(Constantes.VOID);
                cobroVenta.setNumeroCuenta(Constantes.VOID);

                cobroVenta.setNumeroCheque(Constantes.VOID);
            }
            if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){
                cobroVenta.setTipoTarjeta(Constantes.VOID);
                cobroVenta.setSiglaTarjeta(Constantes.VOID);
                cobroVenta.setNumeroTarjeta(Constantes.VOID);

                cobroVenta.setNumeroCelular(Constantes.VOID);
                cobroVenta.setNumeroCuenta(Constantes.VOID);
                cobroVenta.setCodigoOperacion(Constantes.VOID);
            }


            return this.grabarRectificar(cobroVenta);
        }


        String errorValidacion = "Error de validación Método Modificar Pago de Venta";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<CobroVenta> listar() throws Exception {
        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO", Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);
        return cobroVentaMapper.listByParameterMap(params);
    }

    @Override
    public CobroVenta listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<CobroVenta> cobroVentas = cobroVentaMapper.listByParameterMap(params);

        if(cobroVentas.size() > 0)
            return cobroVentas.get(0);
        else
            return null;
    }

    @Override
    public void eliminar(Long id) throws Exception {

        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        CobroVenta cobroVenta = this.listarPorId(id);
        Venta v = ventaService.listarPorId(cobroVenta.getVenta().getId());

        v.setUpdatedAd(fechaActualTime);
        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final
        v.setUser(user);

        cobroVenta.setVenta(v);


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id, cobroVenta);
        }
        else{
            String errorValidacion = "Error de validación Método Eliminar Cobro de Venta";

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
    public CobroVenta grabarRegistro(CobroVenta c) throws Exception {

        c.getVenta().setPagado(Constantes.VENTA_NO_PAGADO);

        if(c.getImporte().compareTo(new BigDecimal(0)) <= 0)
            c.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA);

        if(c.getImporte().compareTo(new BigDecimal(0)) > 0 && c.getImporte().compareTo(c.getVenta().getMontoPorCobrar()) < 0)
            c.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL);

        if(c.getImporte().compareTo(new BigDecimal(0)) > 0 && c.getImporte().compareTo(c.getVenta().getMontoPorCobrar()) >= 0){
            c.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL);
            c.getVenta().setPagado(Constantes.VENTA_SI_PAGADO);
            c.setImporte(c.getVenta().getMontoPorCobrar());
        }

        CobroVenta cobroVenta = cobroVentaDAO.registrar(c);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", c.getVenta().getId());
        params.put("ESTADO", c.getVenta().getEstado());
        params.put("PAGADO", c.getVenta().getPagado());
        params.put("UPDATED_AT", c.getVenta().getUpdatedAd());
        ventaMapper.updateByPrimaryKeySelective(params);


        return cobroVenta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(CobroVenta cobroVenta) throws Exception {

        CobroVenta cobroVentaBD = this.listarPorId(cobroVenta.getId());

        cobroVenta.getVenta().setPagado(Constantes.VENTA_NO_PAGADO);

        if(cobroVenta.getImporte().subtract(cobroVentaBD.getImporte()).compareTo(new BigDecimal(0)) <= 0)
            cobroVenta.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA);

        if(cobroVenta.getImporte().subtract(cobroVentaBD.getImporte()).compareTo(new BigDecimal(0)) > 0 && cobroVenta.getImporte().subtract(cobroVentaBD.getImporte()).compareTo(cobroVenta.getVenta().getMontoPorCobrar()) < 0)
            cobroVenta.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL);

        if(cobroVenta.getImporte().subtract(cobroVentaBD.getImporte()).compareTo(new BigDecimal(0)) > 0 && cobroVenta.getImporte().subtract(cobroVentaBD.getImporte()).compareTo(cobroVenta.getVenta().getMontoPorCobrar()) >= 0){
            cobroVenta.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL);
            cobroVenta.getVenta().setPagado(Constantes.VENTA_SI_PAGADO);
            cobroVenta.setImporte(cobroVenta.getVenta().getMontoPorCobrar().add(cobroVentaBD.getImporte()));
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", cobroVenta.getId());

        params.put("FECHA", cobroVenta.getFecha());
        params.put("METODOS_PAGO_ID", cobroVenta.getMetodoPago().getId());
        params.put("IMPORTE", cobroVenta.getImporte());
        params.put("TIPO_TARJETA", cobroVenta.getTipoTarjeta());
        params.put("SIGLA_TARJETA", cobroVenta.getSiglaTarjeta());
        params.put("NUMERO_TARJETA", cobroVenta.getNumeroTarjeta());
        params.put("BANCO", cobroVenta.getBanco());
        params.put("NUMERO_CUENTA", cobroVenta.getNumeroCuenta());
        params.put("NUMERO_CELULAR", cobroVenta.getNumeroCelular());
        params.put("NUMERO_CHEQUE", cobroVenta.getNumeroCheque());
        params.put("CODIGO_OPERACION", cobroVenta.getCodigoOperacion());

        params.put("USER_ID", cobroVenta.getUserId());
        params.put("UPDATED_AT", cobroVenta.getUpdatedAd());

        int res = cobroVentaMapper.updateByPrimaryKeySelective(params);

        params.clear();
        params.put("ID", cobroVenta.getVenta().getId());
        params.put("ESTADO", cobroVenta.getVenta().getEstado());
        params.put("PAGADO", cobroVenta.getVenta().getPagado());
        params.put("UPDATED_AT", cobroVenta.getVenta().getUpdatedAd());
        ventaMapper.updateByPrimaryKeySelective(params);

        return res;
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
        params.put("UPDATED_AT",fechaUpdate);

        int res= cobroVentaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Banco indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void grabarEliminar(Long id, CobroVenta cobroVenta) throws Exception {

        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("UPDATED_AT",fechaUpdate);

        int res= cobroVentaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Banco indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

        cobroVenta.getVenta().setPagado(Constantes.VENTA_NO_PAGADO);

        if(cobroVenta.getImporte().compareTo(cobroVenta.getVenta().getMontoCobrado()) >= 0)
            cobroVenta.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA);

        if(cobroVenta.getImporte().compareTo(cobroVenta.getVenta().getMontoCobrado()) < 0)
            cobroVenta.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL);

        params.clear();
        params.put("ID", cobroVenta.getVenta().getId());
        params.put("ESTADO", cobroVenta.getVenta().getEstado());
        params.put("PAGADO", cobroVenta.getVenta().getPagado());
        params.put("UPDATED_AT", cobroVenta.getVenta().getUpdatedAd());
        ventaMapper.updateByPrimaryKeySelective(params);

    }

    @Override
    public boolean validacionRegistro(CobroVenta cobroVenta, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(cobroVenta.getFecha() == null)
        {
            resultado = false;
            error = "Debe de ingresar la fecha de Pago";
            errors.add(error);
        }

        if(cobroVenta.getVenta() == null){
            resultado = false;
            error = "La Venta remitida no es válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getVenta().getEstado().intValue() == Constantes.VENTA_ESTADO_ANULADO){
            resultado = false;
            error = "La Venta remitida se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getVenta().getEstado().intValue() == Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL){
            resultado = false;
            error = "La Venta remitida se encuentra cobrada en su totalidad";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getMetodoPago() == null){
            resultado = false;
            error = "El Método de Pago remitido no es válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getMetodoPago().getTipoId() == null){
            resultado = false;
            error = "El Tipo de Método de Pago remitido no es válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){

            if(cobroVenta.getBanco().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Banco Válido";
                errors.add(error);
            }

            if(cobroVenta.getNumeroCuenta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar una Cuenta Bancaria Válida";
                errors.add(error);
            }

            if(cobroVenta.getCodigoOperacion().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Código de Operación";
                errors.add(error);
            }
        }

        if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){

            if(cobroVenta.getTipoTarjeta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Tipo de Tarjeta Válido";
                errors.add(error);
            }

            if(cobroVenta.getNumeroTarjeta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar los 4 últimos dígitos de la tarjeta";
                errors.add(error);
            }
        }

        if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){

            if(cobroVenta.getNumeroCelular().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Número de Celular Válido";
                errors.add(error);
            }

            if(cobroVenta.getCodigoOperacion().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Código de Operación";
                errors.add(error);
            }
        }

        if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){

            if(cobroVenta.getBanco().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Banco Válido";
                errors.add(error);
            }

            if(cobroVenta.getNumeroCheque().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Número de Cheque";
                errors.add(error);
            }
        }

        if(cobroVenta.getImporte() == null || cobroVenta.getImporte().compareTo(new BigDecimal(Constantes.CANTIDAD_ZERO)) < 0){
            resultado = false;
            error = "Debe de remitir un Monto Abonado Válido";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(CobroVenta cobroVenta, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(cobroVenta.getFecha() == null)
        {
            resultado = false;
            error = "Debe de ingresar la fecha de Pago";
            errors.add(error);
        }

        if(cobroVenta.getVenta() == null){
            resultado = false;
            error = "La Venta remitida no es válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getVenta().getEstado().intValue() == Constantes.VENTA_ESTADO_ANULADO){
            resultado = false;
            error = "La Venta remitida se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getMetodoPago() == null){
            resultado = false;
            error = "El Método de Pago remitido no es válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getMetodoPago().getTipoId() == null){
            resultado = false;
            error = "El Tipo de Método de Pago remitido no es válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){

            if(cobroVenta.getBanco().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Banco Válido";
                errors.add(error);
            }

            if(cobroVenta.getNumeroCuenta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar una Cuenta Bancaria Válida";
                errors.add(error);
            }

            if(cobroVenta.getCodigoOperacion().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Código de Operación";
                errors.add(error);
            }
        }

        if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){

            if(cobroVenta.getTipoTarjeta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Tipo de Tarjeta Válido";
                errors.add(error);
            }

            if(cobroVenta.getNumeroTarjeta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar los 4 últimos dígitos de la tarjeta";
                errors.add(error);
            }
        }

        if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){

            if(cobroVenta.getNumeroCelular().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Número de Celular Válido";
                errors.add(error);
            }

            if(cobroVenta.getCodigoOperacion().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Código de Operación";
                errors.add(error);
            }
        }

        if(cobroVenta.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){

            if(cobroVenta.getBanco().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Banco Válido";
                errors.add(error);
            }

            if(cobroVenta.getNumeroCheque().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Número de Cheque";
                errors.add(error);
            }
        }

        if(cobroVenta.getImporte() == null || cobroVenta.getImporte().compareTo(new BigDecimal(Constantes.CANTIDAD_ZERO)) < 0){
            resultado = false;
            error = "Debe de remitir un Monto Abonado Válido";
            errors.add(error);
        }

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
}

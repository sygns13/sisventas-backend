package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.PagoProveedorDAO;
import com.bcs.ventas.dao.mappers.EntradaStockMapper;
import com.bcs.ventas.dao.mappers.PagoProveedorMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.EntradaStockService;
import com.bcs.ventas.service.PagoProveedorService;
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
public class PagoProveedorServiceImpl implements PagoProveedorService {

    @Autowired
    private PagoProveedorDAO pagoProveedorDAO;

    @Autowired
    private PagoProveedorMapper pagoProveedorMapper;

    @Autowired
    private EntradaStockService entradaStockService;

    @Autowired
    private EntradaStockMapper entradaStockMapper;
    @Override
    public PagoProveedor registrar(PagoProveedor pagoProveedor) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        EntradaStock entradaStock = entradaStockService.listarPorId(pagoProveedor.getEntradaStock().getId());

        entradaStock.setUpdatedAd(fechaActualTime);
        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final
        entradaStock.setUser(user);

        pagoProveedor.setEntradaStock(entradaStock);
        pagoProveedor.setCreatedAt(fechaActualTime);
        pagoProveedor.setUpdatedAd(fechaActualTime);

        //TODO: Temporal hasta incluir Oauth inicio
        pagoProveedor.setEmpresaId(1L);
        pagoProveedor.setUserId(2L);
        //user.setEmpresaId(1L);
        //user = userDAO.listarPorId(user.getId());
        //Todo: Temporal hasta incluir Oauth final

        pagoProveedor.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        pagoProveedor.setActivo(Constantes.REGISTRO_ACTIVO);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(pagoProveedor, resultValidacion);

        if(validacion){

            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CASH)){
                pagoProveedor.setTipoTarjeta(Constantes.VOID);
                pagoProveedor.setSiglaTarjeta(Constantes.VOID);
                pagoProveedor.setNumeroTarjeta(Constantes.VOID);

                pagoProveedor.setBanco(Constantes.VOID);
                pagoProveedor.setNumeroCuenta(Constantes.VOID);
                pagoProveedor.setCodigoOperacion(Constantes.VOID);

                pagoProveedor.setNumeroCelular(Constantes.VOID);
                pagoProveedor.setNumeroCheque(Constantes.VOID);
            }
            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){
                pagoProveedor.setBanco(Constantes.VOID);
                pagoProveedor.setNumeroCuenta(Constantes.VOID);
                pagoProveedor.setCodigoOperacion(Constantes.VOID);

                pagoProveedor.setNumeroCelular(Constantes.VOID);
                pagoProveedor.setNumeroCheque(Constantes.VOID);
            }
            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){
                pagoProveedor.setTipoTarjeta(Constantes.VOID);
                pagoProveedor.setSiglaTarjeta(Constantes.VOID);
                pagoProveedor.setNumeroTarjeta(Constantes.VOID);

                pagoProveedor.setNumeroCelular(Constantes.VOID);
                pagoProveedor.setNumeroCheque(Constantes.VOID);
            }
            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){
                pagoProveedor.setTipoTarjeta(Constantes.VOID);
                pagoProveedor.setSiglaTarjeta(Constantes.VOID);
                pagoProveedor.setNumeroTarjeta(Constantes.VOID);

                pagoProveedor.setBanco(Constantes.VOID);
                pagoProveedor.setNumeroCuenta(Constantes.VOID);

                pagoProveedor.setNumeroCheque(Constantes.VOID);
            }
            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){
                pagoProveedor.setTipoTarjeta(Constantes.VOID);
                pagoProveedor.setSiglaTarjeta(Constantes.VOID);
                pagoProveedor.setNumeroTarjeta(Constantes.VOID);

                pagoProveedor.setNumeroCelular(Constantes.VOID);
                pagoProveedor.setNumeroCuenta(Constantes.VOID);
                pagoProveedor.setCodigoOperacion(Constantes.VOID);
            }


            return this.grabarRegistro(pagoProveedor);
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
    public int modificar(PagoProveedor pagoProveedor) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        EntradaStock entradaStock = entradaStockService.listarPorId(pagoProveedor.getEntradaStock().getId());

        entradaStock.setUpdatedAd(fechaActualTime);
        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final
        entradaStock.setUser(user);

        pagoProveedor.setEntradaStock(entradaStock);
        pagoProveedor.setUpdatedAd(fechaActualTime);

        //TODO: Temporal hasta incluir Oauth inicio
        pagoProveedor.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(pagoProveedor, resultValidacion);

        if(validacion){

            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CASH)){
                pagoProveedor.setTipoTarjeta(Constantes.VOID);
                pagoProveedor.setSiglaTarjeta(Constantes.VOID);
                pagoProveedor.setNumeroTarjeta(Constantes.VOID);

                pagoProveedor.setBanco(Constantes.VOID);
                pagoProveedor.setNumeroCuenta(Constantes.VOID);
                pagoProveedor.setCodigoOperacion(Constantes.VOID);

                pagoProveedor.setNumeroCelular(Constantes.VOID);
                pagoProveedor.setNumeroCheque(Constantes.VOID);
            }
            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){
                pagoProveedor.setBanco(Constantes.VOID);
                pagoProveedor.setNumeroCuenta(Constantes.VOID);
                pagoProveedor.setCodigoOperacion(Constantes.VOID);

                pagoProveedor.setNumeroCelular(Constantes.VOID);
                pagoProveedor.setNumeroCheque(Constantes.VOID);
            }
            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){
                pagoProveedor.setTipoTarjeta(Constantes.VOID);
                pagoProveedor.setSiglaTarjeta(Constantes.VOID);
                pagoProveedor.setNumeroTarjeta(Constantes.VOID);

                pagoProveedor.setNumeroCelular(Constantes.VOID);
                pagoProveedor.setNumeroCheque(Constantes.VOID);
            }
            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){
                pagoProveedor.setTipoTarjeta(Constantes.VOID);
                pagoProveedor.setSiglaTarjeta(Constantes.VOID);
                pagoProveedor.setNumeroTarjeta(Constantes.VOID);

                pagoProveedor.setBanco(Constantes.VOID);
                pagoProveedor.setNumeroCuenta(Constantes.VOID);

                pagoProveedor.setNumeroCheque(Constantes.VOID);
            }
            if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){
                pagoProveedor.setTipoTarjeta(Constantes.VOID);
                pagoProveedor.setSiglaTarjeta(Constantes.VOID);
                pagoProveedor.setNumeroTarjeta(Constantes.VOID);

                pagoProveedor.setNumeroCelular(Constantes.VOID);
                pagoProveedor.setNumeroCuenta(Constantes.VOID);
                pagoProveedor.setCodigoOperacion(Constantes.VOID);
            }


            return this.grabarRectificar(pagoProveedor);
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
    public List<PagoProveedor> listar() throws Exception {
        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO", Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);
        return pagoProveedorMapper.listByParameterMap(params);
    }

    @Override
    public PagoProveedor listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<PagoProveedor> pagoProveedors = pagoProveedorMapper.listByParameterMap(params);

        if(pagoProveedors.size() > 0)
            return pagoProveedors.get(0);
        else
            return null;
    }

    @Override
    public void eliminar(Long id) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        PagoProveedor pagoProveedor = this.listarPorId(id);
        EntradaStock entradaStock = entradaStockService.listarPorId(pagoProveedor.getEntradaStock().getId());

        entradaStock.setUpdatedAd(fechaActualTime);
        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final
        entradaStock.setUser(user);

        pagoProveedor.setEntradaStock(entradaStock);


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id, pagoProveedor);
        }
        else{
            String errorValidacion = "Error de validación Método Eliminar Pago de Compra";

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
    public PagoProveedor grabarRegistro(PagoProveedor pagoProveedor) throws Exception {

        //FIXME: Temporal hasta incluir currencys
        pagoProveedor.setMontoReal(pagoProveedor.getMontoPago());
        pagoProveedor.setTipoPago(Constantes.TIPO_PAGO_SOLES);

        if(pagoProveedor.getMontoReal().compareTo(new BigDecimal(0)) <= 0)
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA);

        if(pagoProveedor.getMontoReal().compareTo(new BigDecimal(0)) > 0 && pagoProveedor.getMontoReal().compareTo(pagoProveedor.getEntradaStock().getMontoPorPagar()) < 0)
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL);

        if(pagoProveedor.getMontoReal().compareTo(new BigDecimal(0)) > 0 && pagoProveedor.getMontoReal().compareTo(pagoProveedor.getEntradaStock().getMontoPorPagar()) >= 0){
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL);
            pagoProveedor.setMontoReal(pagoProveedor.getEntradaStock().getMontoPorPagar());
            pagoProveedor.setMontoPago(pagoProveedor.getEntradaStock().getMontoPorPagar());
        }

        PagoProveedor pagoProveedorReg = pagoProveedorDAO.registrar(pagoProveedor);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", pagoProveedor.getEntradaStock().getId());
        params.put("ESTADO", pagoProveedor.getEntradaStock().getEstado());
        params.put("UPDATED_AT", pagoProveedor.getEntradaStock().getUpdatedAd());
        entradaStockMapper.updateByPrimaryKeySelective(params);

        return pagoProveedorReg;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(PagoProveedor pagoProveedor) throws Exception {
        PagoProveedor pagoProveedorBD = this.listarPorId(pagoProveedor.getId());

        //FIXME: Temporal hasta incluir currencys
        pagoProveedor.setMontoReal(pagoProveedor.getMontoPago());
        pagoProveedor.setTipoPago(Constantes.TIPO_PAGO_SOLES);

        if(pagoProveedor.getMontoReal().subtract(pagoProveedorBD.getMontoReal()).compareTo(new BigDecimal(0)) <= 0)
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA);

        if(pagoProveedor.getMontoReal().subtract(pagoProveedorBD.getMontoReal()).compareTo(new BigDecimal(0)) > 0 && pagoProveedor.getMontoReal().subtract(pagoProveedorBD.getMontoReal()).compareTo(pagoProveedor.getEntradaStock().getMontoPorPagar()) < 0)
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL);

        if(pagoProveedor.getMontoReal().subtract(pagoProveedorBD.getMontoReal()).compareTo(new BigDecimal(0)) > 0 && pagoProveedor.getMontoReal().subtract(pagoProveedorBD.getMontoReal()).compareTo(pagoProveedor.getEntradaStock().getMontoPorPagar()) >= 0){
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL);
            pagoProveedor.setMontoReal(pagoProveedor.getEntradaStock().getMontoPorPagar().add(pagoProveedorBD.getMontoReal()));
            pagoProveedor.setMontoPago(pagoProveedor.getEntradaStock().getMontoPorPagar().add(pagoProveedorBD.getMontoReal()));
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", pagoProveedor.getId());

        params.put("FECHA", pagoProveedor.getFecha());
        params.put("METODOS_PAGO_ID", pagoProveedor.getMetodoPago().getId());
        params.put("MONTO_PAGO", pagoProveedor.getMontoReal());
        params.put("MONTO_REAL", pagoProveedor.getMontoPago());
        params.put("TIPO_TARJETA", pagoProveedor.getTipoTarjeta());
        params.put("SIGLA_TARJETA", pagoProveedor.getSiglaTarjeta());
        params.put("NUMERO_TARJETA", pagoProveedor.getNumeroTarjeta());
        params.put("BANCO", pagoProveedor.getBanco());
        params.put("NUMERO_CUENTA", pagoProveedor.getNumeroCuenta());
        params.put("NUMERO_CELULAR", pagoProveedor.getNumeroCelular());
        params.put("NUMERO_CHEQUE", pagoProveedor.getNumeroCheque());
        params.put("CODIGO_OPERACION", pagoProveedor.getCodigoOperacion());

        params.put("USER_ID", pagoProveedor.getUserId());
        params.put("UPDATED_AT", pagoProveedor.getUpdatedAd());

        int res = pagoProveedorMapper.updateByPrimaryKeySelective(params);

        params.clear();
        params.put("ID", pagoProveedor.getEntradaStock().getId());
        params.put("ESTADO", pagoProveedor.getEntradaStock().getEstado());
        params.put("UPDATED_AT", pagoProveedor.getEntradaStock().getUpdatedAd());
        entradaStockMapper.updateByPrimaryKeySelective(params);

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

        int res= pagoProveedorMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Pago indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void grabarEliminar(Long id, PagoProveedor pagoProveedor) throws Exception {

        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("UPDATED_AT",fechaUpdate);

        int res= pagoProveedorMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Registro indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }


        if(pagoProveedor.getMontoReal().compareTo(pagoProveedor.getEntradaStock().getMontoPagado()) >= 0)
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA);

        if(pagoProveedor.getMontoReal().compareTo(pagoProveedor.getEntradaStock().getMontoPagado()) < 0)
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL);

        params.clear();
        params.put("ID", pagoProveedor.getEntradaStock().getId());
        params.put("ESTADO", pagoProveedor.getEntradaStock().getEstado());
        params.put("UPDATED_AT", pagoProveedor.getEntradaStock().getUpdatedAd());
        entradaStockMapper.updateByPrimaryKeySelective(params);

    }

    @Override
    public boolean validacionRegistro(PagoProveedor pagoProveedor, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(pagoProveedor.getFecha() == null)
        {
            resultado = false;
            error = "Debe de ingresar la fecha de Pago";
            errors.add(error);
        }

        if(pagoProveedor.getEntradaStock() == null){
            resultado = false;
            error = "La Compra remitida no es válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(pagoProveedor.getEntradaStock().getEstado().intValue() == Constantes.COMPRA_ESTADO_ANULADO){
            resultado = false;
            error = "La Compra remitida se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(pagoProveedor.getEntradaStock().getEstado().intValue() == Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL){
            resultado = false;
            error = "La Compra remitida se encuentra pagada en su totalidad";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(pagoProveedor.getMetodoPago() == null){
            resultado = false;
            error = "El Método de Pago remitido no es válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(pagoProveedor.getMetodoPago().getTipoId() == null){
            resultado = false;
            error = "El Tipo de Método de Pago remitido no es válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){

            if(pagoProveedor.getBanco().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Banco Válido";
                errors.add(error);
            }

            if(pagoProveedor.getNumeroCuenta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar una Cuenta Bancaria Válida";
                errors.add(error);
            }

            if(pagoProveedor.getCodigoOperacion().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Código de Operación";
                errors.add(error);
            }
        }

        if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){

            if(pagoProveedor.getTipoTarjeta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Tipo de Tarjeta Válido";
                errors.add(error);
            }

            if(pagoProveedor.getNumeroTarjeta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar los 4 últimos dígitos de la tarjeta";
                errors.add(error);
            }
        }

        if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){

            if(pagoProveedor.getNumeroCelular().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Número de Celular Válido";
                errors.add(error);
            }

            if(pagoProveedor.getCodigoOperacion().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Código de Operación";
                errors.add(error);
            }
        }

        if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){

            if(pagoProveedor.getBanco().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Banco Válido";
                errors.add(error);
            }

            if(pagoProveedor.getNumeroCheque().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Número de Cheque";
                errors.add(error);
            }
        }

        if(pagoProveedor.getMontoPago() == null || pagoProveedor.getMontoPago().compareTo(new BigDecimal(Constantes.CANTIDAD_ZERO)) < 0){
            resultado = false;
            error = "Debe de remitir un Monto de Pago Válido";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(PagoProveedor pagoProveedor, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(pagoProveedor.getFecha() == null)
        {
            resultado = false;
            error = "Debe de ingresar la fecha de Pago";
            errors.add(error);
        }

        if(pagoProveedor.getEntradaStock() == null){
            resultado = false;
            error = "La Compra remitida no es válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(pagoProveedor.getEntradaStock().getEstado().intValue() == Constantes.COMPRA_ESTADO_ANULADO){
            resultado = false;
            error = "La Compra remitida se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(pagoProveedor.getMetodoPago() == null){
            resultado = false;
            error = "El Método de Pago remitido no es válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(pagoProveedor.getMetodoPago().getTipoId() == null){
            resultado = false;
            error = "El Tipo de Método de Pago remitido no es válido";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_WIRE_TRANSFER)){

            if(pagoProveedor.getBanco().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Banco Válido";
                errors.add(error);
            }

            if(pagoProveedor.getNumeroCuenta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar una Cuenta Bancaria Válida";
                errors.add(error);
            }

            if(pagoProveedor.getCodigoOperacion().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Código de Operación";
                errors.add(error);
            }
        }

        if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CREDIT_CARD)){

            if(pagoProveedor.getTipoTarjeta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Tipo de Tarjeta Válido";
                errors.add(error);
            }

            if(pagoProveedor.getNumeroTarjeta().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar los 4 últimos dígitos de la tarjeta";
                errors.add(error);
            }
        }

        if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_E_WALLET)){

            if(pagoProveedor.getNumeroCelular().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Número de Celular Válido";
                errors.add(error);
            }

            if(pagoProveedor.getCodigoOperacion().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Código de Operación";
                errors.add(error);
            }
        }

        if(pagoProveedor.getMetodoPago().getTipoId().equals(Constantes.ID_TIPO_METODO_PAGO_CHEQUE)){

            if(pagoProveedor.getBanco().trim().isEmpty()){
                resultado = false;
                error = "Debe de Seleccionar un Banco Válido";
                errors.add(error);
            }

            if(pagoProveedor.getNumeroCheque().trim().isEmpty()){
                resultado = false;
                error = "Debe de Ingresar un Número de Cheque";
                errors.add(error);
            }
        }

        if(pagoProveedor.getMontoPago() == null || pagoProveedor.getMontoPago().compareTo(new BigDecimal(Constantes.CANTIDAD_ZERO)) < 0){
            resultado = false;
            error = "Debe de remitir un Monto de Pago Válido";
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

    @Override
    public void altabaja(Long id, Integer valor) throws Exception {
        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("ACTIVO", valor);
        params.put("UPDATED_AT",fechaUpdate);

        int res= pagoProveedorMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Override
    public Page<PagoProveedor> listar(Pageable page, String buscar) throws Exception {
        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = pagoProveedorMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<PagoProveedor> pagoProveedors = pagoProveedorMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return new PageImpl<>(pagoProveedors, page, total);
    }
}

package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.DetalleMetodoPagoDAO;
import com.bcs.ventas.dao.mappers.DetalleMetodoPagoMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Banco;
import com.bcs.ventas.model.entities.DetalleMetodoPago;
import com.bcs.ventas.model.entities.TipoTarjeta;
import com.bcs.ventas.service.BancoService;
import com.bcs.ventas.service.DetalleMetodoPagoService;
import com.bcs.ventas.service.TipoTarjetaService;
import com.bcs.ventas.utils.Constantes;
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
public class DetalleMetodoPagoServiceImpl implements DetalleMetodoPagoService {

    @Autowired
    DetalleMetodoPagoDAO detalleMetodoPagoDAO;

    @Autowired
    DetalleMetodoPagoMapper detalleMetodoPagoMapper;

    @Autowired
    private BancoService bancoService;

    @Autowired
    private TipoTarjetaService tipoTarjetaService;


    @Override
    public DetalleMetodoPago registrar(DetalleMetodoPago a) throws Exception {
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        a.setEmpresaId(1L);
        a.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        if (a.getNumeroCuenta() != null)
            a.setNumeroCuenta(a.getNumeroCuenta().trim());

        if (a.getNumeroCelular() != null)
            a.setNumeroCelular(a.getNumeroCelular().trim());


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Registro de Método de Pago";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(DetalleMetodoPago a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if (a.getNumeroCuenta() != null)
            a.setNumeroCuenta(a.getNumeroCuenta().trim());

        if (a.getNumeroCelular() != null)
            a.setNumeroCelular(a.getNumeroCelular().trim());


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Registro de Método de Pago";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<DetalleMetodoPago> listar() throws Exception {
        //return bancoDAO.listar();

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        List<DetalleMetodoPago> detalleMetodoPagos = detalleMetodoPagoMapper.listByParameterMap(params);

        detalleMetodoPagos.forEach((detalleMetodoPago) -> {
            if(detalleMetodoPago.getTipoTarjetaId() != null && detalleMetodoPago.getTipoTarjetaId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
                TipoTarjeta obj = null;
                try {
                    obj = tipoTarjetaService.listarPorId(detalleMetodoPago.getTipoTarjetaId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                detalleMetodoPago.setTipoTarjeta(obj);
            }

            if(detalleMetodoPago.getBancoId() != null && detalleMetodoPago.getBancoId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
                Banco obj = null;
                try {
                    obj = bancoService.listarPorId(detalleMetodoPago.getBancoId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                detalleMetodoPago.setBanco(obj);
            }
        });

        return detalleMetodoPagos;
    }

    public Page<DetalleMetodoPago> listar(Pageable page, String buscar, Long metodoPagoId , Long bancoId) throws Exception {
        //return bancoDAO.listar();

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        if(metodoPagoId.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("METODOS_PAGO_ID",metodoPagoId);
        }

        if(bancoId.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("BANCO_ID",bancoId);
        }

        Long total = detalleMetodoPagoMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<DetalleMetodoPago> detalleMetodoPagos = detalleMetodoPagoMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        detalleMetodoPagos.forEach((detalleMetodoPago) -> {
            if(detalleMetodoPago.getTipoTarjetaId() != null && detalleMetodoPago.getTipoTarjetaId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
                TipoTarjeta obj = null;
                try {
                    obj = tipoTarjetaService.listarPorId(detalleMetodoPago.getTipoTarjetaId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                detalleMetodoPago.setTipoTarjeta(obj);
            }

            if(detalleMetodoPago.getBancoId() != null && detalleMetodoPago.getBancoId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
                Banco obj = null;
                try {
                    obj = bancoService.listarPorId(detalleMetodoPago.getBancoId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                detalleMetodoPago.setBanco(obj);
            }
        });

        return new PageImpl<>(detalleMetodoPagos, page, total);
    }

    @Override
    public DetalleMetodoPago listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<DetalleMetodoPago> detalleMetodoPagos = detalleMetodoPagoMapper.listByParameterMap(params);

        detalleMetodoPagos.forEach((detalleMetodoPago) -> {
            if(detalleMetodoPago.getTipoTarjetaId() != null && detalleMetodoPago.getTipoTarjetaId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
                TipoTarjeta obj = null;
                try {
                    obj = tipoTarjetaService.listarPorId(detalleMetodoPago.getTipoTarjetaId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                detalleMetodoPago.setTipoTarjeta(obj);
            }

            if(detalleMetodoPago.getBancoId() != null && detalleMetodoPago.getBancoId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
                Banco obj = null;
                try {
                    obj = bancoService.listarPorId(detalleMetodoPago.getBancoId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                detalleMetodoPago.setBanco(obj);
            }
        });

        if(detalleMetodoPagos.size() > 0)
            return detalleMetodoPagos.get(0);
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
            String errorValidacion = "Error de validación Método Eliminar Registro de Método de Pago";

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
    public DetalleMetodoPago grabarRegistro(DetalleMetodoPago a) throws Exception {
        return detalleMetodoPagoDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(DetalleMetodoPago b) throws Exception {
        //return bancoDAO.modificar(a);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", b.getId());
        params.put("METODOS_PAGO_ID", b.getMetodoPago().getId());
        params.put("TIPO_TARJETA_ID", b.getTipoTarjetaId());
        params.put("BANCO_ID", b.getBancoId());
        params.put("NUMERO_CUENTA", b.getNumeroCuenta());
        params.put("NUMERO_CELULAR", b.getNumeroCelular());

        params.put("USER_ID", b.getUserId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        return detalleMetodoPagoMapper.updateByPrimaryKeySelective(params);
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

        int res= detalleMetodoPagoMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Banco indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Override
    public boolean validacionRegistro(DetalleMetodoPago a, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        if(a.getMetodoPago() == null || a.getMetodoPago().getId() == null ||  a.getMetodoPago().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Seleccione el Método de Pago";
            errors.add(error);
        }

        if(a.getMetodoPago() != null){
            if(( a.getMetodoPago().getTipoId().equals("WT") || a.getMetodoPago().getTipoId().equals("CH") ) &&
                    ( a.getBancoId() == null || a.getBancoId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0 || a.getNumeroCuenta().isEmpty())){
                resultado = false;
                error = "Seleccione el Banco e Ingrese la cuenta bancaria";
                errors.add(error);
            }
        }

        if(a.getMetodoPago() != null){
            if(a.getMetodoPago().getTipoId().equals("EW") && a.getNumeroCelular().isEmpty()){
            resultado = false;
            error = "Ingrese el número de Celular";
            errors.add(error);
            }
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(DetalleMetodoPago a, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(a.getMetodoPago() == null || a.getMetodoPago().getId() == null ||  a.getMetodoPago().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Seleccione el Método de Pago";
            errors.add(error);
        }

        if(a.getMetodoPago() != null){
            if(( a.getMetodoPago().getTipoId().equals("WT") || a.getMetodoPago().getTipoId().equals("CH") ) &&
                    ( a.getBancoId() == null || a.getBancoId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0 || a.getNumeroCuenta().isEmpty())){
                resultado = false;
                error = "Seleccione el Banco e Ingrese la cuenta bancaria";
                errors.add(error);
            }
        }

        if(a.getMetodoPago() != null){
            if(a.getMetodoPago().getTipoId().equals("EW") && a.getNumeroCelular().isEmpty()){
                resultado = false;
                error = "Ingrese el número de Celular";
                errors.add(error);
            }
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

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void altabaja(Long id, Integer valor, Long userId) throws Exception {
        LocalDateTime fechaActual = LocalDateTime.now();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", id);
        params.put("ACTIVO", valor);

        params.put("USER_ID", userId);
        params.put("UPDATED_AT", fechaActual);

        detalleMetodoPagoMapper.updateByPrimaryKeySelective(params);
    }

}

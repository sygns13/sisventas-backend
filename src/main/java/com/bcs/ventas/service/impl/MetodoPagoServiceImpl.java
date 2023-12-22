package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.MetodoPagoDAO;
import com.bcs.ventas.dao.mappers.MetodoPagoMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.MetodoPago;
import com.bcs.ventas.service.MetodoPagoService;
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
public class MetodoPagoServiceImpl implements MetodoPagoService {

    @Autowired
    private MetodoPagoDAO metodoPagoDAO;

    @Autowired
    private MetodoPagoMapper metodoPagoMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public MetodoPago registrar(MetodoPago a) throws Exception {
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
        if(a.getTipoId() == null)    a.setTipoId("");
        if(a.getTipo() == null)    a.setTipo("");

        a.setNombre(a.getNombre().trim());
        a.setTipoId(a.getTipoId().trim());
        a.setTipo(a.getTipo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Metodo de Pago";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public int modificar(MetodoPago a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getTipoId() == null)    a.setTipoId("");
        if(a.getTipo() == null)    a.setTipo("");

        a.setNombre(a.getNombre().trim());
        a.setTipoId(a.getTipoId().trim());
        a.setTipo(a.getTipo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Metodo de Pago";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<MetodoPago> listar() throws Exception {
        //return metodoPagoMapper.getAllEntities();
        //return metodoPagoDAO.listar();
        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        return metodoPagoMapper.listByParameterMap(params);
    }


    public Page<MetodoPago> listar(Pageable page, String buscar) throws Exception {
        //return bancoDAO.listar();
        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = metodoPagoMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<MetodoPago> metodoPagos = metodoPagoMapper.listByParameterMap(params);

        return new PageImpl<>(metodoPagos, page, total);
    }

    @Override
    public MetodoPago listarPorId(Long id) throws Exception {
        // Map<String, Object> params = new HashMap<String, Object>();
        //params.put("ID",id);
        //return metodoPagoMapper.listByParameterMap(params).get(0);
        //return metodoPagoDAO.listarPorId(id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<MetodoPago> metodoPagos = metodoPagoMapper.listByParameterMap(params);

        if(metodoPagos.size() > 0)
            return metodoPagos.get(0);
        else
            return null;
    }

    @Override
    public void eliminar(Long id) throws Exception {
        // metodoPagoDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else{
            String errorValidacion = "Error de validación Método Eliminar Metodo de Pago";

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
        int res= metodoPagoMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }



    //Métodos de Grabado

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public MetodoPago grabarRegistro(MetodoPago a) throws Exception {
        return metodoPagoDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(MetodoPago t) throws Exception {
        //return metodoPagoDAO.modificar(a);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", t.getId());
        params.put("NOMBRE", t.getNombre());
        params.put("TIPO_ID", t.getTipoId());
        params.put("TIPO", t.getTipo());
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", t.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        return metodoPagoMapper.updateByPrimaryKeySelective(params);
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
        int res= metodoPagoMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Metodo de Pago indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //Métodos de Validación

    @Override
    public boolean validacionRegistro(MetodoPago a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<MetodoPago> metodoPagosV1 = metodoPagoMapper.listByParameterMap(params);



        if(metodoPagosV1.size() > 0){
            resultado = false;
            error = "El nombre del Metodo de Pago ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(MetodoPago a , Map<String, Object> resultValidacion){

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


        List<MetodoPago> metodoPagosV1 = metodoPagoMapper.listByParameterMap(params);

        if(metodoPagosV1.size() > 0){
            resultado = false;
            error = "El nombre del Metodo de Pago ingresado ya se encuentra registrado";
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

        //Lógica de Validaciones para Eliminación MetodoPago

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

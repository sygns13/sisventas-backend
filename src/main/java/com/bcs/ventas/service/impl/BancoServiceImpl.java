package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.BancoDAO;
import com.bcs.ventas.dao.mappers.BancoMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Banco;
import com.bcs.ventas.service.BancoService;
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
public class BancoServiceImpl implements BancoService {

    @Autowired
    private BancoDAO bancoDAO;

    @Autowired
    private BancoMapper bancoMapper;

    @Override
    public Banco registrar(Banco a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        a.setEmpresaId(1L);
        a.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDir() == null) a.setDir("");

        a.setNombre(a.getNombre().trim());
        a.setDir(a.getDir().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Banco";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public int modificar(Banco a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDir() == null) a.setDir("");

        a.setNombre(a.getNombre().trim());
        a.setDir(a.getDir().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Banco";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<Banco> listar() throws Exception {
        //return bancoDAO.listar();

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO",Constantes.REGISTRO_NO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        return bancoMapper.listByParameterMap(params);
    }

    public Page<Banco> listar(Pageable page, String buscar) throws Exception {
        //return bancoDAO.listar();

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = bancoMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Banco> bancos = bancoMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return new PageImpl<>(bancos, page, total);
    }

    @Override
    public Banco listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Banco> bancos = bancoMapper.listByParameterMap(params);

        if(bancos.size() > 0)
            return bancos.get(0);
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
            String errorValidacion = "Error de validación Método Eliminar banco";

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
    public void altabaja(Long id, Integer valor) throws Exception {

        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("ACTIVO", valor);
        params.put("UPDATED_AT",fechaUpdate);

        int res= bancoMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public Banco grabarRegistro(Banco a) throws Exception {
        return bancoDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Banco b) throws Exception {
        //return bancoDAO.modificar(a);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", b.getId());
        params.put("NOMBRE", b.getNombre());
        params.put("DIR", b.getDir());
        params.put("USER_ID", b.getUserId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        return bancoMapper.updateByPrimaryKeySelective(params);
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

        int res= bancoMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Banco indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //TODO: Métodos de Validación

    @Override
    public boolean validacionRegistro(Banco a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Banco> bancosV1 = bancoMapper.listByParameterMap(params);



        if(bancosV1.size() > 0){
            resultado = false;
            error = "El nombre del Banco ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Banco a , Map<String, Object> resultValidacion){

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


        List<Banco> bancosV1 = bancoMapper.listByParameterMap(params);

        if(bancosV1.size() > 0){
            resultado = false;
            error = "El nombre del Banco ingresado ya se encuentra registrado";
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

        //Lógica de Validaciones para Eliminación Banco

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

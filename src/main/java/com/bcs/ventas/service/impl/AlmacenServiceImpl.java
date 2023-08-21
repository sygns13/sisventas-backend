package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.mappers.AlmacenMapper;
import com.bcs.ventas.dao.mappers.DepartamentoMapper;
import com.bcs.ventas.dao.mappers.DistritoMapper;
import com.bcs.ventas.dao.mappers.ProvinciaMapper;
import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Departamento;
import com.bcs.ventas.model.entities.Distrito;
import com.bcs.ventas.model.entities.Provincia;
import com.bcs.ventas.service.AlmacenService;
import com.bcs.ventas.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class AlmacenServiceImpl implements AlmacenService {

    @Autowired
    private AlmacenDAO almacenDAO;

    @Autowired
    private AlmacenMapper almacenMapper;

    @Autowired
    private DepartamentoMapper departamentoMapper;

    @Autowired
    private ProvinciaMapper provinciaMapper;

    @Autowired
    private DistritoMapper distritoMapper;

    @Override
    public Almacen registrar(Almacen a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        a.setEmpresaId(1L);
        a.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDireccion() == null) a.setDireccion("");
        if(a.getCodigo() == null)    a.setCodigo("");

        a.setNombre(a.getNombre().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setCodigo(a.getCodigo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Local";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());

            for(String error: errors ){
                errorValidacion = error + ". " + errorValidacion;
            }
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public int modificar(Almacen a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        a.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDireccion() == null) a.setDireccion("");
        if(a.getCodigo() == null)    a.setCodigo("");

        a.setNombre(a.getNombre().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setCodigo(a.getCodigo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Eliminar Local";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    public List<Almacen> listar() throws Exception {
        //return almacenMapper.getAllEntities();
        //return almacenDAO.listar();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        return almacenMapper.listByParameterMap(params);
    }

    public Page<Almacen> listar(Pageable page, String buscar) throws Exception {
        //return almacenMapper.getAllEntities();
        //return almacenDAO.listar();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("BUSCAR","%"+buscar+"%");

        int total = almacenMapper.getTotalElements(params);
        int totalPages = (int) Math.ceil( ((double)total) / page.getPageSize());
        int offset = page.getPageSize()*(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Almacen> almacenes = almacenMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return new PageImpl<>(almacenes, page, total);
    }

    @Override
    public Almacen listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Almacen> almacens = almacenMapper.listByParameterMap(params);

        if(almacens.size() > 0)
            return almacens.get(0);
        else
            return null;

        //return almacenDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
       // almacenDAO.eliminar(id);
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else{
            String errorValidacion = "Error de validación Método Modificar Local";

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

        int res= almacenMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo realizar la transacción, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public Almacen grabarRegistro(Almacen a) throws Exception {
        return almacenDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Almacen a) throws Exception {
        //return almacenDAO.modificar(a);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", a.getId());
        params.put("NOMBRE", a.getNombre());
        params.put("DIRECCION", a.getDireccion());
        params.put("CODIGO", a.getCodigo());
        params.put("DISTRITO_ID", a.getDistritoId());
        params.put("USER_ID", a.getUserId());
        params.put("UPDATED_AT", a.getUpdatedAd());

        return almacenMapper.updateByPrimaryKeySelective(params);
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

        int res= almacenMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Local indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }




    //TODO: Métodos de Validación

    @Override
    public boolean validacionRegistro(Almacen a, Map<String, Object> resultValidacion){

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Almacen> almacensV1 = almacenMapper.listByParameterMap(params);



        if(almacensV1.size() > 0){
            resultado = false;
            error = "El nombre del local ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        params.put("CODIGO",a.getCodigo());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            resultado = false;
            error = "El código del local ingresado ya se encuentra registrado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Almacen a , Map<String, Object> resultValidacion){

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


        List<Almacen> almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            resultado = false;
            error = "El nombre del local ingresado ya se encuentra registrado";
            errors.add(error);
        }
        params.clear();

        params.put("NO_ID",a.getId());
        params.put("CODIGO",a.getCodigo());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            resultado = false;
            error = "El código del local ingresado ya se encuentra registrado";
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

        //Lógica de Validaciones para Eliminación Almacén

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public List<Departamento> getDepartamentos(Long idPais) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("PAIS_ID", idPais);

        return departamentoMapper.listByParameterMap(params);
    }

    @Override
    public List<Provincia> getProvincias(Long idDep) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("DEPARTAMENTO_ID", idDep);

        return provinciaMapper.listByParameterMap(params);
    }

    @Override
    public List<Distrito> getDistritos(Long idProv) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("PROVINCIA_ID", idProv);

        return distritoMapper.listByParameterMap(params);
    }
}

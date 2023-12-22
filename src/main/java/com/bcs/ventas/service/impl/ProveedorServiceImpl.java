package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.ProveedorDAO;
import com.bcs.ventas.dao.TipoDocumentoDAO;
import com.bcs.ventas.dao.mappers.EntradaStockMapper;
import com.bcs.ventas.dao.mappers.ProveedorMapper;
import com.bcs.ventas.dao.mappers.TipoDocumentoMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.EntradaStock;
import com.bcs.ventas.model.entities.Proveedor;
import com.bcs.ventas.model.entities.TipoDocumento;
import com.bcs.ventas.service.ProveedorService;
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
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorDAO proveedorDAO;

    @Autowired
    private ProveedorMapper proveedorMapper;

    @Autowired
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Autowired
    private TipoDocumentoMapper tipoDocumentoMapper;
    @Autowired
    private EntradaStockMapper entradaStockMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public Page<Proveedor> listar(Pageable page, String buscar) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = proveedorMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Proveedor> proveedors = proveedorMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return new PageImpl<>(proveedors, page, total);
    }

    @Override
    public Proveedor registrar(Proveedor a) throws Exception {
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
        if(a.getDocumento() == null) a.setDocumento("");
        if(a.getDireccion() == null) a.setDireccion("");
        if(a.getTelefono() == null) a.setTelefono("");
        if(a.getAnexo() == null) a.setAnexo("");
        if(a.getCelular() == null) a.setCelular("");
        if(a.getResponsable() == null) a.setResponsable("");

        a.setNombre(a.getNombre().trim());
        a.setDocumento(a.getDocumento().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setTelefono(a.getTelefono().trim());
        a.setAnexo(a.getAnexo().trim());
        a.setCelular(a.getCelular().trim());
        a.setResponsable(a.getResponsable().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Proveedor";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(Proveedor a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDocumento() == null) a.setDocumento("");
        if(a.getDireccion() == null) a.setDireccion("");
        if(a.getTelefono() == null) a.setTelefono("");
        if(a.getAnexo() == null) a.setAnexo("");
        if(a.getCelular() == null) a.setCelular("");
        if(a.getResponsable() == null) a.setResponsable("");

        a.setNombre(a.getNombre().trim());
        a.setDocumento(a.getDocumento().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setTelefono(a.getTelefono().trim());
        a.setAnexo(a.getAnexo().trim());
        a.setCelular(a.getCelular().trim());
        a.setResponsable(a.getResponsable().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Proveedor";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<Proveedor> listar() throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO", Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        return proveedorMapper.listByParameterMap(params);
    }

    @Override
    public Proveedor listarPorId(Long id) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Proveedor> proveedors = proveedorMapper.listByParameterMap(params);

        if(proveedors.size() > 0)
            return proveedors.get(0);
        else
            return null;
    }


    public Proveedor getByDocument(String document) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("DOCUMENTO",document.trim());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Proveedor> proveedors = proveedorMapper.listByParameterMap(params);

        if(proveedors.size() > 0)
            return proveedors.get(0);
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
        else{
            String errorValidacion = "Error de validación Método Eliminar Proveedor";

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
    public Proveedor grabarRegistro(Proveedor a) throws Exception {
        return proveedorDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Proveedor b) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", b.getId());
        params.put("NOMBRE", b.getNombre());
        params.put("TIPO_DOCUMENTO_ID", b.getTipoDocumento().getId());
        params.put("DOCUMENTO", b.getDocumento());
        params.put("DIRECCION", b.getDireccion());
        params.put("TELEFONO", b.getTelefono());
        params.put("ANEXO", b.getAnexo());
        params.put("CELULAR", b.getCelular());
        params.put("RESPONSABLE", b.getResponsable());
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        return proveedorMapper.updateByPrimaryKeySelective(params);
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
        int res= proveedorMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Proveedor indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Override
    public boolean validacionRegistro(Proveedor a, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("TIPO_DOCUMENTO_ID",a.getTipoDocumento().getId());
        params.put("DOCUMENTO",a.getDocumento());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Proveedor> proveedorsV1 = proveedorMapper.listByParameterMap(params);


        if(a.getTipoDocumento() == null || a.getTipoDocumento().getId() == null || a.getTipoDocumento().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Debe de seleccionar el Tipo de Documento de Identidad ";
            errors.add(error);
        }

        if(proveedorsV1.size() > 0){
            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(a.getTipoDocumento().getId());
            resultado = false;
            error = "Ya se encuentra registrado un proveedor con el documento "+tipoDocumento.getTipo()+" Nº "+a.getDocumento();
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Proveedor a, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_ID",a.getId());
        params.put("TIPO_DOCUMENTO_ID",a.getTipoDocumento().getId());
        params.put("DOCUMENTO",a.getDocumento());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);


        List<Proveedor> proveedorsV1 = proveedorMapper.listByParameterMap(params);

        if(proveedorsV1.size() > 0){
            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(a.getTipoDocumento().getId());
            resultado = false;
            error = "Ya se encuentra registrado otro proveedor con el documento "+tipoDocumento.getKey()+" "+a.getDocumento();
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionEliminacion(Long idProveedor, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        Proveedor proveedor = proveedorDAO.listarPorId(idProveedor);

        //Lógica de Validaciones para Eliminación Proveedor
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CLIENTE_ID", idProveedor);
        params.put("EMPRESA_ID",proveedor.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<EntradaStock> entradaStocksV1 = entradaStockMapper.listByParameterMap(params);

        if(entradaStocksV1.size() > 0){
            resultado = false;
            error = "Por integridad de Datos no se puede eliminar este proveedor ya que tiene Compras asociadas a él";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

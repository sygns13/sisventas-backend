package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.ClienteDAO;
import com.bcs.ventas.dao.TipoDocumentoDAO;
import com.bcs.ventas.dao.mappers.ClienteMapper;
import com.bcs.ventas.dao.mappers.TipoDocumentoMapper;
import com.bcs.ventas.dao.mappers.VentaMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Banco;
import com.bcs.ventas.model.entities.Cliente;
import com.bcs.ventas.model.entities.TipoDocumento;
import com.bcs.ventas.model.entities.Venta;
import com.bcs.ventas.service.ClienteService;
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
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Autowired
    private TipoDocumentoMapper tipoDocumentoMapper;

    @Autowired
    private VentaMapper ventaMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public Page<Cliente> listar(Pageable page, String buscar) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("BUSCAR","%"+buscar+"%");

        Long total = clienteMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Cliente> bancos = clienteMapper.listByParameterMap(params);
        //Page<Almacen> resultado = new PageImpl<>(almacenes, page, totalPages);

        return new PageImpl<>(bancos, page, total);
    }

    @Override
    public Cliente registrar(Cliente a) throws Exception {
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
        if(a.getCorreo1() == null) a.setCorreo1("");
        if(a.getCorreo2() == null) a.setCorreo2("");

        a.setNombre(a.getNombre().trim());
        a.setDocumento(a.getDocumento().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setTelefono(a.getTelefono().trim());
        a.setCorreo1(a.getCorreo1().trim());
        a.setCorreo2(a.getCorreo2().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(a, resultValidacion);

        if(validacion)
            return this.grabarRegistro(a);

        String errorValidacion = "Error de validación Método Registrar Cliente";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(Cliente a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDocumento() == null) a.setDocumento("");
        if(a.getDireccion() == null) a.setDireccion("");
        if(a.getTelefono() == null) a.setTelefono("");
        if(a.getCorreo1() == null) a.setCorreo1("");
        if(a.getCorreo2() == null) a.setCorreo2("");

        a.setNombre(a.getNombre().trim());
        a.setDocumento(a.getDocumento().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setTelefono(a.getTelefono().trim());
        a.setCorreo1(a.getCorreo1().trim());
        a.setCorreo2(a.getCorreo2().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(a, resultValidacion);

        if(validacion)
            return this.grabarRectificar(a);

        String errorValidacion = "Error de validación Método Modificar Cliente";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<Cliente> listar() throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BORRADO", Constantes.REGISTRO_NO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        return clienteMapper.listByParameterMap(params);
    }

    @Override
    public Cliente listarPorId(Long id) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Cliente> clientes = clienteMapper.listByParameterMap(params);

        if(clientes.size() > 0)
            return clientes.get(0);
        else
            return null;
    }


    @Override
    public Cliente getByDocument(String document) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("DOCUMENTO",document.trim());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Cliente> clientes = clienteMapper.listByParameterMap(params);

        if(clientes.size() > 0)
            return clientes.get(0);
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
            String errorValidacion = "Error de validación Método Eliminar Cliente";

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
    public Cliente grabarRegistro(Cliente a) throws Exception {
        return clienteDAO.registrar(a);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Cliente b) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", b.getId());
        params.put("NOMBRE", b.getNombre());
        params.put("TIPO_DOCUMENTO_ID", b.getTipoDocumento().getId());
        params.put("DOCUMENTO", b.getDocumento());
        params.put("DIRECCION", b.getDireccion());
        params.put("TELEFONO", b.getTelefono());
        params.put("CORREO1", b.getCorreo1());
        params.put("CORREO2", b.getCorreo2());
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", b.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());

        return clienteMapper.updateByPrimaryKeySelective(params);
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

        int res= clienteMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar el Cliente indicado, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Override
    public boolean validacionRegistro(Cliente a, Map<String, Object> resultValidacion) throws Exception {

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

        List<Cliente> cientesV1 = clienteMapper.listByParameterMap(params);


        if(a.getTipoDocumento() == null || a.getTipoDocumento().getId() == null || a.getTipoDocumento().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0){
            resultado = false;
            error = "Debe de seleccionar el Tipo de Documento de Identidad ";
            errors.add(error);
        }

        if(cientesV1.size() > 0){
            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(a.getTipoDocumento().getId());
            resultado = false;
            error = "Ya se encuentra registrado un cliente con el documento "+tipoDocumento.getTipo()+" Nº "+a.getDocumento();
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Cliente a, Map<String, Object> resultValidacion) throws Exception {
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


        List<Cliente> cientesV1 = clienteMapper.listByParameterMap(params);

        if(cientesV1.size() > 0){
            TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(a.getTipoDocumento().getId());
            resultado = false;
            error = "Ya se encuentra registrado otro cliente con el documento "+tipoDocumento.getKey()+" "+a.getDocumento();
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionEliminacion(Long idCliente, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        Cliente cliente = clienteDAO.listarPorId(idCliente);

        //Lógica de Validaciones para Eliminación Cliente
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CLIENTE_ID", idCliente);
        params.put("EMPRESA_ID",cliente.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Venta> ventasV1 = ventaMapper.listByParameterMap(params);

        if(ventasV1.size() > 0){
            resultado = false;
            error = "Por integridad de Datos no se puede eliminar este cliente ya que tiene ventas asociadas a él";
            errors.add(error);
        }
        params.clear();

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

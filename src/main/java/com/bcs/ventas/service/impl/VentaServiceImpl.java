package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.UserDAO;
import com.bcs.ventas.dao.VentaDAO;
import com.bcs.ventas.dao.mappers.ProductoMapper;
import com.bcs.ventas.dao.mappers.VentaMapper;
import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.User;
import com.bcs.ventas.model.entities.Venta;
import com.bcs.ventas.service.AlmacenService;
import com.bcs.ventas.service.VentaService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.beans.FiltroVenta;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class VentaServiceImpl implements VentaService {

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private VentaMapper ventaMapper;

    @Autowired
    private AlmacenService almacenService;

    @Autowired
    private VentaDAO ventaDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public Venta registrar(Venta v) throws Exception {

        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        v.setCreatedAt(fechaActualTime);
        v.setUpdatedAd(fechaActualTime);

        if(v.getFecha() == null)
            v.setFecha(fechaActual);

        if(v.getHora() == null)
            v.setHora(horaActual);

        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        v.setEmpresaId(1L);
        user.setId(2L);
        //user.setEmpresaId(1L);
        //user = userDAO.listarPorId(user.getId());
        //Todo: Temporal hasta incluir Oauth final

        v.setUser(user);
        v.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        v.setActivo(Constantes.REGISTRO_ACTIVO);

        if(v.getSubtotalInafecto() == null) v.setSubtotalInafecto(Constantes.CANTIDAD_ZERO_BIG_DECIMAL);
        if(v.getSubtotalAfecto() == null) v.setSubtotalAfecto(Constantes.CANTIDAD_ZERO_BIG_DECIMAL);
        if(v.getIgv() == null) v.setIgv(Constantes.CANTIDAD_ZERO_BIG_DECIMAL);

        v.setEstado(Constantes.VENTA_ESTADO_INICIADO);
        v.setPagado(Constantes.VENTA_NO_PAGADO);
        v.setTipo(Constantes.VENTA_TIPO_BIENES);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(v, resultValidacion);

        if(validacion){
            //Get Sequence
            FiltroVenta filtroSeq= new FiltroVenta();
            filtroSeq.setAlmacenId(v.getAlmacen().getId());

            Long sequence = this.GetSequence(filtroSeq);
            String numeroVenta = "";

            numeroVenta = v.getAlmacen().getNombre().charAt(0) + StringUtils.leftPad(v.getAlmacen().getId().toString(), 3,"0") + "-" + StringUtils.leftPad(sequence.toString(), 10,"0");
            v.setNumeroVenta(numeroVenta);

            return this.grabarRegistro(v);
        }


        String errorValidacion = "Error de validación Método Registrar Venta de Bienes Inicial";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(Venta venta) throws Exception {
        return 0;
    }

    public Venta modificarVenta(Venta v) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();

        v.setCreatedAt(fechaActualTime);
        v.setUpdatedAd(fechaActualTime);

        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        v.setUser(user);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(v, resultValidacion);

        if(validacion){
            int res = this.grabarRectificar(v);
            Venta venta = new Venta();
            if(res == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                venta = this.listarPorId(v.getId());
            }
            return venta;
        }


        String errorValidacion = "Error de validación Método Modificar Venta de Bienes";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }
    public Venta modificarVentaCliente(Venta v) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();

        v.setCreatedAt(fechaActualTime);
        v.setUpdatedAd(fechaActualTime);

        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setId(2L);
        //Todo: Temporal hasta incluir Oauth final

        v.setUser(user);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificadoCliente(v, resultValidacion);

        if(validacion){
            int res = this.grabarRectificarCliente(v);
            Venta venta = new Venta();
            if(res == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                venta = this.listarPorId(v.getId());
            }
            return venta;
        }


        String errorValidacion = "Error de validación Método Modificar Cliente de Venta";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<Venta> listar() throws Exception {
        return null;
    }

    @Override
    public Venta listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Venta> ventas = ventaMapper.listByParameterMap(params);

        if(ventas.size() > 0)
            return ventas.get(0);
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
            String errorValidacion = "Error de validación Método Eliminar venta";

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
    public Venta grabarRegistro(Venta v) throws Exception {
        Venta venta = ventaDAO.registrar(v);
        return venta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Venta v) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", v.getId());
        params.put("FECHA", v.getFecha());
        params.put("CLIENTE_ID", v.getCliente().getId());
        params.put("COMPROBANTE_ID", v.getComprobante().getId());
        params.put("SUBTOTAL_INAFECTO", v.getSubtotalInafecto());
        params.put("SUBTOTAL_AFECTO", v.getSubtotalAfecto());
        params.put("IGV", v.getIgv());
        params.put("ESTADO", v.getEstado());
        params.put("PAGADO", v.getPagado());
        params.put("HORA", v.getHora());
        params.put("USER_ID", v.getUser().getId());
        params.put("UPDATED_AT", v.getUpdatedAd());

        return ventaMapper.updateByPrimaryKeySelective(params);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public int grabarRectificarCliente(Venta v) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", v.getId());
        params.put("CLIENTE_ID", v.getCliente().getId());
        params.put("USER_ID", v.getUser().getId());
        params.put("UPDATED_AT", v.getUpdatedAd());

        return ventaMapper.updateByPrimaryKeySelective(params);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public Venta grabarRectificarM(Venta v) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", v.getId());
        params.put("FECHA", v.getFecha());
        params.put("CLIENTE_ID", v.getCliente().getId());
        params.put("COMPROBANTE_ID", v.getComprobante().getId());
        params.put("SUBTOTAL_INAFECTO", v.getSubtotalInafecto());
        params.put("SUBTOTAL_AFECTO", v.getSubtotalAfecto());
        params.put("IGV", v.getIgv());
        params.put("ESTADO", v.getEstado());
        params.put("PAGADO", v.getPagado());
        params.put("HORA", v.getHora());
        params.put("USER_ID", v.getUser().getId());
        params.put("UPDATED_AT", v.getUpdatedAd());

        int resultado = ventaMapper.updateByPrimaryKeySelective(params);

        return v;
    }

    @Override
    public void grabarEliminar(Long id) throws Exception {
        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("UPDATED_AT",fechaUpdate);


        int res= ventaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar la venta indicada, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Override
    public boolean validacionRegistro(Venta v, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(v.getAlmacen() == null || ( v.getAlmacen().getId() == null )){
            resultado = false;
            error = "Debe de remitir correctamente el Almacen";
            errors.add(error);
        } else {
            Almacen almacen = almacenService.listarPorId(v.getAlmacen().getId());
            v.setAlmacen(almacen);

            if(almacen == null) {
                resultado = false;
                error = "El Almacen remitido no existe o fue borrado";
                errors.add(error);
            }
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Venta v, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(v.getId() == null || ( v.getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el ID de la Venta";
            errors.add(error);
        }

        if(v.getFecha() == null){
            resultado = false;
            error = "Debe de remitir correctamente la Fecha de la Venta";
            errors.add(error);
        }

        if(v.getHora() == null){
            resultado = false;
            error = "Debe de remitir correctamente la Hora de la Venta";
            errors.add(error);
        }

        if(v.getSubtotalAfecto() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Importe Sub Total Afecto de la Venta";
            errors.add(error);
        }

        if(v.getSubtotalInafecto() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Importe Sub Total Inafecto de la Venta";
            errors.add(error);
        }

        if(v.getIgv() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Importe del IGV de la Venta";
            errors.add(error);
        }

        if(v.getEstado() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Estado de la Venta";
            errors.add(error);
        }

        if(v.getPagado() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Estado del Pago de la Venta";
            errors.add(error);
        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }


    private boolean validacionModificadoCliente(Venta v, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(v.getId() == null || ( v.getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el ID de la Venta";
            errors.add(error);
        }

        if(v.getCliente() == null || v.getCliente().getId() == null || ( v.getCliente().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el Cliente de la Venta";
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

        Venta v = this.listarPorId(id);

        //Lógica de Validaciones para Eliminación Venta
        if(v.getComprobante() != null){
            resultado = false;
            error = "No se puede eliminar la Venta porque ya Cuenta con un Comprobante asociado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public Page<Venta> listar(Pageable page, FiltroVenta filtros) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getNumeroVenta() != null && filtros.getNumeroVenta().trim().length() > 0)
            params.put("NUMERO_VENTA", filtros.getNumeroVenta());

        if(filtros.getId() != null && filtros.getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ID",filtros.getId());

        if(filtros.getFecha() != null)
            params.put("FECHA", filtros.getFecha());

        if(filtros.getFechaInicio() != null && filtros.getFechaFinal() != null){
            params.put("FECHA_INI", filtros.getFechaInicio());
            params.put("FECHA_FIN", filtros.getFechaFinal());
        }

        if(filtros.getEstadoVenta() != null)
            params.put("ESTADO", filtros.getEstadoVenta());

        if(filtros.getPagado() != null)
            params.put("PAGADO", filtros.getPagado());

        if(filtros.getTipoVenta() != null)
            params.put("TIPO", filtros.getTipoVenta());

        if(filtros.getIdCliente() != null && filtros.getIdCliente().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CLIENTE_ID", filtros.getIdCliente());

        if(filtros.getNombreCliente() != null && filtros.getNombreCliente().trim().length() > 0)
            params.put("CLI_NOMBRE", "%"+filtros.getNombreCliente()+"%");

        if(filtros.getDocumentoCliente() != null && filtros.getDocumentoCliente().trim().length() > 0)
            params.put("CLI_DOCUMENTO", filtros.getDocumentoCliente());

        if(filtros.getIdTipoDocumentoCliente() != null)
            params.put("CLI_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoCliente());

        if(filtros.getIdComprobante() != null && filtros.getIdComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("COMPROBANTE_ID", filtros.getIdComprobante());

        if(filtros.getSerieComprobante() != null && filtros.getSerieComprobante().trim().length() > 0)
            params.put("CO_SERIE", filtros.getSerieComprobante());

        if(filtros.getNumeroComprobante() != null && filtros.getNumeroComprobante().trim().length() > 0)
            params.put("CO_NUMERO", filtros.getNumeroComprobante());

        if(filtros.getIdTipoComprobante() != null && filtros.getIdTipoComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CO_TIPO_COMPROBANTE_ID", filtros.getIdTipoComprobante());

        if(filtros.getIdUser() != null && filtros.getIdUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("USER_ID", filtros.getIdUser());

        if(filtros.getNameUser() != null && filtros.getNameUser().trim().length() > 0)
            params.put("U_NAME", filtros.getNameUser());

        if(filtros.getEmailUser() != null && filtros.getEmailUser().trim().length() > 0)
            params.put("U_EMAIL", filtros.getEmailUser());

        if(filtros.getIdTipoUser() != null && filtros.getIdTipoUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("U_TIPO_USER_ID", filtros.getIdTipoUser());

        if(filtros.getBuscarDatosUser() != null && filtros.getBuscarDatosUser().trim().length() > 0)
            params.put("U_BUSCAR", filtros.getBuscarDatosUser());


        Long total = ventaMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Venta> ventas = ventaMapper.listByParameterMap(params);

        return new PageImpl<>(ventas, page, total);
    }









    private Long GetSequence(FiltroVenta filtros) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        long sequence = ventaMapper.getTotalElements(params);
        sequence++;

        return sequence;
    }
}

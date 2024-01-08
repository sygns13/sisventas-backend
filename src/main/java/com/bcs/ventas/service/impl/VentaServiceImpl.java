package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.*;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.dto.ProductosVentaDTO;
import com.bcs.ventas.model.dto.TopProductosVendidosDTO;
import com.bcs.ventas.model.dto.VentasDetallesDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.AlmacenService;
import com.bcs.ventas.service.ClienteService;
import com.bcs.ventas.service.InitComprobanteService;
import com.bcs.ventas.service.VentaService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.AgregarProductoBean;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroVenta;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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
    private DetalleVentaDAO detalleVentaDAO;

    @Autowired
    private DetalleVentaMapper detalleVentaMapper;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoteDAO loteDAO;

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private LoteMapper loteMapper;

    @Autowired
    private  CobroVentaDAO cobroVentaDAO;

    @Autowired
    private  CobroVentaMapper cobroVentaMapper;

    @Autowired
    private  ComprobanteDAO comprobanteDAO;

    @Autowired
    private  ComprobanteMapper comprobanteMapper;

    @Autowired
    private InitComprobanteService initComprobanteService;

    @Autowired
    private TipoDocumentoMapper tipoDocumentoMapper;

    @Autowired
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Autowired
    private TipoComprobanteDAO tipoComprobanteDAO;

    @Autowired
    private InitComprobanteDAO initComprobanteDAO;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

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

        //Oauth inicio
        v.setEmpresaId(claimsAuthorization.getEmpresaId());
        user.setId(claimsAuthorization.getUserId());
        //user.setEmpresaId(1L);
        //user = userDAO.listarPorId(user.getId());
        //Oauth final

        v.setUser(user);
        v.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        v.setActivo(Constantes.REGISTRO_ACTIVO);

        if(v.getSubtotalInafecto() == null) v.setSubtotalInafecto(Constantes.CANTIDAD_ZERO_BIG_DECIMAL);
        if(v.getSubtotalAfecto() == null) v.setSubtotalAfecto(Constantes.CANTIDAD_ZERO_BIG_DECIMAL);
        if(v.getIgv() == null) v.setIgv(Constantes.CANTIDAD_ZERO_BIG_DECIMAL);

        v.setEstado(Constantes.VENTA_ESTADO_INICIADO);
        v.setPagado(Constantes.VENTA_NO_PAGADO);
        v.setTipo(Constantes.VENTA_TIPO_BIENES);

        v.setCantidadIcbper(Constantes.CANTIDAD_ZERO);
        v.setMontoIcbper(Constantes.CANTIDAD_ICBPER_2023);

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

        v.setUpdatedAd(fechaActualTime);

        User user = new User();

        //Oauth inicio
        user.setUserId(claimsAuthorization.getUserId());
        //Oauth final

        v.setUser(user);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(v, resultValidacion);

        if(validacion){
            int res = this.grabarRectificar(v);
            if(res == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                Venta venta = this.listarPorId(v.getId());
                Venta ventaRes = this.recalcularVenta(venta);
                return ventaRes;
            }
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

        //Oauth inicio
        user.setId(claimsAuthorization.getUserId());
        //Oauth final

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

    public Venta modificarVentaClienteFirst(Venta v) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO_2);
        params.put("EMPRESA_ID",EmpresaId);

        List<Cliente> clientes = clienteMapper.listByParameterMap(params);

        if(clientes.size() > 0){
            v.setCliente(clientes.get(0));
            return this.modificarVentaCliente(v);
        }
            return v;
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

        if(!ventas.isEmpty()){
            params.clear();
            params.put("VENTA_ID",ventas.get(0).getId());
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

            List<DetalleVenta> detallesVentas = detalleVentaMapper.listByParameterMap(params);
            ventas.get(0).setDetalleVentas(detallesVentas);

            if(ventas.get(0).getEstado().equals(Constantes.VENTA_ESTADO_ANULADO))
                ventas.get(0).setEstadoStr(Constantes.VENTA_ESTADO_ANULADO_STR);

            if(ventas.get(0).getEstado().equals(Constantes.VENTA_ESTADO_INICIADO))
                ventas.get(0).setEstadoStr(Constantes.VENTA_ESTADO_INICIADO_STR);

            if(ventas.get(0).getEstado().equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                ventas.get(0).setEstadoStr(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR);

            if(ventas.get(0).getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                ventas.get(0).setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR);

            if(ventas.get(0).getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                ventas.get(0).setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR);

            if(ventas.get(0).getCliente() != null && ventas.get(0).getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(ventas.get(0).getCliente().getTipoDocumento().getId());
                    ventas.get(0).getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if(ventas.get(0).getComprobante() != null && ventas.get(0).getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(ventas.get(0).getComprobante().getInitComprobante().getId());
                    ventas.get(0).getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            return ventas.get(0);
        }

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

    @Override
    public void anular(Long id) throws Exception {
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionAnulacion(id, resultValidacion);

        if(validacion) {
            this.grabarAnular(id);
        }
        else {
            String errorValidacion = "Error de validación Método Anular venta";

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
    private CobroVenta grabarRegistroCobro(CobroVenta c, InitComprobante initComprobante) throws Exception {

        c.getVenta().setPagado(Constantes.VENTA_NO_PAGADO);

        if(c.getImporte().compareTo(new BigDecimal(0)) <= 0)
            c.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA);

        if(c.getImporte().compareTo(new BigDecimal(0)) > 0 && c.getImporte().compareTo(c.getVenta().getTotalMonto()) < 0)
            c.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL);

        if(c.getImporte().compareTo(new BigDecimal(0)) > 0 && c.getImporte().compareTo(c.getVenta().getTotalMonto()) >= 0){
            c.getVenta().setEstado(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL);
            c.getVenta().setPagado(Constantes.VENTA_SI_PAGADO);
            c.setImporte(c.getVenta().getTotalMonto());
        }

        CobroVenta cobroVenta = cobroVentaDAO.registrar(c);


        initComprobante.setNumeroActual(initComprobante.getNumeroActual() + Constantes.CANTIDAD_UNIDAD_INTEGER);
        initComprobanteService.modificar(initComprobante);

        Comprobante comprobante = new Comprobante();
        comprobante.setSerie(initComprobante.getLetraSerieStr()+initComprobante.getNumSerieStr());
        comprobante.setNumero(StringUtils.leftPad(initComprobante.getNumeroActual().toString(), initComprobante.getCantidadDigitos(), "0"));
        comprobante.setCantidadDigitos(initComprobante.getCantidadDigitos());
        comprobante.setInitComprobante(initComprobante);
        comprobante.setEstado(Constantes.COMPROBANTE_ESTADO_CREADO);
        comprobante.setUserId(cobroVenta.getUserId());
        comprobante.setAlmacenId(cobroVenta.getVenta().getAlmacen().getId());
        comprobante.setEmpresaId(cobroVenta.getEmpresaId());
        comprobante.setActivo(Constantes.REGISTRO_ACTIVO);
        comprobante.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        comprobante.setCreatedAt(cobroVenta.getCreatedAt());
        comprobante.setUpdatedAd(cobroVenta.getUpdatedAd());

        comprobante = comprobanteDAO.registrar(comprobante);

        cobroVenta.getVenta().setComprobante(comprobante);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", c.getVenta().getId());
        params.put("ESTADO", c.getVenta().getEstado());
        params.put("PAGADO", c.getVenta().getPagado());
        params.put("COMPROBANTE_ID", comprobante.getId());
        params.put("UPDATED_AT", c.getVenta().getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        ventaMapper.updateByPrimaryKeySelective(params);


        return cobroVenta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private Venta grabarGenerarComprobante(Venta venta, InitComprobante initComprobante) throws Exception {

        LocalDateTime fechaActualTime = LocalDateTime.now();

        initComprobante.setNumeroActual(initComprobante.getNumeroActual() + Constantes.CANTIDAD_UNIDAD_INTEGER);
        initComprobanteService.modificar(initComprobante);

        Comprobante comprobante = new Comprobante();
        comprobante.setSerie(initComprobante.getLetraSerieStr()+initComprobante.getNumSerieStr());
        comprobante.setNumero(StringUtils.leftPad(initComprobante.getNumeroActual().toString(), initComprobante.getCantidadDigitos(), "0"));
        comprobante.setCantidadDigitos(initComprobante.getCantidadDigitos());
        comprobante.setInitComprobante(initComprobante);
        comprobante.setEstado(Constantes.COMPROBANTE_ESTADO_CREADO);
        //Oauth inicio
        comprobante.setUserId(claimsAuthorization.getUserId());
        //Oauth final
        comprobante.setAlmacenId(venta.getAlmacen().getId());
        comprobante.setEmpresaId(venta.getEmpresaId());
        comprobante.setActivo(Constantes.REGISTRO_ACTIVO);
        comprobante.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        comprobante.setCreatedAt(fechaActualTime);
        comprobante.setUpdatedAd(fechaActualTime);

        comprobante = comprobanteDAO.registrar(comprobante);

        venta.setComprobante(comprobante);
        venta.setUpdatedAd(fechaActualTime);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", venta.getId());
        //params.put("ESTADO", venta.getEstado());
        params.put("COMPROBANTE_ID", comprobante.getId());
        params.put("UPDATED_AT", venta.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        ventaMapper.updateByPrimaryKeySelective(params);


        return venta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Venta v) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", v.getId());

        if(v.getFecha() != null)
            params.put("FECHA", v.getFecha());

        if(v.getCliente() != null && v.getCliente().getId() != null)
            params.put("CLIENTE_ID", v.getCliente().getId());

        if(v.getComprobante() != null && v.getComprobante().getId() != null)
            params.put("COMPROBANTE_ID", v.getComprobante().getId());

        if(v.getSubtotalInafecto() != null)
            params.put("SUBTOTAL_INAFECTO", v.getSubtotalInafecto());

        if(v.getSubtotalExonerado() != null)
            params.put("SUBTOTAL_EXONERADO", v.getSubtotalExonerado());

        if(v.getSubtotalAfecto() != null)
            params.put("SUBTOTAL_AFECTO", v.getSubtotalAfecto());

        if(v.getTotalMonto() != null)
            params.put("TOTAL_MONTO", v.getTotalMonto());

        if(v.getTotalAfectoIsc() != null)
            params.put("TOTAL_AFECTO_ISC", v.getTotalAfectoIsc());

        if(v.getIgv() != null)
            params.put("IGV", v.getIgv());

        if(v.getIsc() != null)
            params.put("ISC", v.getIsc());

        if(v.getEstado() != null)
            params.put("ESTADO", v.getEstado());

        if(v.getPagado() != null)
            params.put("PAGADO", v.getPagado());

        if(v.getHora() != null)
            params.put("HORA", v.getHora());

        if(v.getUser() != null && v.getUser().getId() != null)
            params.put("USER_ID", v.getUser().getId());

        if(v.getNumeroVenta() != null)
            params.put("NUMERO_VENTA", v.getNumeroVenta());

        if(v.getCantidadIcbper() != null)
            params.put("CANTIDAD_ICBPER", v.getCantidadIcbper());

        if(v.getMontoIcbper() != null)
            params.put("MONTO_ICBPER", v.getMontoIcbper());


        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", v.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        return ventaMapper.updateByPrimaryKeySelective(params);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public int grabarRectificarCliente(Venta v) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", v.getId());
        params.put("CLIENTE_ID", v.getCliente().getId());
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", v.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        return ventaMapper.updateByPrimaryKeySelective(params);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private Venta grabarRectificarM(Venta v) throws Exception {

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
        params.put("USER_ID", claimsAuthorization.getUserId());
        params.put("UPDATED_AT", v.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int resultado = ventaMapper.updateByPrimaryKeySelective(params);

        return v;
    }

    @Override
    public Venta agregarProducto(AgregarProductoBean addProductoVenta) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final


        Map<String, Object> resultValidacion = new HashMap<String, Object>();
        boolean validacion = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;
        String errorValidacion = "Error de validación Agregar Producto a Venta de Bienes";

        Venta venta = this.listarPorId(addProductoVenta.getIdVenta());
        if(venta == null){
            validacion = false;
            error = "No se envió una Venta Válida";
            errors.add(error);
        }

        if(addProductoVenta.getCodigoUnidad() != null)
            addProductoVenta.setCodigoUnidad(addProductoVenta.getCodigoUnidad().trim());

        if(addProductoVenta.getCodigoUnidad() == null || addProductoVenta.getCodigoUnidad().isEmpty()){
            validacion = false;
            error = "Envíe un código válido";
            errors.add(error);
        }

        if(!validacion){
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());

            throw new ValidationServiceException(errorValidacion);
        }



        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ALMACEN_ID",venta.getAlmacen().getId());
        params.put("CODIGO_UNIDAD", addProductoVenta.getCodigoUnidad());
        List<ProductosVentaDTO> productosVenta = productoMapper.listByParameterMapProductoVenta(params);

        if(productosVenta.isEmpty()){
            validacion = false;
            error = "No se encontró producto con el código enviado";
            errors.add(error);
        }

        if(!validacion){
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());

            throw new ValidationServiceException(errorValidacion);
        }

        ProductosVentaDTO productoVenta = productosVenta.get(0);

        if(new BigDecimal(productoVenta.getStock().getCantidad()).compareTo(new BigDecimal(productoVenta.getDetalleUnidadProducto().getUnidad().getCantidad())) < 0){
            validacion = false;
            error = "No se tiene stock del producto " +productoVenta.getProducto().getNombre()+ " para la canitidad solicitada de " + productoVenta.getDetalleUnidadProducto().getUnidad().getCantidad() + " unidades";
            errors.add(error);
        }

        if(!validacion){
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());

            throw new ValidationServiceException(errorValidacion);
        }

        Lote loteBD = null;

        if(productoVenta.getProducto().getActivoLotes().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            params.clear();
            params.put("PRODUCTO_ID", productoVenta.getProducto().getId());
            params.put("ALMACEN_ID", venta.getAlmacen().getId());
            params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
            params.put("CANTIDAD_MIN", new BigDecimal(productoVenta.getDetalleUnidadProducto().getUnidad().getCantidad()));

            List<Lote> lotes = loteMapper.listByParameterMap(params);

            if(lotes.isEmpty()){
                validacion = false;
                error = "No se encontraron lotes con cantidades disponibles en stock con el código enviado";
                errors.add(error);
            }

            loteBD = lotes.get(0);
        }

        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setVenta(venta);
        detalleVenta.setVentaIdReference(venta.getId());
        detalleVenta.setProducto(productoVenta.getProducto());
        detalleVenta.setPrecioVenta(productoVenta.getDetalleUnidadProducto().getPrecio());
        detalleVenta.setPrecioCompra(productoVenta.getDetalleUnidadProducto().getCostoCompra());
        detalleVenta.setCantidad(Constantes.CANTIDAD_UNIDAD_DOUBLE);
        detalleVenta.setAlmacenId(venta.getAlmacen().getId());
        detalleVenta.setEsGrabado(productoVenta.getProducto().getAfectoIgv());
        detalleVenta.setEsIsc(productoVenta.getProducto().getAfectoIsc());
        detalleVenta.setDescuento(new BigDecimal(0));
        detalleVenta.setTipDescuento(Constantes.TIPO_DESCUENTO_PORCENTIL);
        detalleVenta.setCantidadReal(productoVenta.getDetalleUnidadProducto().getUnidad().getCantidad());
        detalleVenta.setUnidad(productoVenta.getDetalleUnidadProducto().getUnidad().getNombre());
        detalleVenta.setPrecioTotal(productoVenta.getDetalleUnidadProducto().getPrecio());
        detalleVenta.setLote(loteBD);

        return this.registrarDetalle(detalleVenta);
    }

    @Override
    public Venta registrarDetalle(DetalleVenta detalleVenta) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        detalleVenta.setCreatedAt(fechaActualTime);
        detalleVenta.setUpdatedAd(fechaActualTime);

        //Oauth inicio
        detalleVenta.setEmpresaId(claimsAuthorization.getEmpresaId());
        detalleVenta.setUserId(claimsAuthorization.getUserId());
        //user.setEmpresaId(1L);
        //user = userDAO.listarPorId(user.getId());
        //Oauth final

        detalleVenta.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        detalleVenta.setActivo(Constantes.REGISTRO_ACTIVO);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = false;
        Venta venta = this.listarPorId(detalleVenta.getVentaIdReference());

        validacion =this.validacionVentaDetalle(detalleVenta, resultValidacion);

        if(validacion){
            detalleVenta.setVenta(venta);
            BigDecimal precioTotalFix = detalleVenta.getPrecioTotal().setScale(2, RoundingMode.HALF_UP);
            detalleVenta.setPrecioTotal(precioTotalFix);

            //Get Anteriores registros mismo prod mismo lote misma unidad
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("VENTA_ID", venta.getId());
            params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
            params.put("PRODUCTO_ID", detalleVenta.getProducto().getId());
            params.put("ALMACEN_ID", detalleVenta.getAlmacenId());
            params.put("CANTIDAD_REAL", detalleVenta.getCantidadReal());

            if(detalleVenta.getProducto().getActivoLotes().intValue() == Constantes.REGISTRO_ACTIVO.intValue())
                params.put("LOTE_ID", detalleVenta.getLote().getId());

            List<DetalleVenta> detallesVentas = detalleVentaMapper.listByParameterMap(params);

            if(detallesVentas.isEmpty()){
                this.grabarRegistroDetalle(venta, detalleVenta);
                //Venta ventaRes = this.recalcularVenta(venta);
                return venta;
            }

            this.grabarModificarDetalleAdd(venta, detalleVenta, detallesVentas.get(0));
            Venta ventaRes = this.recalcularVenta(venta);
            return ventaRes;
        }



        String errorValidacion = "Error de validación Agregar Productos a Venta de Bienes";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public Venta modificarDetalle(DetalleVenta detalleVenta) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        detalleVenta.setUpdatedAd(fechaActualTime);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = false;
        Venta venta = this.listarPorId(detalleVenta.getVentaIdReference());

        validacion =this.validacionModificacionVentaDetalle(detalleVenta, resultValidacion);

        if(validacion){
            DetalleVenta detalleVentaBD = (DetalleVenta) resultValidacion.get("detalleVentaBD");
            detalleVenta.setVenta(venta);
            BigDecimal precioTotalFix = detalleVenta.getPrecioTotal().setScale(2, RoundingMode.HALF_UP);
            detalleVenta.setPrecioTotal(precioTotalFix);

            this.grabarModificarDetalle(venta, detalleVenta, detalleVentaBD);
            Venta ventaRes = this.recalcularVenta(venta);
            return ventaRes;
        }



        String errorValidacion = "Error de validación Agregar Productos a Venta de Bienes";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public Venta resetVenta(Venta ventaRemitida) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = false;
        Venta venta = this.listarPorId(ventaRemitida.getId());

        venta.setUpdatedAd(fechaActualTime);

        this.grabarResetVenta(venta);
        Venta ventaRes = this.recalcularVentaReset(venta);
        return ventaRes;

    }

    @Override
    public Venta generarComprobante(Venta venta) throws Exception {

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        Venta ventaBD = this.listarPorId(venta.getId());

        boolean validacion = this.validacionGenerarComprobante(venta, ventaBD, resultValidacion);

        if(validacion){
            InitComprobante initComprobante = new InitComprobante();
            initComprobante = initComprobanteService.listarPorId(venta.getInitComprobanteId());
            Venta ventaGenComp = this.grabarGenerarComprobante(ventaBD, initComprobante);
            Venta ventaRes = this.recalcularVenta(ventaGenComp);
            return ventaRes;
        }

        String errorValidacion = "Error de validación Generar Comprobante de Pago";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public CobroVenta cobrarVenta(CobroVenta cobroVenta) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        Venta v = this.listarPorId(cobroVenta.getVenta().getId());

        v.setUpdatedAd(fechaActualTime);
        User user = new User();

        //Oauth inicio
        user.setUserId(claimsAuthorization.getUserId());
        //Oauth final
        v.setUser(user);

        cobroVenta.setVenta(v);
        cobroVenta.setCreatedAt(fechaActualTime);
        cobroVenta.setUpdatedAd(fechaActualTime);

        //Oauth inicio
        cobroVenta.setEmpresaId(claimsAuthorization.getEmpresaId());
        cobroVenta.setUserId(claimsAuthorization.getUserId());
        //user.setEmpresaId(1L);
        //user = userDAO.listarPorId(user.getId());
        //Oauth final

        cobroVenta.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        cobroVenta.setActivo(Constantes.REGISTRO_ACTIVO);

        cobroVenta.setFecha(fechaActual);


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistroCobro(cobroVenta, resultValidacion);

        if(validacion){
            InitComprobante initComprobante = new InitComprobante();
            initComprobante = initComprobanteService.listarPorId(cobroVenta.getInitComprobanteId());

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


            return this.grabarRegistroCobro(cobroVenta, initComprobante);
        }


        String errorValidacion = "Error de validación Método Cobrar Venta";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);

    }

    @Override
    public Venta recalcularVentaPublic(Venta venta) throws  Exception {
        return this.recalcularVenta(venta);
    }

    private boolean validacionVentaDetalle(DetalleVenta detalleVenta, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final
        BigDecimal cantidadTotal = BigDecimal.valueOf(detalleVenta.getCantidad() * detalleVenta.getCantidadReal());

        if(detalleVenta.getProducto().getActivoLotes().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            Lote loteBD = loteDAO.listarPorId(detalleVenta.getLote().getId());
            BigDecimal cantidadLote = BigDecimal.valueOf(loteBD.getCantidad());

            if(loteBD.getActivo().intValue() != Constantes.REGISTRO_ACTIVO.intValue()){
                resultado = false;
                error = "EL lote no se encuentra activo, por favor seleccione otro lote";
                errors.add(error);
            }

            if(loteBD.getBorrado().intValue() == Constantes.REGISTRO_BORRADO.intValue()){
                resultado = false;
                error = "EL lote se encuentra borrado, por favor seleccione otro lote";
                errors.add(error);
            }

            if(cantidadLote.compareTo(cantidadTotal) < 0){
                resultado = false;
                error = "EL lote no tiene las suficientes unidades para ser agregados a la venta, por favor modifique la cantidad o seleccione otro lote";
                errors.add(error);
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PRODUCTO_ID", detalleVenta.getProducto().getId());
        params.put("ALMACEN_ID", detalleVenta.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);

        if(stockG1.isEmpty()){
            resultado = false;
            error = "No se encuentra el Stock del Producto en el Almacen de la Sucursal";
            errors.add(error);
        }

        if(!stockG1.isEmpty() && (BigDecimal.valueOf(stockG1.get(0).getCantidad()).compareTo(cantidadTotal) < 0)){
            resultado = false;
            error = "EL Producto no tiene suficientes unidades de Stock en el Almacen de la Sucursal para ser agregados a la venta";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
    private boolean validacionDeleteVentaDetalle(DetalleVenta detalleVenta, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    private boolean validacionModificacionVentaDetalle(DetalleVenta detalleVenta, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final
        BigDecimal cantidadTotal = BigDecimal.valueOf(detalleVenta.getCantidad() * detalleVenta.getCantidadReal());

        DetalleVenta detalleVentaBD = detalleVentaDAO.listarPorId(detalleVenta.getId());
        BigDecimal cantidadTotalBD = new BigDecimal(0);


        if(detalleVenta.getProducto().getActivoLotes().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            Lote loteBD = loteDAO.listarPorId(detalleVenta.getLote().getId());
            BigDecimal cantidadLote = BigDecimal.valueOf(loteBD.getCantidad());

            if(detalleVenta.getLote().getId().equals(detalleVentaBD.getLote().getId())){
                cantidadTotalBD = BigDecimal.valueOf(detalleVentaBD.getCantidad() * detalleVentaBD.getCantidadReal());
            }

            if(loteBD.getActivo().intValue() != Constantes.REGISTRO_ACTIVO.intValue()){
                resultado = false;
                error = "EL lote no se encuentra activo, por favor seleccione otro lote";
                errors.add(error);
            }

            if(loteBD.getBorrado().intValue() == Constantes.REGISTRO_BORRADO.intValue()){
                resultado = false;
                error = "EL lote se encuentra borrado, por favor seleccione otro lote";
                errors.add(error);
            }

            if(cantidadLote.compareTo(cantidadTotal.subtract(cantidadTotalBD)) < 0){
                resultado = false;
                error = "EL lote no tiene las suficientes unidades para ser agregados a la venta, por favor modifique la cantidad o seleccione otro lote";
                errors.add(error);
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PRODUCTO_ID", detalleVenta.getProducto().getId());
        params.put("ALMACEN_ID", detalleVenta.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);

        if(stockG1.isEmpty()){
            resultado = false;
            error = "No se encuentra el Stock del Producto en el Almacen de la Sucursal";
            errors.add(error);
        }

        cantidadTotalBD = BigDecimal.valueOf(detalleVentaBD.getCantidad() * detalleVentaBD.getCantidadReal());
        if(!stockG1.isEmpty() && (BigDecimal.valueOf(stockG1.get(0).getCantidad()).compareTo(cantidadTotal.subtract(cantidadTotalBD)) < 0)){
            resultado = false;
            error = "EL Producto no tiene suficientes unidades de Stock en el Almacen de la Sucursal para ser agregados a la venta";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);
        resultValidacion.put("detalleVentaBD",detalleVentaBD);

        return resultado;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public Venta grabarRegistroDetalle(Venta venta, DetalleVenta detalleVenta) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        detalleVenta = detalleVentaDAO.registrar(detalleVenta);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PRODUCTO_ID", detalleVenta.getProducto().getId());
        params.put("ALMACEN_ID", detalleVenta.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        Lote loteBD = null;

        if(detalleVenta.getLote() != null)
            loteBD = loteDAO.listarPorId(detalleVenta.getLote().getId());

        //this.recalcularVenta(venta);
        this.modificarStocks(Constantes.TIPO_RETIRO_PRODUCTOS, stockG1.get(0), loteBD, detalleVenta.getCantidad() * detalleVenta.getCantidadReal());

        return venta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public Venta grabarModificarDetalleAdd(Venta venta, DetalleVenta detalleVenta, DetalleVenta detalleVentaBD) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        BigDecimal precioTotal = detalleVenta.getPrecioVenta().multiply(BigDecimal.valueOf(detalleVenta.getCantidad() + detalleVentaBD.getCantidad()));

        if(detalleVenta.getTipDescuento().equals(Constantes.TIPO_DESCUENTO_PORCENTIL))
            precioTotal = precioTotal.subtract(precioTotal.multiply(detalleVenta.getDescuento()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

        if(detalleVenta.getTipDescuento().equals(Constantes.TIPO_DESCUENTO_FIJO))
            precioTotal = precioTotal.subtract(detalleVenta.getDescuento());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", detalleVentaBD.getId());
        params.put("CANTIDAD", detalleVenta.getCantidad() + detalleVentaBD.getCantidad());
        params.put("DESCUENTO", detalleVenta.getDescuento());
        params.put("TIPO_DESCUENTO", detalleVenta.getTipDescuento());
        params.put("PRECIO_TOTAL", precioTotal);
        params.put("CANTIDAD_REAL", detalleVenta.getCantidadReal());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int res = detalleVentaMapper.updateByPrimaryKeySelective(params);

        //DetalleVenta d = detalleVentaDAO.registrar(detalleVenta);

        params.clear();
        params.put("PRODUCTO_ID", detalleVenta.getProducto().getId());
        params.put("ALMACEN_ID", detalleVenta.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        Lote loteBD = null;

        if(detalleVenta.getLote() != null)
            loteBD = loteDAO.listarPorId(detalleVenta.getLote().getId());

        //this.recalcularVenta(venta);
        this.modificarStocks(Constantes.TIPO_RETIRO_PRODUCTOS, stockG1.get(0), loteBD, detalleVenta.getCantidad() * detalleVenta.getCantidadReal());

        return venta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public Venta grabarModificarDetalle(Venta venta, DetalleVenta detalleVenta, DetalleVenta detalleVentaBD) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Lote loteBD1 = null;
        Lote loteBD2 = null;

        if(detalleVentaBD.getLote() != null)
            loteBD1 = loteDAO.listarPorId(detalleVentaBD.getLote().getId());

        if(detalleVenta.getLote() != null)
            loteBD2 = loteDAO.listarPorId(detalleVenta.getLote().getId());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PRODUCTO_ID", detalleVenta.getProducto().getId());
        params.put("ALMACEN_ID", detalleVenta.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);

        params.clear();
        params.put("ID", detalleVentaBD.getId());
        params.put("CANTIDAD", detalleVenta.getCantidad());
        params.put("DESCUENTO", detalleVenta.getDescuento());
        params.put("TIPO_DESCUENTO", detalleVenta.getTipDescuento());
        params.put("PRECIO_TOTAL", detalleVenta.getPrecioTotal());
        params.put("CANTIDAD_REAL", detalleVenta.getCantidadReal());

        if(detalleVenta.getLote() != null)
            params.put("LOTE_ID", detalleVenta.getLote().getId());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int res = detalleVentaMapper.updateByPrimaryKeySelective(params);

        //this.recalcularVenta(venta);
        this.modificarStocks(Constantes.TIPO_ENTRADA_PRODUCTOS, stockG1.get(0), loteBD1, (detalleVentaBD.getCantidad() * detalleVentaBD.getCantidadReal()));
        this.modificarStocks(Constantes.TIPO_RETIRO_PRODUCTOS, stockG1.get(0), loteBD2, (detalleVenta.getCantidad() * detalleVenta.getCantidadReal()));

        return venta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public Venta grabarResetVenta(Venta venta) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("VENTA_ID",venta.getId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<DetalleVenta> detallesVentas = detalleVentaMapper.listByParameterMap(params);
        for (DetalleVenta dv: detallesVentas) {
            params.clear();
            params.put("PRODUCTO_ID", dv.getProducto().getId());
            params.put("ALMACEN_ID", dv.getAlmacenId());
            params.put("EMPRESA_ID", EmpresaId);

            List<Stock> stockG1 = stockMapper.listByParameterMap(params);
            Lote loteBD = null;

            if(dv.getLote() != null && dv.getLote().getId() != null)
                loteBD = loteDAO.listarPorId(dv.getLote().getId());


            this.modificarStocks(Constantes.TIPO_ENTRADA_PRODUCTOS, stockG1.get(0), loteBD, dv.getCantidad() * dv.getCantidadReal());
            detalleVentaDAO.eliminar(dv.getId());
        }

        return venta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void devolverStockVenta(Long ventaId) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("VENTA_ID", ventaId);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<DetalleVenta> detallesVentas = detalleVentaMapper.listByParameterMap(params);
        for (DetalleVenta dv: detallesVentas) {
            params.clear();
            params.put("PRODUCTO_ID", dv.getProducto().getId());
            params.put("ALMACEN_ID", dv.getAlmacenId());
            params.put("EMPRESA_ID", EmpresaId);

            List<Stock> stockG1 = stockMapper.listByParameterMap(params);
            Lote loteBD = null;

            if(dv.getLote() != null && dv.getLote().getId() != null)
                loteBD = loteDAO.listarPorId(dv.getLote().getId());


            this.modificarStocks(Constantes.TIPO_ENTRADA_PRODUCTOS, stockG1.get(0), loteBD, dv.getCantidad() * dv.getCantidadReal());
            //detalleVentaDAO.eliminar(dv.getId());
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public Venta grabarDeleteDetalle(Venta venta, DetalleVenta detalleVenta) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PRODUCTO_ID", detalleVenta.getProducto().getId());
        params.put("ALMACEN_ID", detalleVenta.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        Lote loteBD = null;

        if(detalleVenta.getLote() != null && detalleVenta.getLote().getId() != null)
            loteBD = loteDAO.listarPorId(detalleVenta.getLote().getId());


        this.modificarStocks(Constantes.TIPO_ENTRADA_PRODUCTOS, stockG1.get(0), loteBD, detalleVenta.getCantidad() * detalleVenta.getCantidadReal());
        detalleVentaDAO.eliminar(detalleVenta.getId());
        //this.recalcularVenta(venta);

        return venta;
    }

    private void modificarStocks(Integer tipoMovimiento, Stock stockModificar, Lote loteBD, Double cantidad) throws Exception {

        //Modificaion Stock
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        LocalDateTime fechaActualTime = LocalDateTime.now();

        if(tipoMovimiento.equals(Constantes.TIPO_ENTRADA_PRODUCTOS))
            stockModificar.setCantidad(stockModificar.getCantidad() + cantidad);

        if(tipoMovimiento.equals(Constantes.TIPO_RETIRO_PRODUCTOS))
            stockModificar.setCantidad(stockModificar.getCantidad() - cantidad);

        stockModificar.setUpdatedAd(fechaActualTime);
        stockDAO.modificar(stockModificar);

        if(loteBD != null && loteBD.getId() != null && loteBD.getId() > 0){

            //Modificaion Stock Lote
            if(tipoMovimiento.equals(Constantes.TIPO_ENTRADA_PRODUCTOS))
                loteBD.setCantidad(loteBD.getCantidad() + cantidad);

            if(tipoMovimiento.equals(Constantes.TIPO_RETIRO_PRODUCTOS))
                loteBD.setCantidad(loteBD.getCantidad() - cantidad);

            loteBD.setUpdatedAd(fechaActualTime);
            loteDAO.modificar(loteBD);

        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private Venta recalcularVenta(Venta venta) throws Exception {
        return  this.recalcularVenta(venta, null);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private Venta recalcularVenta(Venta venta, Long idRemove) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("VENTA_ID",venta.getId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        if(idRemove != null)
            params.put("NO_ID",idRemove);

        List<DetalleVenta> detallesVentas = detalleVentaMapper.listByParameterMap(params);

        BigDecimal subTotalInafecto = new BigDecimal(0);
        BigDecimal subTotalAfecto = new BigDecimal(0);
        BigDecimal subtotalExonerado = new BigDecimal(0);
        BigDecimal totalVenta = new BigDecimal(0);

        BigDecimal subTotalISC = new BigDecimal(0);

        BigDecimal igv = new BigDecimal(0);
        BigDecimal isc = new BigDecimal(0);

        for (DetalleVenta dv: detallesVentas) {
            if(dv.getEsGrabado().intValue() == Constantes.GRABADO_IGV.intValue())
                subTotalAfecto = subTotalAfecto.add(dv.getPrecioTotal());
            if(dv.getEsGrabado().intValue() == Constantes.INAFECTO_IGV.intValue())
                subTotalInafecto = subTotalInafecto.add(dv.getPrecioTotal());
            if(dv.getEsGrabado().intValue() == Constantes.EXONERADO_IGV.intValue())
                subtotalExonerado = subtotalExonerado.add(dv.getPrecioTotal());


            if(dv.getEsIsc().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
                subTotalISC = subTotalISC.add(dv.getPrecioTotal());

                if(dv.getProducto().getTipoTasaIsc().intValue() == Constantes.TIPO_ISC_PERCENTIL.intValue()){
                    BigDecimal iscProd = dv.getPrecioTotal().subtract(dv.getPrecioTotal().multiply(BigDecimal.valueOf(dv.getProducto().getTasaIsc())).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
                    isc = isc.add(iscProd);

                }
                if(dv.getProducto().getTipoTasaIsc().intValue() == Constantes.TIPO_ISC_FIJO.intValue()){
                    BigDecimal iscProd = dv.getPrecioTotal().subtract(BigDecimal.valueOf(dv.getProducto().getTasaIsc()));
                    isc = isc.add(iscProd);
                }
            }
        }

        totalVenta = totalVenta.add(subTotalInafecto);
        totalVenta = totalVenta.add(subTotalAfecto);
        totalVenta = totalVenta.add(subtotalExonerado);

        BigDecimal totalICBPER = new BigDecimal(0);
        totalICBPER = venta.getMontoIcbper().multiply(new BigDecimal(venta.getCantidadIcbper()));
        totalVenta = totalVenta.add(totalICBPER);


        venta.setSubtotalInafecto(subTotalInafecto);
        venta.setSubtotalAfecto(subTotalAfecto);
        venta.setSubtotalExonerado(subtotalExonerado);
        venta.setTotalMonto(totalVenta);
        venta.setTotalAfectoIsc(subTotalISC);

        //Calc IGV
        BigDecimal base = subTotalAfecto.divide( new BigDecimal(Constantes.PERCENTIL_IGV), 2, RoundingMode.HALF_UP);
        igv = subTotalAfecto.subtract(base);
        venta.setIgv(igv);

        //Calc ISC
        venta.setIsc(isc);

        venta.setDetalleVentas(detallesVentas);

        //Update Venta

        params.clear();
        params.put("ID", venta.getId());
        params.put("SUBTOTAL_INAFECTO", venta.getSubtotalInafecto());
        params.put("SUBTOTAL_AFECTO", venta.getSubtotalAfecto());
        params.put("SUBTOTAL_EXONERADO", venta.getSubtotalExonerado());
        params.put("TOTAL_MONTO", venta.getTotalMonto());
        params.put("TOTAL_AFECTO_ISC", venta.getSubtotalAfecto());
        params.put("IGV", venta.getIgv());
        params.put("ISC", venta.getIsc());
        params.put("USER_ID", venta.getUser().getId());
        params.put("UPDATED_AT", venta.getUpdatedAd());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int resultado = ventaMapper.updateByPrimaryKeySelective(params);

        return venta;

    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private Venta recalcularVentaReset(Venta venta) throws Exception {

        BigDecimal subTotalInafecto = new BigDecimal(0);
        BigDecimal subTotalAfecto = new BigDecimal(0);
        BigDecimal subtotalExonerado = new BigDecimal(0);
        BigDecimal totalVenta = new BigDecimal(0);

        BigDecimal subTotalISC = new BigDecimal(0);

        BigDecimal igv = new BigDecimal(0);
        BigDecimal isc = new BigDecimal(0);


        totalVenta = totalVenta.add(subTotalInafecto);
        totalVenta = totalVenta.add(subTotalAfecto);
        totalVenta = totalVenta.add(subtotalExonerado);

        BigDecimal totalICBPER = new BigDecimal(0);
        totalICBPER = venta.getMontoIcbper().multiply(new BigDecimal(venta.getCantidadIcbper()));
        totalVenta = totalVenta.add(totalICBPER);


        venta.setSubtotalInafecto(subTotalInafecto);
        venta.setSubtotalAfecto(subTotalAfecto);
        venta.setSubtotalExonerado(subtotalExonerado);
        venta.setTotalMonto(totalVenta);
        venta.setTotalAfectoIsc(subTotalISC);

        //Calc IGV
        BigDecimal base = subTotalAfecto.divide( new BigDecimal(Constantes.PERCENTIL_IGV), 2, RoundingMode.HALF_UP);
        igv = subTotalAfecto.subtract(base);
        venta.setIgv(igv);

        //Calc ISC
        venta.setIsc(isc);

        venta.setDetalleVentas(new ArrayList<>());


        venta.setCliente(null);
        venta.setCantidadIcbper(Constantes.CANTIDAD_ZERO);
        venta.setMontoIcbper(Constantes.CANTIDAD_ICBPER_2023);

        //Update Venta

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", venta.getId());
        params.put("SUBTOTAL_INAFECTO", venta.getSubtotalInafecto());
        params.put("SUBTOTAL_AFECTO", venta.getSubtotalAfecto());
        params.put("SUBTOTAL_EXONERADO", venta.getSubtotalExonerado());
        params.put("TOTAL_MONTO", venta.getTotalMonto());
        params.put("TOTAL_AFECTO_ISC", venta.getSubtotalAfecto());
        params.put("IGV", venta.getIgv());
        params.put("ISC", venta.getIsc());
        params.put("USER_ID", venta.getUser().getId());
        params.put("UPDATED_AT", venta.getUpdatedAd());
        params.put("CLIENTE_ID_NULLABLE", Constantes.CANTIDAD_UNIDAD);
        params.put("CANTIDAD_ICBPER", venta.getCantidadIcbper());
        params.put("MONTO_ICBPER", venta.getMontoIcbper());

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int resultado = ventaMapper.updateByPrimaryKeySelective(params);

        return venta;

    }

    @Override
    public Venta eliminarDetalle(DetalleVenta detalleVenta) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        detalleVenta.setUpdatedAd(fechaActualTime);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = false;
        Venta venta = this.listarPorId(detalleVenta.getVentaIdReference());

        validacion =this.validacionDeleteVentaDetalle(detalleVenta, resultValidacion);

        if(validacion){
            detalleVenta.setVenta(venta);
            this.grabarDeleteDetalle(venta, detalleVenta);
            Venta ventaRes = this.recalcularVenta(venta, detalleVenta.getId());
            return ventaRes;
        }



        String errorValidacion = "Error de validación Agregar Productos a Venta de Bienes";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void grabarEliminar(Long id) throws Exception {
        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        //Devolver Stocks
        this.devolverStockVenta(id);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("UPDATED_AT",fechaUpdate);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int res= ventaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar la venta indicada, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }


    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private void grabarAnular(Long id) throws Exception {
        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        //Devolver Stocks
        this.devolverStockVenta(id);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("ESTADO",Constantes.VENTA_ESTADO_ANULADO);
        params.put("UPDATED_AT",fechaUpdate);

        params.put("EMPRESA_ID",claimsAuthorization.getEmpresaId());
        int res= ventaMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo anular la venta indicada, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
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

        Venta venta = this.listarPorId(id);

        if(venta.getEstado().intValue() == Constantes.VENTA_ESTADO_ANULADO){
            resultado = false;
            error = "La Venta remitida se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(venta.getEstado().intValue() > Constantes.VENTA_ESTADO_INICIADO){
            if(venta.getComprobante() != null && venta.getComprobante().getInitComprobante() != null && venta.getComprobante().getInitComprobante().getId() != null){

                InitComprobante initComprobante = new InitComprobante();
                initComprobante = initComprobanteService.listarPorId(venta.getComprobante().getInitComprobante().getId());

                if(initComprobante.getTipoComprobante().getPrefix().equals(Constantes.COMPROBANTE_PREFIJO_FACTURA)){
                    resultado = false;
                    error = "No se puede Eliminar una Ventas con Comprobante de Tipo Factura";
                    errors.add(error);
                }
                if(initComprobante.getTipoComprobante().getPrefix().equals(Constantes.COMPROBANTE_PREFIJO_BOLETA)){
                    resultado = false;
                    error = "No se puede Eliminar una Ventas con Comprobante de Tipo Boleta";
                    errors.add(error);
                }
            }
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
    private boolean validacionAnulacion(Long id, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        Venta venta = this.listarPorId(id);

        if(venta.getEstado().intValue() == Constantes.VENTA_ESTADO_ANULADO){
            resultado = false;
            error = "La Venta remitida ya se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(venta.getEstado().intValue() == Constantes.VENTA_ESTADO_INICIADO){
            resultado = false;
            error = "No se puede Anular una venta que aun no fue pagada y que no se ha generado ningún comprobante";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public Page<Venta> listar(Pageable page, FiltroVenta filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getNumeroVenta() != null && !filtros.getNumeroVenta().trim().isEmpty())
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

        if(filtros.getTipoVenta() != null && filtros.getTipoVenta() > Constantes.CANTIDAD_ZERO)
            params.put("TIPO", filtros.getTipoVenta());

        if(filtros.getIdCliente() != null && filtros.getIdCliente().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CLIENTE_ID", filtros.getIdCliente());

        if(filtros.getNombreCliente() != null && !filtros.getNombreCliente().trim().isEmpty())
            params.put("CLI_NOMBRE", "%"+filtros.getNombreCliente()+"%");

        if(filtros.getDocumentoCliente() != null && !filtros.getDocumentoCliente().trim().isEmpty())
            params.put("CLI_DOCUMENTO", filtros.getDocumentoCliente());

        if(filtros.getIdTipoDocumentoCliente() != null)
            params.put("CLI_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoCliente());

        if(filtros.getIdComprobante() != null && filtros.getIdComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("COMPROBANTE_ID", filtros.getIdComprobante());

        if(filtros.getSerieComprobante() != null && !filtros.getSerieComprobante().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieComprobante());

        if(filtros.getNumeroComprobante() != null && !filtros.getNumeroComprobante().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroComprobante());

        if(filtros.getIdTipoComprobante() != null && filtros.getIdTipoComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CO_TIPO_COMPROBANTE_ID", filtros.getIdTipoComprobante());

        if(filtros.getIdUser() != null && filtros.getIdUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("USER_ID", filtros.getIdUser());

        if(filtros.getNameUser() != null && !filtros.getNameUser().trim().isEmpty())
            params.put("U_NAME", filtros.getNameUser());

        if(filtros.getEmailUser() != null && !filtros.getEmailUser().trim().isEmpty())
            params.put("U_EMAIL", filtros.getEmailUser());

        if(filtros.getIdTipoUser() != null && filtros.getIdTipoUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("U_TIPO_USER_ID", filtros.getIdTipoUser());

        if(filtros.getBuscarDatosUser() != null && !filtros.getBuscarDatosUser().trim().isEmpty())
            params.put("U_BUSCAR", filtros.getBuscarDatosUser());

        //Buscar General
        if(filtros.getBuscarDatos() != null && !filtros.getBuscarDatos().trim().isEmpty())
            params.put("BUSCAR_GENERAL", "%"+filtros.getBuscarDatos()+"%");


        Long total = ventaMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Venta> ventas = ventaMapper.listByParameterMap(params);

        ventas.forEach((venta) -> {
            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_ANULADO))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_ANULADO_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_INICIADO))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_INICIADO_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR);

            if(venta.getCliente() != null && venta.getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(venta.getCliente().getTipoDocumento().getId());
                    venta.getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if(venta.getComprobante() != null && venta.getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(venta.getComprobante().getInitComprobante().getId());
                    venta.getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //ventas = ventas.stream().sorted((Comparator.comparing(Venta::getId))).collect(Collectors.toList());

        return new PageImpl<>(ventas, page, total);
    }

    @Override
    public Page<VentasDetallesDTO> listarDetallado(Pageable page, FiltroVenta filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getNumeroVenta() != null && !filtros.getNumeroVenta().trim().isEmpty())
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

        if(filtros.getTipoVenta() != null && filtros.getTipoVenta() > Constantes.CANTIDAD_ZERO)
            params.put("TIPO", filtros.getTipoVenta());

        if(filtros.getIdCliente() != null && filtros.getIdCliente().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CLIENTE_ID", filtros.getIdCliente());

        if(filtros.getNombreCliente() != null && !filtros.getNombreCliente().trim().isEmpty())
            params.put("CLI_NOMBRE", "%"+filtros.getNombreCliente()+"%");

        if(filtros.getDocumentoCliente() != null && !filtros.getDocumentoCliente().trim().isEmpty())
            params.put("CLI_DOCUMENTO", filtros.getDocumentoCliente());

        if(filtros.getIdTipoDocumentoCliente() != null)
            params.put("CLI_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoCliente());

        if(filtros.getIdComprobante() != null && filtros.getIdComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("COMPROBANTE_ID", filtros.getIdComprobante());

        if(filtros.getSerieComprobante() != null && !filtros.getSerieComprobante().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieComprobante());

        if(filtros.getNumeroComprobante() != null && !filtros.getNumeroComprobante().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroComprobante());

        if(filtros.getIdTipoComprobante() != null && filtros.getIdTipoComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CO_TIPO_COMPROBANTE_ID", filtros.getIdTipoComprobante());

        if(filtros.getIdUser() != null && filtros.getIdUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("USER_ID", filtros.getIdUser());

        if(filtros.getNameUser() != null && !filtros.getNameUser().trim().isEmpty())
            params.put("U_NAME", filtros.getNameUser());

        if(filtros.getEmailUser() != null && !filtros.getEmailUser().trim().isEmpty())
            params.put("U_EMAIL", filtros.getEmailUser());

        if(filtros.getIdTipoUser() != null && filtros.getIdTipoUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("U_TIPO_USER_ID", filtros.getIdTipoUser());

        if(filtros.getBuscarDatosUser() != null && !filtros.getBuscarDatosUser().trim().isEmpty())
            params.put("U_BUSCAR", filtros.getBuscarDatosUser());

        //Buscar General
        if(filtros.getBuscarDatos() != null && !filtros.getBuscarDatos().trim().isEmpty())
            params.put("BUSCAR_GENERAL", "%"+filtros.getBuscarDatos()+"%");

        //Filtro para Detalle de Venta
        if(filtros.getIdProducto() != null && filtros.getIdProducto().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("PRODUCTO_ID", filtros.getIdProducto());
        }


        Long total = ventaMapper.getTotalElementsPagarDetail(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<VentasDetallesDTO> ventasDTO = ventaMapper.listDetailByParameterMap(params);

        ventasDTO.forEach((dto) -> {
            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_ANULADO))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_ANULADO_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_INICIADO))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_INICIADO_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR);

            if(dto.getVenta().getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                dto.getVenta().setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR);

            if(dto.getVenta().getCliente() != null && dto.getVenta().getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(dto.getVenta().getCliente().getTipoDocumento().getId());
                    dto.getVenta().getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if(dto.getVenta().getComprobante() != null && dto.getVenta().getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(dto.getVenta().getComprobante().getInitComprobante().getId());
                    dto.getVenta().getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //ventas = ventas.stream().sorted((Comparator.comparing(Venta::getId))).collect(Collectors.toList());

        return new PageImpl<>(ventasDTO, page, total);
    }

    @Override
    public Page<TopProductosVendidosDTO> listarTopProductosVendidos(Pageable page, FiltroVenta filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getNumeroVenta() != null && !filtros.getNumeroVenta().trim().isEmpty())
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

        if(filtros.getTipoVenta() != null && filtros.getTipoVenta() > Constantes.CANTIDAD_ZERO)
            params.put("TIPO", filtros.getTipoVenta());

        if(filtros.getIdCliente() != null && filtros.getIdCliente().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CLIENTE_ID", filtros.getIdCliente());

        if(filtros.getNombreCliente() != null && !filtros.getNombreCliente().trim().isEmpty())
            params.put("CLI_NOMBRE", "%"+filtros.getNombreCliente()+"%");

        if(filtros.getDocumentoCliente() != null && !filtros.getDocumentoCliente().trim().isEmpty())
            params.put("CLI_DOCUMENTO", filtros.getDocumentoCliente());

        if(filtros.getIdTipoDocumentoCliente() != null)
            params.put("CLI_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoCliente());

        if(filtros.getIdComprobante() != null && filtros.getIdComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("COMPROBANTE_ID", filtros.getIdComprobante());

        if(filtros.getSerieComprobante() != null && !filtros.getSerieComprobante().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieComprobante());

        if(filtros.getNumeroComprobante() != null && !filtros.getNumeroComprobante().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroComprobante());

        if(filtros.getIdTipoComprobante() != null && filtros.getIdTipoComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CO_TIPO_COMPROBANTE_ID", filtros.getIdTipoComprobante());

        if(filtros.getIdUser() != null && filtros.getIdUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("USER_ID", filtros.getIdUser());

        if(filtros.getNameUser() != null && !filtros.getNameUser().trim().isEmpty())
            params.put("U_NAME", filtros.getNameUser());

        if(filtros.getEmailUser() != null && !filtros.getEmailUser().trim().isEmpty())
            params.put("U_EMAIL", filtros.getEmailUser());

        if(filtros.getIdTipoUser() != null && filtros.getIdTipoUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("U_TIPO_USER_ID", filtros.getIdTipoUser());

        if(filtros.getBuscarDatosUser() != null && !filtros.getBuscarDatosUser().trim().isEmpty())
            params.put("U_BUSCAR", filtros.getBuscarDatosUser());

        //Buscar General
        if(filtros.getBuscarDatos() != null && !filtros.getBuscarDatos().trim().isEmpty())
            params.put("BUSCAR_GENERAL", "%"+filtros.getBuscarDatos()+"%");

        //Filtro para Detalle de Venta
        if(filtros.getTipoProductoId() != null && filtros.getTipoProductoId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("TIPO_PRODUCTO_ID", filtros.getTipoProductoId());
        }

        if(filtros.getPresentacionId() != null && filtros.getPresentacionId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("PRESENTACION_ID", filtros.getPresentacionId());
        }

        if(filtros.getMarcaId() != null && filtros.getMarcaId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("MARCA_ID", filtros.getMarcaId());
        }


        Long total = ventaMapper.getTotalElementsTopProductosVendidos(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<TopProductosVendidosDTO> ventasDTO = ventaMapper.listTopProductosVendidosByParameterMap(params);

        return new PageImpl<>(ventasDTO, page, total);
    }

    @Override
    public Page<Venta> listarCobrado(Pageable page, FiltroVenta filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);
        params.put("ESTADO_MAYOR", Constantes.VENTA_ESTADO_INICIADO);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getNumeroVenta() != null && !filtros.getNumeroVenta().trim().isEmpty())
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

        if(filtros.getTipoVenta() != null && filtros.getTipoVenta() > Constantes.CANTIDAD_ZERO)
            params.put("TIPO", filtros.getTipoVenta());

        if(filtros.getIdCliente() != null && filtros.getIdCliente().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CLIENTE_ID", filtros.getIdCliente());

        if(filtros.getNombreCliente() != null && !filtros.getNombreCliente().trim().isEmpty())
            params.put("CLI_NOMBRE", "%"+filtros.getNombreCliente()+"%");

        if(filtros.getDocumentoCliente() != null && !filtros.getDocumentoCliente().trim().isEmpty())
            params.put("CLI_DOCUMENTO", filtros.getDocumentoCliente());

        if(filtros.getIdTipoDocumentoCliente() != null)
            params.put("CLI_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoCliente());

        if(filtros.getIdComprobante() != null && filtros.getIdComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("COMPROBANTE_ID", filtros.getIdComprobante());

        if(filtros.getSerieComprobante() != null && !filtros.getSerieComprobante().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieComprobante());

        if(filtros.getNumeroComprobante() != null && !filtros.getNumeroComprobante().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroComprobante());

        if(filtros.getIdTipoComprobante() != null && filtros.getIdTipoComprobante().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("CO_TIPO_COMPROBANTE_ID", filtros.getIdTipoComprobante());

        if(filtros.getIdUser() != null && filtros.getIdUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("USER_ID", filtros.getIdUser());

        if(filtros.getNameUser() != null && !filtros.getNameUser().trim().isEmpty())
            params.put("U_NAME", filtros.getNameUser());

        if(filtros.getEmailUser() != null && !filtros.getEmailUser().trim().isEmpty())
            params.put("U_EMAIL", filtros.getEmailUser());

        if(filtros.getIdTipoUser() != null && filtros.getIdTipoUser().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("U_TIPO_USER_ID", filtros.getIdTipoUser());

        if(filtros.getBuscarDatosUser() != null && !filtros.getBuscarDatosUser().trim().isEmpty())
            params.put("U_BUSCAR", filtros.getBuscarDatosUser());

        //Buscar General
        if(filtros.getBuscarDatos() != null && !filtros.getBuscarDatos().trim().isEmpty())
            params.put("BUSCAR_GENERAL", "%"+filtros.getBuscarDatos()+"%");


        Long total = ventaMapper.getTotalElementsCobrar(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<Venta> ventas = ventaMapper.listByParameterMapCobrar(params);

        ventas.forEach((venta) -> {
            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_ANULADO))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_ANULADO_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_INICIADO))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_INICIADO_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_NO_COBRADA_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR);

            if(venta.getEstado().equals(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL))
                venta.setEstadoStr(Constantes.VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR);

            if(venta.getCliente() != null && venta.getCliente().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(venta.getCliente().getTipoDocumento().getId());
                    venta.getCliente().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if(venta.getComprobante() != null && venta.getComprobante().getInitComprobante() != null){
                try {
                    InitComprobante initComprobante = initComprobanteDAO.listarPorId(venta.getComprobante().getInitComprobante().getId());
                    venta.getComprobante().setInitComprobante(initComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //ventas = ventas.stream().sorted((Comparator.comparing(Venta::getId))).collect(Collectors.toList());

        return new PageImpl<>(ventas, page, total);
    }


    private boolean validacionRegistroCobro(CobroVenta cobroVenta, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(cobroVenta.getVenta() == null){
            resultado = false;
            error = "La Venta remitida no es válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(cobroVenta.getVenta().getCliente() == null){
            resultado = false;
            error = "Debe de seleccionar un Cliente";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("VENTA_ID",cobroVenta.getVenta().getId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<DetalleVenta> detallesVentas = detalleVentaMapper.listByParameterMap(params);

        if(detallesVentas == null || detallesVentas.isEmpty()){
            resultado = false;
            error = "Debe de agregar Productos a la Venta";
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

        if(cobroVenta.getInitComprobanteId() == null || cobroVenta.getInitComprobanteId().compareTo(Constantes.CANTIDAD_ZERO_LONG) < 0){
            resultado = false;
            error = "Debe de seleccionar una Serie de Método de Pago Válida";
            errors.add(error);
        } else{
            InitComprobante initComprobante = new InitComprobante();
            initComprobante = initComprobanteService.listarPorId(cobroVenta.getInitComprobanteId());

            if(initComprobante.getTipoComprobante().getPrefix().equals(Constantes.COMPROBANTE_PREFIJO_FACTURA)){
                if(cobroVenta.getVenta().getCliente().getTipoDocumento().getKey().equals(Constantes.COMPROBANTE_TIPO_OTROS_DOCUMENTOS)){
                    resultado = false;
                    error = "No se puede Emitir Factura al tipo de Cliente con tipo de Documento Otros Documentos";
                    errors.add(error);
                }
                if(cobroVenta.getVenta().getCliente().getTipoDocumento().getKey().equals(Constantes.COMPROBANTE_TIPO_DOCUMENTO_DNI)){
                    resultado = false;
                    error = "No se puede Emitir Factura al tipo de Cliente con tipo de Documento DNI";
                    errors.add(error);
                }
                if(cobroVenta.getVenta().getCliente().getTipoDocumento().getKey().equals(Constantes.COMPROBANTE_TIPO_DOCUMENTO_PARTIDA_NACIMIENTO)){
                    resultado = false;
                    error = "No se puede Emitir Factura al tipo de Cliente con tipo de Documento Partida de Nacimiento";
                    errors.add(error);
                }
            }
        }



        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    private boolean validacionGenerarComprobante(Venta venta, Venta ventaBD, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(venta.getCliente() == null){
            resultado = false;
            error = "Debe de seleccionar un Cliente";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(ventaBD.getEstado().intValue() == Constantes.VENTA_ESTADO_ANULADO){
            resultado = false;
            error = "La Venta remitida se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(venta.getInitComprobanteId() == null || venta.getInitComprobanteId().compareTo(Constantes.CANTIDAD_ZERO_LONG) < 0){
            resultado = false;
            error = "Debe de seleccionar una Serie de Método de Pago Válida";
            errors.add(error);
        } else{
            InitComprobante initComprobante = new InitComprobante();
            initComprobante = initComprobanteService.listarPorId(venta.getInitComprobanteId());

            if(initComprobante.getTipoComprobante().getPrefix().equals(Constantes.COMPROBANTE_PREFIJO_NOTA_VENTA)){
                resultado = false;
                error = "El comprobante a generar debe de ser una Factura o una Boleta";
                errors.add(error);
            }

            if(initComprobante.getTipoComprobante().getPrefix().equals(Constantes.COMPROBANTE_PREFIJO_FACTURA)){
                if(venta.getCliente().getTipoDocumento().getKey().equals(Constantes.COMPROBANTE_TIPO_OTROS_DOCUMENTOS)){
                    resultado = false;
                    error = "No se puede Emitir Factura al tipo de Cliente con tipo de Documento Otros Documentos";
                    errors.add(error);
                }
                if(venta.getCliente().getTipoDocumento().getKey().equals(Constantes.COMPROBANTE_TIPO_DOCUMENTO_DNI)){
                    resultado = false;
                    error = "No se puede Emitir Factura al tipo de Cliente con tipo de Documento DNI";
                    errors.add(error);
                }
                if(venta.getCliente().getTipoDocumento().getKey().equals(Constantes.COMPROBANTE_TIPO_DOCUMENTO_PARTIDA_NACIMIENTO)){
                    resultado = false;
                    error = "No se puede Emitir Factura al tipo de Cliente con tipo de Documento Partida de Nacimiento";
                    errors.add(error);
                }
            }
        }



        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }









    private Long GetSequence(FiltroVenta filtros) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        long sequence = ventaMapper.getTotalElements(params);
        sequence++;

        return sequence;
    }

    @Override
    public Page<CobroVenta> listarPagos(Pageable page, Long id) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);

        params.put("VENTA_ID", id);
        params.put("MAYOR_MIN_IMPORTE", Constantes.CANTIDAD_ZERO);


        Long total = cobroVentaMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<CobroVenta> cobroVentas = cobroVentaMapper.listByParameterMap(params);

        return new PageImpl<>(cobroVentas, page, total);
    }
}

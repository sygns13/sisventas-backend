package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.*;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.dto.ProductosVentaDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.AlmacenService;
import com.bcs.ventas.service.VentaService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.AgregarProductoBean;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

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
        params.put("USER_ID", v.getUser().getId());
        params.put("UPDATED_AT", v.getUpdatedAd());

        int resultado = ventaMapper.updateByPrimaryKeySelective(params);

        return v;
    }

    @Override
    public Venta agregarProducto(AgregarProductoBean addProductoVenta) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final


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
            error = "No se envió un Producto Válido";
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
            error = "No se tiene stock del producto " +productoVenta.getProducto().getNombre()+ " para la canitidad solicitada de " + productoVenta.getDetalleUnidadProducto().getUnidad().getCantidad() + "unidades";
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

        //TODO: Temporal hasta incluir Oauth inicio
        detalleVenta.setEmpresaId(1L);
        detalleVenta.setUserId(2L);
        //user.setEmpresaId(1L);
        //user = userDAO.listarPorId(user.getId());
        //Todo: Temporal hasta incluir Oauth final

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
                Venta ventaRes = this.recalcularVenta(venta);
                return ventaRes;
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

        this.graberResetVenta(venta);
        Venta ventaRes = this.recalcularVentaReset(venta);
        return ventaRes;

    }

    private boolean validacionVentaDetalle(DetalleVenta detalleVenta, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final
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

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final
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

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        DetalleVenta d = detalleVentaDAO.registrar(detalleVenta);

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

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

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

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

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

        int res = detalleVentaMapper.updateByPrimaryKeySelective(params);

        //this.recalcularVenta(venta);
        this.modificarStocks(Constantes.TIPO_ENTRADA_PRODUCTOS, stockG1.get(0), loteBD1, (detalleVentaBD.getCantidad() * detalleVentaBD.getCantidadReal()));
        this.modificarStocks(Constantes.TIPO_RETIRO_PRODUCTOS, stockG1.get(0), loteBD2, (detalleVenta.getCantidad() * detalleVenta.getCantidadReal()));

        return venta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public Venta graberResetVenta(Venta venta) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

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
    public Venta grabarDeleteDetalle(Venta venta, DetalleVenta detalleVenta) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

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

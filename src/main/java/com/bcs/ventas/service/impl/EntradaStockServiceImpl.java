package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.*;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.dto.ProductosVentaDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.AlmacenService;
import com.bcs.ventas.service.EntradaStockService;
import com.bcs.ventas.service.LoteService;
import com.bcs.ventas.service.TipoDocumentoService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.AgregarProductoBean;
import com.bcs.ventas.utils.beans.FiltroEntradaStock;
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
import java.util.stream.Collectors;

@Transactional
@Service
public class EntradaStockServiceImpl implements EntradaStockService {

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private EntradaStockMapper entradaStockMapper;

    @Autowired
    private AlmacenService almacenService;

    @Autowired
    private EntradaStockDAO entradaStockDAO;
    @Autowired
    private DetalleEntradaStockDAO detalleEntradaStockDAO;

    @Autowired
    private DetalleEntradaStockMapper detalleEntradaStockMapper;

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
    private  FacturaProveedorDAO facturaProveedorDAO;

    @Autowired
    private  FacturaProveedorMapper facturaProveedorMapper;

    @Autowired
    private  PagoProveedorDAO pagoProveedorDAO;

    @Autowired
    private  PagoProveedorMapper pagoProveedorMapper;

    @Autowired
    private TipoDocumentoService tipoDocumentoService;

    @Autowired
    private TipoDocumentoMapper tipoDocumentoMapper;

    @Autowired
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Autowired
    private TipoComprobanteDAO tipoComprobanteDAO;

    @Autowired
    private TipoComprobanteMapper tipoComprobanteMapper;

    @Autowired
    private ProveedorMapper proveedorMapper;

    @Autowired
    private LoteService loteService;
    @Override
    public EntradaStock registrar(EntradaStock entradaStock) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        entradaStock.setCreatedAt(fechaActualTime);
        entradaStock.setUpdatedAd(fechaActualTime);

        if(entradaStock.getFecha() == null)
            entradaStock.setFecha(fechaActual);

        if(entradaStock.getHora() == null)
            entradaStock.setHora(horaActual);

        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        entradaStock.setEmpresaId(1L);
        user.setId(2L);
        //user.setEmpresaId(1L);
        //user = userDAO.listarPorId(user.getId());
        //Todo: Temporal hasta incluir Oauth final

        entradaStock.setUser(user);
        entradaStock.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        entradaStock.setActivo(Constantes.REGISTRO_ACTIVO);

        if(entradaStock.getFacturado() == null) entradaStock.setFacturado(Constantes.CANTIDAD_ZERO);
        if(entradaStock.getActualizado() == null) entradaStock.setActualizado(Constantes.CANTIDAD_ZERO);
        if(entradaStock.getTotalMonto() == null) entradaStock.setTotalMonto(Constantes.CANTIDAD_ZERO_BIG_DECIMAL);

        entradaStock.setEstado(Constantes.COMPRA_ESTADO_INICIADO);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(entradaStock, resultValidacion);

        if(validacion){
            //Get Sequence
            FiltroEntradaStock filtroSeq= new FiltroEntradaStock();
            filtroSeq.setAlmacenId(entradaStock.getAlmacen().getId());

            Long sequence = this.GetSequence(filtroSeq);
            String numeroCompra = "";

            numeroCompra = "C-" + entradaStock.getAlmacen().getNombre().charAt(0) + StringUtils.leftPad(entradaStock.getAlmacen().getId().toString(), 3,"0") + "-" + StringUtils.leftPad(sequence.toString(), 10,"0");
            entradaStock.setNumero(numeroCompra);

            return this.grabarRegistro(entradaStock);
        }


        String errorValidacion = "Error de validación Método Registrar Entrada Stock de Bienes Inicial";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(EntradaStock entradaStock) throws Exception {
        return 0;
    }

    @Override
    public EntradaStock modificarEntradaStock(EntradaStock entradaStock) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();

        entradaStock.setUpdatedAd(fechaActualTime);

        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        entradaStock.setUser(user);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(entradaStock, resultValidacion);

        if(validacion){
            int res = this.grabarRectificar(entradaStock);
            if(res == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                EntradaStock e = this.listarPorId(entradaStock.getId());
                EntradaStock entradaStockRes = this.recalcularEntradaStock(e);
                return entradaStockRes;
            }
        }


        String errorValidacion = "Error de validación Método Modificar Entrada Stock de Bienes";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public EntradaStock modificarEntradaStockProveedor(EntradaStock entradaStock) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();

        entradaStock.setCreatedAt(fechaActualTime);
        entradaStock.setUpdatedAd(fechaActualTime);

        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setId(2L);
        //Todo: Temporal hasta incluir Oauth final

        entradaStock.setUser(user);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificadoProveedor(entradaStock, resultValidacion);

        if(validacion){
            int res = this.grabarRectificarProveedor(entradaStock);
            EntradaStock resp = new EntradaStock();
            if(res == Constantes.CANTIDAD_UNIDAD_INTEGER.intValue()){
                resp = this.listarPorId(entradaStock.getId());
            }
            return resp;
        }


        String errorValidacion = "Error de validación Método Modificar Proveedor de la Compra";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public EntradaStock modificarEntradaStockProveedorFirst(EntradaStock entradaStock) throws Exception {
        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO_2);
        params.put("EMPRESA_ID",EmpresaId);

        List<Proveedor> proveedors = proveedorMapper.listByParameterMap(params);

        if(proveedors.size() > 0){
            entradaStock.setProveedor(proveedors.get(0));
            return this.modificarEntradaStockProveedor(entradaStock);
        }
        return entradaStock;
    }

    @Override
    public List<EntradaStock> listar() throws Exception {
        return null;
    }

    @Override
    public EntradaStock listarPorId(Long id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<EntradaStock> entradaStocks = entradaStockMapper.listByParameterMap(params);

        if(!entradaStocks.isEmpty()){
            params.clear();
            params.put("ENTRADA_STOCK_ID",entradaStocks.get(0).getId());
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

            List<DetalleEntradaStock> detalleEntradaStock = detalleEntradaStockMapper.listByParameterMap(params);
            entradaStocks.get(0).setDetalleEntradaStock(detalleEntradaStock);

            if(entradaStocks.get(0).getEstado().equals(Constantes.COMPRA_ESTADO_ANULADO))
                entradaStocks.get(0).setEstadoStr(Constantes.COMPRA_ESTADO_ANULADO_STR);

            if(entradaStocks.get(0).getEstado().equals(Constantes.COMPRA_ESTADO_INICIADO))
                entradaStocks.get(0).setEstadoStr(Constantes.COMPRA_ESTADO_INICIADO_STR);

            if(entradaStocks.get(0).getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA))
                entradaStocks.get(0).setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA_STR);

            if(entradaStocks.get(0).getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL))
                entradaStocks.get(0).setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL_STR);

            if(entradaStocks.get(0).getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL))
                entradaStocks.get(0).setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL_STR);

            if(entradaStocks.get(0).getProveedor() != null && entradaStocks.get(0).getProveedor().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStocks.get(0).getProveedor().getTipoDocumento().getId());
                    entradaStocks.get(0).getProveedor().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if(entradaStocks.get(0).getFacturaProveedor() != null && entradaStocks.get(0).getFacturaProveedor().getTipoComprobante() != null){
                try {
                    TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(entradaStocks.get(0).getFacturaProveedor().getTipoComprobante().getId());
                    entradaStocks.get(0).getFacturaProveedor().setTipoComprobante(tipoComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            return entradaStocks.get(0);
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
            String errorValidacion = "Error de validación Método Eliminar Compra";

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
            String errorValidacion = "Error de validación Método Anular Compra";

            if (resultValidacion.get("errors") != null) {
                List<String> errors = (List<String>) resultValidacion.get("errors");
                if (errors.size() > 0)
                    errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
            }

            throw new ValidationServiceException(errorValidacion);
        }
    }
    @Override
    public Page<EntradaStock> listar(Pageable page, FiltroEntradaStock filtros) throws Exception {
        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getNumeroEntradaStock() != null && !filtros.getNumeroEntradaStock().trim().isEmpty())
            params.put("NUMERO", filtros.getNumeroEntradaStock());

        if(filtros.getId() != null && filtros.getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ID",filtros.getId());

        if(filtros.getFecha() != null)
            params.put("FECHA", filtros.getFecha());

        if(filtros.getFechaInicio() != null && filtros.getFechaFinal() != null){
            params.put("FECHA_INI", filtros.getFechaInicio());
            params.put("FECHA_FIN", filtros.getFechaFinal());
        }

        if(filtros.getEstado() != null)
            params.put("ESTADO", filtros.getEstado());

        if(filtros.getActualizado() != null)
            params.put("ACTUALIZADO", filtros.getActualizado());

        if(filtros.getFacturado() != null)
            params.put("FACTURADO", filtros.getFacturado());

        if(filtros.getIdProveedor() != null && filtros.getIdProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("PROVEEDOR_ID", filtros.getIdProveedor());

        if(filtros.getNombreProveedor() != null && !filtros.getNombreProveedor().trim().isEmpty())
            params.put("PRO_NOMBRE", "%"+filtros.getNombreProveedor()+"%");

        if(filtros.getDocumentoProveedor() != null && !filtros.getDocumentoProveedor().trim().isEmpty())
            params.put("PRO_DOCUMENTO", filtros.getDocumentoProveedor());

        if(filtros.getIdTipoDocumentoProveedor() != null)
            params.put("PRO_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoProveedor());

        if(filtros.getIdFacturaProveedor() != null && filtros.getIdFacturaProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("FACTURA_PROVEEDOR_ID", filtros.getIdFacturaProveedor());

        if(filtros.getSerieFacturaProveedor() != null && !filtros.getSerieFacturaProveedor().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieFacturaProveedor());

        if(filtros.getNumeroFacturaProveedor() != null && !filtros.getNumeroFacturaProveedor().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroFacturaProveedor());

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


        Long total = entradaStockMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<EntradaStock> entradaStocks = entradaStockMapper.listByParameterMap(params);

        entradaStocks.forEach((entradaStock) -> {
            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_ANULADO))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_ANULADO_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_INICIADO))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_INICIADO_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL_STR);

            if(entradaStock.getProveedor() != null && entradaStock.getProveedor().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStock.getProveedor().getTipoDocumento().getId());
                    entradaStock.getProveedor().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if(entradaStock.getFacturaProveedor() != null && entradaStock.getFacturaProveedor().getTipoComprobante() != null){
                try {
                    TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(entradaStock.getFacturaProveedor().getTipoComprobante().getId());
                    entradaStock.getFacturaProveedor().setTipoComprobante(tipoComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });


        return new PageImpl<>(entradaStocks, page, total);
    }

    @Override
    public Page<EntradaStock> listarPagado(Pageable page, FiltroEntradaStock filtros) throws Exception {
        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("ORDER_DESC", Constantes.CANTIDAD_UNIDAD);
        params.put("ESTADO_MAYOR", Constantes.COMPRA_ESTADO_INICIADO);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        if(filtros.getNumeroEntradaStock() != null && !filtros.getNumeroEntradaStock().trim().isEmpty())
            params.put("NUMERO", filtros.getNumeroEntradaStock());

        if(filtros.getId() != null && filtros.getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ID",filtros.getId());

        if(filtros.getFecha() != null)
            params.put("FECHA", filtros.getFecha());

        if(filtros.getFechaInicio() != null && filtros.getFechaFinal() != null){
            params.put("FECHA_INI", filtros.getFechaInicio());
            params.put("FECHA_FIN", filtros.getFechaFinal());
        }

        if(filtros.getEstado() != null)
            params.put("ESTADO", filtros.getEstado());

        if(filtros.getActualizado() != null)
            params.put("ACTUALIZADO", filtros.getActualizado());

        if(filtros.getFacturado() != null)
            params.put("FACTURADO", filtros.getFacturado());

        if(filtros.getIdProveedor() != null && filtros.getIdProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("PROVEEDOR_ID", filtros.getIdProveedor());

        if(filtros.getNombreProveedor() != null && !filtros.getNombreProveedor().trim().isEmpty())
            params.put("PRO_NOMBRE", "%"+filtros.getNombreProveedor()+"%");

        if(filtros.getDocumentoProveedor() != null && !filtros.getDocumentoProveedor().trim().isEmpty())
            params.put("PRO_DOCUMENTO", filtros.getDocumentoProveedor());

        if(filtros.getIdTipoDocumentoProveedor() != null)
            params.put("PRO_TIPO_DOCUMENTO_ID", filtros.getIdTipoDocumentoProveedor());

        if(filtros.getIdFacturaProveedor() != null && filtros.getIdFacturaProveedor().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("FACTURA_PROVEEDOR_ID", filtros.getIdFacturaProveedor());

        if(filtros.getSerieFacturaProveedor() != null && !filtros.getSerieFacturaProveedor().trim().isEmpty())
            params.put("CO_SERIE", filtros.getSerieFacturaProveedor());

        if(filtros.getNumeroFacturaProveedor() != null && !filtros.getNumeroFacturaProveedor().trim().isEmpty())
            params.put("CO_NUMERO", filtros.getNumeroFacturaProveedor());

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


        Long total = entradaStockMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<EntradaStock> entradaStocks = entradaStockMapper.listByParameterMap(params);

        entradaStocks.forEach((entradaStock) -> {
            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_ANULADO))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_ANULADO_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_INICIADO))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_INICIADO_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL_STR);

            if(entradaStock.getEstado().equals(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL))
                entradaStock.setEstadoStr(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL_STR);

            if(entradaStock.getProveedor() != null && entradaStock.getProveedor().getTipoDocumento() != null){
                try {
                    TipoDocumento tipoDocumento = tipoDocumentoDAO.listarPorId(entradaStock.getProveedor().getTipoDocumento().getId());
                    entradaStock.getProveedor().setTipoDocumento(tipoDocumento);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if(entradaStock.getFacturaProveedor() != null && entradaStock.getFacturaProveedor().getTipoComprobante() != null){
                try {
                    TipoComprobante tipoComprobante = tipoComprobanteDAO.listarPorId(entradaStock.getFacturaProveedor().getTipoComprobante().getId());
                    entradaStock.getFacturaProveedor().setTipoComprobante(tipoComprobante);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //ventas = ventas.stream().sorted((Comparator.comparing(Venta::getId))).collect(Collectors.toList());

        return new PageImpl<>(entradaStocks, page, total);
    }


    @Override
    public EntradaStock registrarDetalle(DetalleEntradaStock detalleEntradaStock) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        detalleEntradaStock.setCreatedAt(fechaActualTime);
        detalleEntradaStock.setUpdatedAd(fechaActualTime);

        //TODO: Temporal hasta incluir Oauth inicio
        detalleEntradaStock.setEmpresaId(1L);
        detalleEntradaStock.setUserId(2L);
        //user.setEmpresaId(1L);
        //user = userDAO.listarPorId(user.getId());
        //Todo: Temporal hasta incluir Oauth final

        detalleEntradaStock.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        detalleEntradaStock.setActivo(Constantes.REGISTRO_ACTIVO);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = false;
        EntradaStock entradaStock = this.listarPorId(detalleEntradaStock.getEntradaStockIdReference());

        validacion =this.validacionCompraDetalle(detalleEntradaStock, resultValidacion);

        if(validacion){
            detalleEntradaStock.setEntradaStock(entradaStock);
            BigDecimal precioTotalFix = detalleEntradaStock.getCostoTotal().setScale(2, RoundingMode.HALF_UP);
            detalleEntradaStock.setCostoTotal(precioTotalFix);

            //Get Anteriores registros mismo prod mismo lote misma unidad
            /*
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ENTRADA_STOCK_ID", entradaStock.getId());
            params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
            params.put("PRODUCTO_ID", detalleEntradaStock.getProducto().getId());
            params.put("ALMACEN_ID", detalleEntradaStock.getAlmacenId());
            params.put("CANTIDAD_REAL", detalleEntradaStock.getCantreal());

            if(detalleEntradaStock.getProducto().getActivoLotes().intValue() == Constantes.REGISTRO_ACTIVO.intValue())
                params.put("LOTE_ID", detalleEntradaStock.getLote().getId());

            List<DetalleEntradaStock> detalleEntradaStocks = detalleEntradaStockMapper.listByParameterMap(params);

            if(detalleEntradaStocks.isEmpty()){
                this.grabarRegistroDetalle(entradaStock, detalleEntradaStock);
                //Venta ventaRes = this.recalcularVenta(venta);
                return entradaStock;
            }


            this.grabarModificarDetalleAdd(entradaStock, detalleEntradaStock, detalleEntradaStocks.get(0));
            EntradaStock entradaStockRes = this.recalcularEntradaStock(entradaStock);
            return entradaStockRes;
            */
            this.grabarRegistroDetalle(entradaStock, detalleEntradaStock);
            //Venta ventaRes = this.recalcularVenta(venta);
            return entradaStock;
        }



        String errorValidacion = "Error de validación Agregar Productos a Compra de Bienes";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }



    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public EntradaStock grabarRegistroDetalle(EntradaStock entradaStock, DetalleEntradaStock detalleEntradaStock) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Lote loteBD = null;
        if(detalleEntradaStock.getLote() != null){
            detalleEntradaStock.getLote().setCantidad(detalleEntradaStock.getCantidad() * detalleEntradaStock.getCantreal());
            detalleEntradaStock.getLote().setMotivo(Constantes.MOTIVO_INGRESO_COMPRA);
            loteBD = loteService.registrarOnlyNuevoLote(detalleEntradaStock.getLote());
            detalleEntradaStock.setLote(loteBD);
        }


        detalleEntradaStock = detalleEntradaStockDAO.registrar(detalleEntradaStock);

        /*
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PRODUCTO_ID", detalleEntradaStock.getProducto().getId());
        params.put("ALMACEN_ID", detalleEntradaStock.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);


        if(detalleEntradaStock.getLote() != null)
            loteBD = loteDAO.listarPorId(detalleEntradaStock.getLote().getId());
        */

        //this.recalcularVenta(venta);
        //this.modificarStocks(Constantes.TIPO_ENTRADA_PRODUCTOS, stockG1.get(0), loteBD, detalleEntradaStock.getCantidad() * detalleEntradaStock.getCantreal());

        return entradaStock;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public EntradaStock grabarModificarDetalleAdd(EntradaStock entradaStock, DetalleEntradaStock detalleEntradaStock, DetalleEntradaStock detalleEntradaStockBD) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        BigDecimal costoTotal = detalleEntradaStock.getCosto().multiply(BigDecimal.valueOf(detalleEntradaStock.getCantidad() + detalleEntradaStockBD.getCantidad()));

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", detalleEntradaStockBD.getId());
        params.put("CANTIDAD", detalleEntradaStock.getCantidad() + detalleEntradaStockBD.getCantidad());
        params.put("COSTO_TOTAL", costoTotal);
        params.put("CANTREAL", detalleEntradaStock.getCantreal());

        int res = detalleEntradaStockMapper.updateByPrimaryKeySelective(params);

        //DetalleVenta d = detalleVentaDAO.registrar(detalleVenta);

        params.clear();
        params.put("PRODUCTO_ID", detalleEntradaStock.getProducto().getId());
        params.put("ALMACEN_ID", detalleEntradaStock.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        Lote loteBD = null;

        if(detalleEntradaStock.getLote() != null)
            loteBD = loteDAO.listarPorId(detalleEntradaStock.getLote().getId());

        //this.recalcularVenta(venta);
        this.modificarStocks(Constantes.TIPO_ENTRADA_PRODUCTOS, stockG1.get(0), loteBD, detalleEntradaStock.getCantidad() * detalleEntradaStock.getCantreal());

        return entradaStock;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public EntradaStock grabarModificarDetalle(EntradaStock entradaStock, DetalleEntradaStock detalleEntradaStock, DetalleEntradaStock detalleEntradaStockBD) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Lote loteBD = null;

        if(detalleEntradaStockBD.getLote() != null)
            loteBD = loteDAO.listarPorId(detalleEntradaStockBD.getLote().getId());


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PRODUCTO_ID", detalleEntradaStock.getProducto().getId());
        params.put("ALMACEN_ID", detalleEntradaStock.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);

        params.clear();
        params.put("ID", detalleEntradaStockBD.getId());
        params.put("CANTIDAD", detalleEntradaStock.getCantidad());

        params.put("COSTO", detalleEntradaStock.getCosto());
        params.put("COSTO_TOTAL", detalleEntradaStock.getCostoTotal());
        params.put("CANTREAL", detalleEntradaStock.getCantreal());

        if(detalleEntradaStock.getLote() != null)
            params.put("LOTE_ID", detalleEntradaStock.getLote().getId());

        int res = detalleEntradaStockMapper.updateByPrimaryKeySelective(params);

        //this.recalcularVenta(venta);
        //this.modificarOnlyLotes(Constantes.TIPO_ENTRADA_PRODUCTOS, stockG1.get(0), loteBD2, (detalleEntradaStock.getCantidad() * detalleEntradaStock.getCantreal()));
        //this.modificarOnlyLotes(Constantes.TIPO_RETIRO_PRODUCTOS, stockG1.get(0), loteBD1, (detalleEntradaStockBD.getCantidad() * detalleEntradaStockBD.getCantreal()));


        if(loteBD != null && loteBD.getId() != null && loteBD.getId() > 0){
            LocalDateTime fechaActualUpdated = LocalDateTime.now();

            loteBD.setNombre(detalleEntradaStock.getLote().getNombre());
            loteBD.setCantidad(detalleEntradaStock.getLote().getCantidad());
            loteBD.setFechaVencimiento(detalleEntradaStock.getLote().getFechaVencimiento());
            loteBD.setFechaIngreso(detalleEntradaStock.getLote().getFechaIngreso());
            loteBD.setActivoVencimiento(detalleEntradaStock.getLote().getActivoVencimiento());
            loteBD.setUpdatedAd(fechaActualUpdated);

            Lote loteEdited = loteDAO.modificar(loteBD);
        }

        return entradaStock;
    }

    private boolean validacionCompraDetalle(DetalleEntradaStock detalleEntradaStock, Map<String, Object> resultValidacion) throws Exception {

        LocalDate fechaActual = LocalDate.now();
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final
        //BigDecimal cantidadTotal = BigDecimal.valueOf(detalleEntradaStock.getCantidad() * detalleEntradaStock.getCantreal());

        if(detalleEntradaStock.getCantidad() == null || new BigDecimal(detalleEntradaStock.getCantidad()).compareTo(new BigDecimal(0)) <= 0){
            resultado = false;
            error = "Debe de Agregar una cantidad válida";
            errors.add(error);
        }

        if(detalleEntradaStock.getCosto() == null || detalleEntradaStock.getCosto().compareTo(new BigDecimal(0)) <= 0){
            resultado = false;
            error = "Debe de Enviar un costo válido";
            errors.add(error);
        }

        //Validaciones Lotes
        if(detalleEntradaStock.getProducto().getActivoLotes().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            if(detalleEntradaStock.getLote() == null ){
                resultado = false;
                error = "Debe de remitir el lote a agregar";
                errors.add(error);
            } else {
                detalleEntradaStock.getLote().setFechaIngreso(fechaActual);
                if(detalleEntradaStock.getLote().getNombre() == null || detalleEntradaStock.getLote().getNombre().trim().isEmpty()){
                    resultado = false;
                    error = "Debe de ingresar el nombre del Lote para registrarlo";
                    errors.add(error);
                }

                if(detalleEntradaStock.getLote().getFechaIngreso() == null ){
                    resultado = false;
                    error = "Debe de ingresar la fecha de ingreso del Lote para registrarlo";
                    errors.add(error);
                }
            }

        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    private boolean validacionDeleteCompraDetalle(DetalleEntradaStock detalleEntradaStock, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public EntradaStock agregarProducto(AgregarProductoBean addProductoEntradaStock) throws Exception {
        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final


        Map<String, Object> resultValidacion = new HashMap<String, Object>();
        boolean validacion = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;
        String errorValidacion = "Error de validación Agregar Producto a Compra de Bienes";

        EntradaStock entradaStock = this.listarPorId(addProductoEntradaStock.getIdEntradaStock());
        if(entradaStock == null){
            validacion = false;
            error = "No se envió una Compra Válida";
            errors.add(error);
        }

        if(addProductoEntradaStock.getCodigoUnidad() != null)
            addProductoEntradaStock.setCodigoUnidad(addProductoEntradaStock.getCodigoUnidad().trim());

        if(addProductoEntradaStock.getCodigoUnidad() == null || addProductoEntradaStock.getCodigoUnidad().isEmpty()){
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
        params.put("ALMACEN_ID",entradaStock.getAlmacen().getId());
        params.put("CODIGO_UNIDAD", addProductoEntradaStock.getCodigoUnidad());
        List<ProductosVentaDTO> productosVenta = productoMapper.listByParameterMapProductoVenta(params);
        ProductosVentaDTO productoVenta = productosVenta.get(0);

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



        if(!validacion){
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());

            throw new ValidationServiceException(errorValidacion);
        }


        DetalleEntradaStock detalleEntradaStock = new DetalleEntradaStock();
        detalleEntradaStock.setEntradaStock(entradaStock);
        detalleEntradaStock.setEntradaStockIdReference(entradaStock.getId());
        detalleEntradaStock.setProducto(productoVenta.getProducto());
        detalleEntradaStock.setCantidad(Constantes.CANTIDAD_UNIDAD_DOUBLE);
        detalleEntradaStock.setCosto(productoVenta.getDetalleUnidadProducto().getCostoCompra());
        detalleEntradaStock.setAlmacenId(entradaStock.getAlmacen().getId());
        detalleEntradaStock.setCantreal(productoVenta.getDetalleUnidadProducto().getUnidad().getCantidad());
        detalleEntradaStock.setUnidad(productoVenta.getDetalleUnidadProducto().getUnidad().getNombre());
        detalleEntradaStock.setCostoTotal(productoVenta.getDetalleUnidadProducto().getPrecio());
        detalleEntradaStock.setLote(addProductoEntradaStock.getLote());

        return this.registrarDetalle(detalleEntradaStock);
    }

    @Override
    public EntradaStock eliminarDetalle(DetalleEntradaStock detalleEntradaStock) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        detalleEntradaStock.setUpdatedAd(fechaActualTime);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = false;
        EntradaStock entradaStock = this.listarPorId(detalleEntradaStock.getEntradaStockIdReference());

        validacion =this.validacionDeleteCompraDetalle(detalleEntradaStock, resultValidacion);

        if(validacion){
            detalleEntradaStock.setEntradaStock(entradaStock);
            this.grabarDeleteDetalle(entradaStock, detalleEntradaStock);
            EntradaStock entradaStockRes = this.recalcularEntradaStock(entradaStock, detalleEntradaStock.getId());
            return entradaStockRes;
        }



        String errorValidacion = "Error de validación Agregar Productos a Compra de Bienes";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public EntradaStock modificarDetalle(DetalleEntradaStock detalleEntradaStock) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        detalleEntradaStock.setUpdatedAd(fechaActualTime);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = false;
        EntradaStock entradaStock = this.listarPorId(detalleEntradaStock.getEntradaStockIdReference());

        validacion =this.validacionModificacionCompraDetalle(detalleEntradaStock, resultValidacion);

        if(validacion){
            DetalleEntradaStock detalleEntradaStockBD = (DetalleEntradaStock) resultValidacion.get("detalleEntradaStockBD");
            detalleEntradaStock.setEntradaStock(entradaStock);
            BigDecimal precioTotalFix = detalleEntradaStock.getCostoTotal().setScale(2, RoundingMode.HALF_UP);
            detalleEntradaStock.setCostoTotal(precioTotalFix);

            return this.grabarModificarDetalle(entradaStock, detalleEntradaStock, detalleEntradaStockBD);
            //EntradaStock entradaStockRes = this.recalcularEntradaStock(entradaStock);
            //return entradaStockRes;
        }



        String errorValidacion = "Error de validación Agregar Productos a Compra de Bienes";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    private boolean validacionModificacionCompraDetalle(DetalleEntradaStock detalleEntradaStock, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final
        DetalleEntradaStock detalleEntradaStockBD = detalleEntradaStockDAO.listarPorId(detalleEntradaStock.getId());

        if(detalleEntradaStock.getCantidad() == null || new BigDecimal(detalleEntradaStock.getCantidad()).compareTo(new BigDecimal(0)) <= 0){
            resultado = false;
            error = "Debe de Agregar una cantidad válida";
            errors.add(error);
        }

        if(detalleEntradaStock.getCosto() == null || detalleEntradaStock.getCosto().compareTo(new BigDecimal(0)) <= 0){
            resultado = false;
            error = "Debe de Enviar un costo válido";
            errors.add(error);
        }

        //Validaciones Lotes
        if(detalleEntradaStock.getProducto().getActivoLotes().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){
            if(detalleEntradaStock.getLote() == null ){
                resultado = false;
                error = "Debe de remitir el lote a agregar";
                errors.add(error);
            } else {
                LocalDate fechaActual = LocalDate.now();
                detalleEntradaStock.getLote().setFechaIngreso(fechaActual);
                if(detalleEntradaStock.getLote().getNombre() == null || detalleEntradaStock.getLote().getNombre().trim().isEmpty()){
                    resultado = false;
                    error = "Debe de ingresar el nombre del Lote para modificarlo";
                    errors.add(error);
                }

                if(detalleEntradaStock.getLote().getFechaIngreso() == null ){
                    resultado = false;
                    error = "Debe de ingresar la fecha de ingreso del Lote para modificarlo";
                    errors.add(error);
                }
            }

        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);
        resultValidacion.put("detalleEntradaStockBD",detalleEntradaStockBD);

        return resultado;
    }

    @Override
    public EntradaStock resetEntradaStock(EntradaStock entradaStockRemitida) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = false;
        EntradaStock entradaStock = this.listarPorId(entradaStockRemitida.getId());

        entradaStock.setUpdatedAd(fechaActualTime);

        this.grabarResetCompra(entradaStock);
        EntradaStock entradaStockRes = this.recalcularEntradaStockReset(entradaStock);
        return entradaStockRes;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public EntradaStock grabarResetCompra(EntradaStock entradaStock) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ENTRADA_STOCK_ID",entradaStock.getId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<DetalleEntradaStock> detalleEntradaStocks = detalleEntradaStockMapper.listByParameterMap(params);
        for (DetalleEntradaStock de: detalleEntradaStocks) {
            params.clear();
            params.put("PRODUCTO_ID", de.getProducto().getId());
            params.put("ALMACEN_ID", de.getAlmacenId());
            params.put("EMPRESA_ID", EmpresaId);

            List<Stock> stockG1 = stockMapper.listByParameterMap(params);
            Lote loteBD = null;

            if(de.getLote() != null && de.getLote().getId() != null)
                loteBD = loteDAO.listarPorId(de.getLote().getId());


            this.modificarStocks(Constantes.TIPO_RETIRO_PRODUCTOS, stockG1.get(0), loteBD, de.getCantidad() * de.getCantreal());
            detalleEntradaStockDAO.eliminar(de.getId());
        }

        return entradaStock;
    }

    @Override
    public PagoProveedor cobrarEntradaStock(PagoProveedor pagoProveedor) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = LocalDate.now();
        EntradaStock entradaStockBD = this.listarPorId(pagoProveedor.getEntradaStock().getId());

        entradaStockBD.setUpdatedAd(fechaActualTime);
        User user = new User();

        //TODO: Temporal hasta incluir Oauth inicio
        user.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final
        entradaStockBD.setUser(user);

        //pagoProveedor.setEntradaStock(entradaStock);
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
        pagoProveedor.setFecha(fechaActual);




        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistroPago(pagoProveedor, entradaStockBD, resultValidacion);

        if(validacion){
            TipoComprobante tipoComprobante = new TipoComprobante();
            tipoComprobante = tipoComprobanteDAO.listarPorId(pagoProveedor.getTipoComprobanteId());

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


            return this.grabarRegistroPago(pagoProveedor, entradaStockBD, tipoComprobante);
        }


        String errorValidacion = "Error de validación Método Pagar Compra";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public EntradaStock recalcularEntradaStockPublic(EntradaStock entradaStock) throws Exception {
        return this.recalcularEntradaStock(entradaStock);
    }

    @Override
    public EntradaStock generarComprobante(EntradaStock entradaStock) throws Exception {
        return null;
    }



    @Override
    public Page<PagoProveedor> listarPagos(Pageable page, Long id) throws Exception {
        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);

        params.put("ENTRADA_STOCK_ID", id);
        params.put("MAYOR_MIN_IMPORTE", Constantes.CANTIDAD_ZERO);


        Long total = pagoProveedorMapper.getTotalElements(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<PagoProveedor> pagoProveedors = pagoProveedorMapper.listByParameterMap(params);

        return new PageImpl<>(pagoProveedors, page, total);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public EntradaStock grabarRegistro(EntradaStock e) throws Exception {
        EntradaStock entradaStock = entradaStockDAO.registrar(e);
        return entradaStock;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private PagoProveedor grabarRegistroPago(PagoProveedor pagoProveedor, EntradaStock entradaStockBD, TipoComprobante tipoComprobante) throws Exception {

        entradaStockBD.setFacturado( pagoProveedor.getEntradaStock().getFacturado());
        entradaStockBD.setActualizado( pagoProveedor.getEntradaStock().getActualizado());
        entradaStockBD.setFacturaProveedor( pagoProveedor.getEntradaStock().getFacturaProveedor());

        pagoProveedor.setEntradaStock(entradaStockBD);

        //FIXME: Temporal hasta incluir currencys
        pagoProveedor.setMontoReal(pagoProveedor.getMontoPago());
        pagoProveedor.setTipoPago(Constantes.TIPO_PAGO_SOLES);

        if(pagoProveedor.getMontoReal().compareTo(new BigDecimal(0)) <= 0)
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_NO_COBRADA);

        if(pagoProveedor.getMontoReal().compareTo(new BigDecimal(0)) > 0 && pagoProveedor.getMontoReal().compareTo(pagoProveedor.getEntradaStock().getTotalMonto()) < 0)
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_PARCIAL);

        if(pagoProveedor.getMontoReal().compareTo(new BigDecimal(0)) > 0 && pagoProveedor.getMontoReal().compareTo(pagoProveedor.getEntradaStock().getTotalMonto()) >= 0){
            pagoProveedor.getEntradaStock().setEstado(Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL);
            pagoProveedor.setMontoReal(pagoProveedor.getEntradaStock().getTotalMonto());
            pagoProveedor.setMontoPago(pagoProveedor.getEntradaStock().getTotalMonto());
        }

        PagoProveedor pagoProveedorReg = pagoProveedorDAO.registrar(pagoProveedor);


        Map<String, Object> params = new HashMap<String, Object>();

        if(pagoProveedor.getEntradaStock().getFacturado().compareTo(Constantes.COMPRA_SI_FACTURADO) == 0 && pagoProveedor.getEntradaStock().getFacturaProveedor() != null){
            FacturaProveedor comprobante = new FacturaProveedor();
            comprobante.setTipoComprobante(pagoProveedor.getEntradaStock().getFacturaProveedor().getTipoComprobante());
            comprobante.setSerie(pagoProveedor.getEntradaStock().getFacturaProveedor().getSerie());
            comprobante.setNumero(pagoProveedor.getEntradaStock().getFacturaProveedor().getNumero());
            comprobante.setFecha(pagoProveedor.getEntradaStock().getFacturaProveedor().getFecha());
            comprobante.setEstado(Constantes.COMPROBANTE_ESTADO_CREADO);
            comprobante.setObservaciones(pagoProveedor.getEntradaStock().getFacturaProveedor().getObservaciones());
            comprobante.setUserId(pagoProveedor.getUserId());
            comprobante.setAlmacenId(pagoProveedor.getEntradaStock().getAlmacen().getId());
            comprobante.setEmpresaId(pagoProveedor.getEmpresaId());
            comprobante.setActivo(Constantes.REGISTRO_ACTIVO);
            comprobante.setBorrado(Constantes.REGISTRO_NO_BORRADO);
            comprobante.setCreatedAt(pagoProveedor.getCreatedAt());
            comprobante.setUpdatedAd(pagoProveedor.getUpdatedAd());

            comprobante = facturaProveedorDAO.registrar(comprobante);

            pagoProveedorReg.getEntradaStock().setFacturaProveedor(comprobante);

            params.put("FACTURA_PROVEEDOR_ID", comprobante.getId());
        }

        params.put("ID", pagoProveedor.getEntradaStock().getId());
        params.put("ESTADO", pagoProveedor.getEntradaStock().getEstado());

        params.put("FACTURADO", pagoProveedor.getEntradaStock().getFacturado());
        params.put("ACTUALIZADO", pagoProveedor.getEntradaStock().getActualizado());

        params.put("UPDATED_AT", pagoProveedor.getEntradaStock().getUpdatedAd());
        entradaStockMapper.updateByPrimaryKeySelective(params);

        if(pagoProveedor.getEntradaStock().getActualizado().compareTo(Constantes.COMPRA_SI_ACTUALIZADO) == 0){
            //Ingresar Cantidades a Stock
            params.clear();
            params.put("ENTRADA_STOCK_ID", pagoProveedor.getEntradaStock().getId());
            params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

            List<DetalleEntradaStock> detalleEntradaStocks = detalleEntradaStockMapper.listByParameterMap(params);
            for (DetalleEntradaStock de: detalleEntradaStocks) {
                params.clear();
                params.put("PRODUCTO_ID", de.getProducto().getId());
                params.put("ALMACEN_ID", de.getAlmacenId());
                params.put("EMPRESA_ID", de.getEmpresaId());

                List<Stock> stockG1 = stockMapper.listByParameterMap(params);
                Lote loteBD = null;

                if(de.getLote() != null && de.getLote().getId() != null)
                    loteBD = loteDAO.listarPorId(de.getLote().getId());

                this.modificarStocksOnlyStocks(Constantes.TIPO_ENTRADA_PRODUCTOS, stockG1.get(0), loteBD, de.getCantidad() * de.getCantreal());
            }
        }

        return pagoProveedorReg;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(EntradaStock entradaStock) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", entradaStock.getId());

        if(entradaStock.getFecha() != null)
            params.put("FECHA", entradaStock.getFecha());

        if(entradaStock.getHora() != null)
            params.put("HORA", entradaStock.getHora());

        if(entradaStock.getProveedor() != null && entradaStock.getProveedor().getId() != null)
            params.put("PROVEEDOR_ID", entradaStock.getProveedor().getId());

        if(entradaStock.getOrdenCompraId() != null && entradaStock.getOrdenCompraId() != null)
            params.put("ORDEN_COMPRA_ID", entradaStock.getOrdenCompraId());

        if(entradaStock.getFacturado() != null)
            params.put("FACTURADO", entradaStock.getFacturado());

        if(entradaStock.getActualizado() != null)
            params.put("ACTUALIZADO", entradaStock.getActualizado());

        if(entradaStock.getEstado() != null)
            params.put("ESTADO", entradaStock.getEstado());

        if(entradaStock.getTotalMonto() != null)
            params.put("TOTAL_MONTO", entradaStock.getTotalMonto());

        if(entradaStock.getFacturaProveedor() != null)
            params.put("FACTURA_PROVEEDOR_ID", entradaStock.getFacturaProveedor());

        if(entradaStock.getUser() != null && entradaStock.getUser().getId() != null)
            params.put("USER_ID", entradaStock.getUser().getId());

        if(entradaStock.getNumero() != null)
            params.put("NUMERO", entradaStock.getNumero());

        params.put("UPDATED_AT", entradaStock.getUpdatedAd());

        return entradaStockMapper.updateByPrimaryKeySelective(params);
    }

    @Override
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void grabarEliminar(Long id) throws Exception {
        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        //Devolver Stocks
        this.devolverStockCompra(id);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("UPDATED_AT",fechaUpdate);

        int res= entradaStockMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo eliminar la compra indicada, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private void grabarAnular(Long id) throws Exception {
        LocalDateTime fechaActualDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaUpdate = fechaActualDateTime.format(formatter);

        //Devolver Stocks
        this.devolverStockCompra(id);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("ESTADO",Constantes.COMPRA_ESTADO_ANULADO);
        params.put("UPDATED_AT",fechaUpdate);

        int res= entradaStockMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new RuntimeException("No se pudo anular la compra indicada, por favor probar nuevamente o comunicarse con un Administrador del Sistema");
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void devolverStockCompra(Long compraId) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ENTRADA_STOCK_ID", compraId);
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<DetalleEntradaStock> detalleEntradaStock = detalleEntradaStockMapper.listByParameterMap(params);
        for (DetalleEntradaStock de: detalleEntradaStock) {
            params.clear();
            params.put("PRODUCTO_ID", de.getProducto().getId());
            params.put("ALMACEN_ID", de.getAlmacenId());
            params.put("EMPRESA_ID", EmpresaId);

            List<Stock> stockG1 = stockMapper.listByParameterMap(params);
            Lote loteBD = null;

            if(de.getLote() != null && de.getLote().getId() != null)
                loteBD = loteDAO.listarPorId(de.getLote().getId());


            this.modificarStocks(Constantes.TIPO_RETIRO_PRODUCTOS, stockG1.get(0), loteBD, de.getCantidad() * de.getCantreal());
            //detalleVentaDAO.eliminar(dv.getId());
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public EntradaStock grabarDeleteDetalle(EntradaStock entradaStock, DetalleEntradaStock detalleEntradaStock) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PRODUCTO_ID", detalleEntradaStock.getProducto().getId());
        params.put("ALMACEN_ID", detalleEntradaStock.getAlmacenId());
        params.put("EMPRESA_ID", EmpresaId);

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        Lote loteBD = null;

        if(detalleEntradaStock.getLote() != null && detalleEntradaStock.getLote().getId() != null)
            loteBD = loteDAO.listarPorId(detalleEntradaStock.getLote().getId());


        //this.modificarStocks(Constantes.TIPO_RETIRO_PRODUCTOS, stockG1.get(0), loteBD, detalleEntradaStock.getCantidad() * detalleEntradaStock.getCantreal());
        detalleEntradaStockDAO.eliminar(detalleEntradaStock.getId());
        if(loteBD != null)
            loteService.eliminarOnlyLote(loteBD.getId());
        //this.recalcularVenta(venta);

        return entradaStock;
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

    private void modificarOnlyLotes(Integer tipoMovimiento, Lote loteBD, Double cantidad) throws Exception {

        //Modificaion Stock
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        LocalDateTime fechaActualTime = LocalDateTime.now();

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

    private void modificarStocksOnlyStocks(Integer tipoMovimiento, Stock stockModificar, Lote loteBD, Double cantidad) throws Exception {

        //Modificaion Stock
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        LocalDateTime fechaActualTime = LocalDateTime.now();


        //Activacion de Lote
        if(loteBD != null && loteBD.getId() != null && loteBD.getId() > 0) {
            loteBD.setActivo(Constantes.REGISTRO_ACTIVO);
            loteBD.setUpdatedAd(fechaActualTime);
            loteDAO.modificar(loteBD);
        }

        if(tipoMovimiento.equals(Constantes.TIPO_ENTRADA_PRODUCTOS))
            stockModificar.setCantidad(stockModificar.getCantidad() + cantidad);

        if(tipoMovimiento.equals(Constantes.TIPO_RETIRO_PRODUCTOS))
            stockModificar.setCantidad(stockModificar.getCantidad() - cantidad);

        stockModificar.setUpdatedAd(fechaActualTime);
        stockDAO.modificar(stockModificar);

    }

    @Override
    public boolean validacionRegistro(EntradaStock entradaStock, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(entradaStock.getAlmacen() == null || ( entradaStock.getAlmacen().getId() == null )){
            resultado = false;
            error = "Debe de remitir correctamente el Almacen";
            errors.add(error);
        } else {
            Almacen almacen = almacenService.listarPorId(entradaStock.getAlmacen().getId());
            entradaStock.setAlmacen(almacen);

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
    public boolean validacionModificado(EntradaStock entradaStock, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(entradaStock.getId() == null || ( entradaStock.getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el ID de la Compra";
            errors.add(error);
        }

        if(entradaStock.getFecha() == null){
            resultado = false;
            error = "Debe de remitir correctamente la Fecha de la Compra";
            errors.add(error);
        }

        if(entradaStock.getHora() == null){
            resultado = false;
            error = "Debe de remitir correctamente la Hora de la Compra";
            errors.add(error);
        }

        if(entradaStock.getTotalMonto() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Monto de la Compra";
            errors.add(error);
        }

        if(entradaStock.getEstado() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Estado de la Compra";
            errors.add(error);
        }

        if(entradaStock.getFacturado() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Estado del Pago de la Compra";
            errors.add(error);
        }

        if(entradaStock.getActualizado() == null){
            resultado = false;
            error = "Debe de remitir correctamente el Estado de Actualización de la Compra";
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

        EntradaStock entradaStock = this.listarPorId(id);

        if(entradaStock.getEstado().intValue() == Constantes.COMPRA_ESTADO_ANULADO){
            resultado = false;
            error = "La Compra remitida se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
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

        EntradaStock entradaStock = this.listarPorId(id);

        if(entradaStock.getEstado().intValue() == Constantes.COMPRA_ESTADO_ANULADO){
            resultado = false;
            error = "La Compra remitida ya se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(entradaStock.getEstado().intValue() == Constantes.COMPRA_ESTADO_ANULADO){
            resultado = false;
            error = "No se puede Anular una compra que aun no fue pagada y que no se ha generado ningún comprobante";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }













    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public int grabarRectificarProveedor(EntradaStock entradaStock) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", entradaStock.getId());
        params.put("PROVEEDOR_ID", entradaStock.getProveedor().getId());
        params.put("USER_ID", entradaStock.getUser().getId());
        params.put("UPDATED_AT", entradaStock.getUpdatedAd());

        return entradaStockMapper.updateByPrimaryKeySelective(params);
    }
    private boolean validacionModificadoProveedor(EntradaStock entradaStock, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(entradaStock.getId() == null || ( entradaStock.getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el ID de la Compra";
            errors.add(error);
        }

        if(entradaStock.getProveedor() == null || entradaStock.getProveedor().getId() == null || ( entradaStock.getProveedor().getId().compareTo(Constantes.CANTIDAD_ZERO_LONG) <= 0)){
            resultado = false;
            error = "Debe de remitir correctamente el Proveedor de la Compra";
            errors.add(error);
        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private EntradaStock recalcularEntradaStock(EntradaStock entradaStock) throws Exception {
        return  this.recalcularEntradaStock(entradaStock, null);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private EntradaStock recalcularEntradaStock(EntradaStock entradaStock, Long idRemove) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ENTRADA_STOCK_ID",entradaStock.getId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        if(idRemove != null)
            params.put("NO_ID",idRemove);

        List<DetalleEntradaStock> detalleEntradaStocks = detalleEntradaStockMapper.listByParameterMap(params);

        BigDecimal totalCompra = new BigDecimal(0);

        for (DetalleEntradaStock de: detalleEntradaStocks) {
            totalCompra = totalCompra.add(de.getCostoTotal());
        }


        entradaStock.setTotalMonto(totalCompra);
        entradaStock.setDetalleEntradaStock(detalleEntradaStocks);

        //Update Compra

        params.clear();
        params.put("ID", entradaStock.getId());
        params.put("TOTAL_MONTO", entradaStock.getTotalMonto());
        params.put("USER_ID", entradaStock.getUser().getId());
        params.put("UPDATED_AT", entradaStock.getUpdatedAd());

        int resultado = entradaStockMapper.updateByPrimaryKeySelective(params);

        return entradaStock;

    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    private EntradaStock recalcularEntradaStockReset(EntradaStock entradaStock) throws Exception {

        BigDecimal totalCompra = new BigDecimal(0);

        entradaStock.setTotalMonto(totalCompra);
        entradaStock.setDetalleEntradaStock(new ArrayList<>());
        entradaStock.setProveedor(null);


        //Update Compra

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID", entradaStock.getId());
        params.put("TOTAL_MONTO", entradaStock.getTotalMonto());
        params.put("USER_ID", entradaStock.getUser().getId());
        params.put("UPDATED_AT", entradaStock.getUpdatedAd());
        params.put("PROVEEDOR_ID_NULLABLE", Constantes.CANTIDAD_UNIDAD);

        int resultado = entradaStockMapper.updateByPrimaryKeySelective(params);

        return entradaStock;

    }

    private Long GetSequence(FiltroEntradaStock filtros) throws Exception {

        //TODO: Temporal hasta incluir Oauth inicio
        Long EmpresaId = 1L;
        //Todo: Temporal hasta incluir Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0)
            params.put("ALMACEN_ID",filtros.getAlmacenId());

        long sequence = entradaStockMapper.getTotalElements(params);
        sequence++;

        return sequence;
    }

    private boolean validacionRegistroPago(PagoProveedor pagoProveedor,  EntradaStock entradaStockBD, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(pagoProveedor.getEntradaStock() == null){
            resultado = false;
            error = "La Compra remitida no es válida";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(entradaStockBD.getProveedor() == null){
            resultado = false;
            error = "Debe de seleccionar un Proveedor";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ENTRADA_STOCK_ID", entradaStockBD.getId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<DetalleEntradaStock> detalleEntradaStocks = detalleEntradaStockMapper.listByParameterMap(params);

        if(detalleEntradaStocks == null || detalleEntradaStocks.isEmpty()){
            resultado = false;
            error = "Debe de agregar Productos a la Compra";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(entradaStockBD.getEstado().intValue() == Constantes.COMPRA_ESTADO_ANULADO){
            resultado = false;
            error = "La Compra remitida se encuentra anulada";
            errors.add(error);

            resultValidacion.put("errors",errors);
            resultValidacion.put("warnings",warnings);

            return resultado;
        }

        if(entradaStockBD.getEstado().intValue() == Constantes.COMPRA_ESTADO_COMPRA_COBRADA_TOTAL){
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

        if(pagoProveedor.getEntradaStock().getActualizado() == null
                || (pagoProveedor.getEntradaStock().getActualizado().compareTo(Constantes.COMPRA_NO_ACTUALIZADO) != 0 &&
                    pagoProveedor.getEntradaStock().getActualizado().compareTo(Constantes.COMPRA_SI_ACTUALIZADO) != 0
        )){
            resultado = false;
            error = "Debe de remitir un valor adecuado de Compra Actualizada";
            errors.add(error);
        }

        if(pagoProveedor.getEntradaStock().getFacturado() == null
                || (pagoProveedor.getEntradaStock().getFacturado().compareTo(Constantes.COMPRA_NO_FACTURADO) != 0 &&
                    pagoProveedor.getEntradaStock().getFacturado().compareTo(Constantes.COMPRA_SI_FACTURADO) != 0
        )){
            resultado = false;
            error = "Debe de remitir un valor adecuado de Compra Facturada";
            errors.add(error);
        }

        if(pagoProveedor.getEntradaStock().getFacturado().compareTo(Constantes.COMPRA_SI_FACTURADO) == 0){
            if(pagoProveedor.getEntradaStock().getFacturaProveedor() == null){
                resultado = false;
                error = "Debe de remitir un Comprobante de Compra";
                errors.add(error);
            }

            if(pagoProveedor.getEntradaStock().getFacturaProveedor().getTipoComprobante() == null){
                resultado = false;
                error = "Debe de remitir un Tipo de Comprobante de Compra";
                errors.add(error);
            }

            if(pagoProveedor.getEntradaStock().getFacturaProveedor().getTipoComprobante().getId() == null){
                resultado = false;
                error = "Debe de remitir un Tipo de Comprobante de Compra Válido";
                errors.add(error);
            }

            if(pagoProveedor.getEntradaStock().getFacturaProveedor().getSerie() == null || pagoProveedor.getEntradaStock().getFacturaProveedor().getSerie().trim().isEmpty()){
                resultado = false;
                error = "Debe de remitir una Serie de Comprobante de Compra Válida";
                errors.add(error);
            }

            if(pagoProveedor.getEntradaStock().getFacturaProveedor().getNumero() == null || pagoProveedor.getEntradaStock().getFacturaProveedor().getNumero().trim().isEmpty()){
                resultado = false;
                error = "Debe de remitir un Número de Comprobante de Compra Válido";
                errors.add(error);
            }

            if(pagoProveedor.getEntradaStock().getFacturaProveedor().getFecha() == null){
                resultado = false;
                error = "Debe de remitir una Fecha de Comprobante de Compra Válida";
                errors.add(error);
            }

            /*
            if(pagoProveedor.getEntradaStock().getFacturaProveedor().getTipoComprobante().getId() != null){
                TipoComprobante tipoComprobante = new TipoComprobante();
                tipoComprobante = tipoComprobanteDAO.listarPorId(pagoProveedor.getEntradaStock().getFacturaProveedor().getTipoComprobante().getId());

                if(tipoComprobante.getPrefix().equals(Constantes.COMPROBANTE_PREFIJO_FACTURA)){
                    if(pagoProveedor.getEntradaStock().getProveedor().getTipoDocumento().getKey().equals(Constantes.COMPROBANTE_TIPO_OTROS_DOCUMENTOS)){
                        resultado = false;
                        error = "No se puede Emitir Factura al tipo de Proveedor con tipo de Documento Otros Documentos";
                        errors.add(error);
                    }
                    if(pagoProveedor.getEntradaStock().getProveedor().getTipoDocumento().getKey().equals(Constantes.COMPROBANTE_TIPO_DOCUMENTO_DNI)){
                        resultado = false;
                        error = "No se puede Emitir Factura al tipo de Proveedor con tipo de Documento DNI";
                        errors.add(error);
                    }
                    if(pagoProveedor.getEntradaStock().getProveedor().getTipoDocumento().getKey().equals(Constantes.COMPROBANTE_TIPO_DOCUMENTO_PARTIDA_NACIMIENTO)){
                        resultado = false;
                        error = "No se puede Emitir Factura al tipo de Proveedor con tipo de Documento Partida de Nacimiento";
                        errors.add(error);
                    }
                }
            } */
        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}

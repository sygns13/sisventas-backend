package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.*;
import com.bcs.ventas.dao.mappers.AlmacenMapper;
import com.bcs.ventas.dao.mappers.RetiroEntradaProductoMapper;
import com.bcs.ventas.dao.mappers.StockMapper;
import com.bcs.ventas.dao.mappers.UnidadMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.dto.ProductoVencidoDTO;
import com.bcs.ventas.model.dto.RetiroEntradaProductoDTO;
import com.bcs.ventas.model.dto.SalidasIngresosProductosDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.RetiroEntradaProductoService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class RetiroEntradaProductoServiceImpl implements RetiroEntradaProductoService {

    @Autowired
    private AlmacenMapper almacenMapper;

    @Autowired
    private UnidadDAO unidadDAO;

    @Autowired
    private DetalleUnidadProductoDAO detalleUnidadProductoDAO;

    @Autowired
    private RetiroEntradaProductoDAO retiroEntradaProductoDAO;

    @Autowired
    private RetiroEntradaProductoMapper retiroEntradaProductoMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private LoteDAO loteDAO;

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private UnidadMapper unidadMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;


    @Override
    public Page<RetiroEntradaProductoDTO> listar(Pageable pageable, String buscar) throws Exception {
        return null;
    }

    @Override
    public List<Almacen> getAlmacens(Long idEmpresa) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", idEmpresa);
        return almacenMapper.listByParameterMapOrderId(params);
    }

    @Override
    public Page<RetiroEntradaProductoDTO> getMovimientosLibresProductos(Pageable page, FiltroGeneral filtros) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        // params.put("BUSCAR","%"+buscar+"%");

        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", EmpresaId);
        params.put("CANTIDAD", Constantes.CANTIDAD_UNIDAD_INTEGER);


        List<Unidad> unidadG1 = unidadMapper.listByParameterMap(params);
        params.clear();

        LocalDate fechaActual = LocalDate.now();
        params.put("FECHA_ACTUAL", fechaActual.toString());

        if(unidadG1.size() > 0 && unidadG1.get(0) != null && unidadG1.get(0).getId() != null){
            params.put("UNIDAD_ID", unidadG1.get(0).getId());
        }

        if(filtros.getAlmacenId() != null && filtros.getAlmacenId().compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",filtros.getAlmacenId());
        }

        if(filtros.getPalabraClave() != null && !filtros.getPalabraClave().isEmpty()){
            params.put("BUSCAR","%"+filtros.getPalabraClave()+"%");
        }

        if(filtros.getTipo() != null){
            params.put("TIPO",filtros.getTipo());
        }

        if(filtros.getFechaInicio() != null){
            params.put("FECHA_INICIO", filtros.getFechaInicio().toString());
        }

        if(filtros.getFechaFinal() != null){
            params.put("FECHA_FINAL", filtros.getFechaFinal().toString());
        }


        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        Long total = retiroEntradaProductoMapper.getTotalElementsMovimientosProductos(params);
        Long totalPages = (long) Math.ceil( ((double)total) / page.getPageSize());
        Long offset = (long) page.getPageSize() *(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<RetiroEntradaProductoDTO> movimientosProductos = retiroEntradaProductoMapper.listByParameterMapMovientosProductos(params);

        return new PageImpl<>(movimientosProductos, page, total);

    }


    @Override
    public RetiroEntradaProducto registrar(RetiroEntradaProducto retiroEntradaProducto) throws Exception {

        LocalDateTime fechaActual = LocalDateTime.now();
        retiroEntradaProducto.setCreatedAt(fechaActual);
        retiroEntradaProducto.setUpdatedAd(fechaActual);

        //Oauth inicio
        retiroEntradaProducto.setEmpresaId(claimsAuthorization.getEmpresaId());
        retiroEntradaProducto.setUserId(claimsAuthorization.getUserId());
        //Oauth final

        retiroEntradaProducto.setActivo(Constantes.REGISTRO_ACTIVO);
        retiroEntradaProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);

        if(retiroEntradaProducto.getMotivo() == null)    retiroEntradaProducto.setMotivo("");
        retiroEntradaProducto.setMotivo(retiroEntradaProducto.getMotivo().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();


        Map<String, Object> params = new HashMap<String, Object>();

        params.put("PRODUCTO_ID",retiroEntradaProducto.getProductoId());
        params.put("ALMACEN_ID",retiroEntradaProducto.getAlmacenId());
        params.put("EMPRESA_ID",retiroEntradaProducto.getEmpresaId());

        List<Stock> stockG1 = stockMapper.listByParameterMap(params);
        if(stockG1.size()>0){
            resultValidacion.put("stockBD",stockG1.get(0));
        }

        params.clear();

        if(retiroEntradaProducto.getLoteId() != null && !retiroEntradaProducto.getLoteId().equals(Constantes.CANTIDAD_ZERO_LONG)){
            Lote loteBD = loteDAO.listarPorId(retiroEntradaProducto.getLoteId() );
            resultValidacion.put("loteBD",loteBD);
        }



        boolean validacion = this.validacionRegistro(retiroEntradaProducto, resultValidacion);

        if(validacion)
            return this.grabarRegistro(retiroEntradaProducto, resultValidacion);

        String errorValidacion = "Error de validación Método Registrar Entrada o Salida de Productos";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(RetiroEntradaProducto retiroEntradaProducto) throws Exception {
        return 0;
    }

    @Override
    public List<RetiroEntradaProducto> listar() throws Exception {
        return null;
    }

    @Override
    public RetiroEntradaProducto listarPorId(Long aLong) throws Exception {
        return null;
    }

    @Override
    public void eliminar(Long aLong) throws Exception {

    }

    @Override
    public RetiroEntradaProducto grabarRegistro(RetiroEntradaProducto retiroEntradaProducto) throws Exception {
        return null;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public RetiroEntradaProducto grabarRegistro(RetiroEntradaProducto retiroEntradaProducto, Map<String, Object> datos) throws Exception {

        //Modificaion Stock
        Stock  stockModificar= (Stock) datos.get("stockBD");
        if(retiroEntradaProducto.getTipo().equals(Constantes.TIPO_ENTRADA_PRODUCTOS))
            stockModificar.setCantidad(stockModificar.getCantidad() + retiroEntradaProducto.getCantidadReal());

        if(retiroEntradaProducto.getTipo().equals(Constantes.TIPO_RETIRO_PRODUCTOS))
            stockModificar.setCantidad(stockModificar.getCantidad() - retiroEntradaProducto.getCantidadReal());

        stockModificar.setUpdatedAd(retiroEntradaProducto.getUpdatedAd());
        stockDAO.modificar(stockModificar);

        if(!retiroEntradaProducto.getLoteId().equals(Constantes.CANTIDAD_ZERO_LONG)){

            //Modificaion Stock
            Lote loteBD = (Lote) datos.get("loteBD");
            if(retiroEntradaProducto.getTipo().equals(Constantes.TIPO_ENTRADA_PRODUCTOS))
                loteBD.setCantidad(loteBD.getCantidad() + retiroEntradaProducto.getCantidadReal());

            if(retiroEntradaProducto.getTipo().equals(Constantes.TIPO_RETIRO_PRODUCTOS))
                loteBD.setCantidad(loteBD.getCantidad() - retiroEntradaProducto.getCantidadReal());

            loteBD.setUpdatedAd(retiroEntradaProducto.getUpdatedAd());
            loteDAO.modificar(loteBD);

        }

        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        retiroEntradaProducto.setFecha(fechaActual);
        retiroEntradaProducto.setHora(horaActual);

        return retiroEntradaProductoDAO.registrar(retiroEntradaProducto);
    }

    @Override
    public int grabarRectificar(RetiroEntradaProducto retiroEntradaProducto) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long aLong) throws Exception {

    }

    @Override
    public boolean validacionRegistro(RetiroEntradaProducto retiroEntradaProducto, Map<String, Object> resultValidacion) {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        Stock  stockBD= (Stock) resultValidacion.get("stockBD");



        if(stockBD != null && retiroEntradaProducto.getTipo().equals(Constantes.TIPO_RETIRO_PRODUCTOS) && stockBD.getCantidad() < retiroEntradaProducto.getCantidadReal())
        {
            resultado = false;
            error = "La Cantidad de Salida indicada es superior a la cantidad total de Stock del Producto, por favor modifique.";
            errors.add(error);
        }

        if(!retiroEntradaProducto.getLoteId().equals(Constantes.CANTIDAD_ZERO_LONG)){

            Lote loteBD = (Lote) resultValidacion.get("loteBD");

            if(loteBD != null &&  retiroEntradaProducto.getTipo().equals(Constantes.TIPO_RETIRO_PRODUCTOS) && loteBD.getCantidad() < retiroEntradaProducto.getCantidadReal())
            {
                resultado = false;
                error = "La Cantidad de Salida indicada es superior a la cantidad total del Lote del Producto seleccionado, por favor modifique.";
                errors.add(error);
            }
        }


        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(RetiroEntradaProducto retiroEntradaProducto, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long aLong, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }
}

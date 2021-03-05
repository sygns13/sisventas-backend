package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.ProductoDAO;
import com.bcs.ventas.dao.RetiroEntradaProductoDAO;
import com.bcs.ventas.dao.StockDAO;
import com.bcs.ventas.dao.mappers.AlmacenMapper;
import com.bcs.ventas.dao.mappers.StockMapper;
import com.bcs.ventas.exception.ValidationServiceException;
import com.bcs.ventas.model.dto.AlmacenStockDTO;
import com.bcs.ventas.model.dto.LoteAlmacenUnidadDTO;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.RetiroEntradaProducto;
import com.bcs.ventas.model.entities.Stock;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.service.StockService;
import com.bcs.ventas.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StockServiceImpl implements StockService {

    @Autowired
    private AlmacenMapper almacenMapper;

    @Autowired
    private ProductoDAO productoDAO;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private RetiroEntradaProductoDAO retiroEntradaProductoDAO;


    @Override
    public Stock registrar(Stock stock) throws Exception {

        LocalDateTime fechaActual = LocalDateTime.now();
        stock.setCreatedAt(fechaActual);
        stock.setUpdatedAd(fechaActual);

        //TODO: Temporal hasta incluir Oauth inicio
        stock.setEmpresaId(1L);
        stock.setUserId(2L);
        //Todo: Temporal hasta incluir Oauth final

        stock.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        stock.setActivo(Constantes.REGISTRO_ACTIVO);

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(stock, resultValidacion);

        if(validacion)
            return this.grabarRegistro(stock);

        String errorValidacion = "Error de validación Método Registrar Stock";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }

        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(Stock stock) throws Exception {
        return 0;
    }

    @Override
    public List<Stock> listar() throws Exception {
        return null;
    }

    @Override
    public Stock listarPorId(Long aLong) throws Exception {
        return null;
    }

    @Override
    public void eliminar(Long aLong) throws Exception {

    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public Stock grabarRegistro(Stock stock) throws Exception {

        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        //String datoZero = Constantes.ID_INGRESO_SALIDA_STOCK_NO_LOTE;

        if(stock != null){

            if(stock.getId() != null && stock.getId() > 0){

                Stock stockBD = stockDAO.listarPorId(stock.getId());

                double cantDiferida = stock.getCantidad() - stockBD.getCantidad();

                if(cantDiferida > 0){
                    LocalDateTime fechaActualDateTime = LocalDateTime.now();
                    RetiroEntradaProducto retiroEntradaProducto = new RetiroEntradaProducto();

                    retiroEntradaProducto.setFecha(fechaActual);
                    retiroEntradaProducto.setMotivo(Constantes.MOTIVO_INGRESO_MODIFICACION_STOCK);
                    retiroEntradaProducto.setLoteId(Constantes.ID_INGRESO_SALIDA_STOCK_NO_LOTE);
                    retiroEntradaProducto.setHora(horaActual);
                    retiroEntradaProducto.setCantidadReal(cantDiferida);
                    retiroEntradaProducto.setUserId(stock.getUserId());
                    retiroEntradaProducto.setEmpresaId(stock.getEmpresaId());
                    retiroEntradaProducto.setActivo(Constantes.REGISTRO_ACTIVO);
                    retiroEntradaProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);
                    retiroEntradaProducto.setTipo(Constantes.TIPO_ENTRADA_PRODUCTOS);
                    retiroEntradaProducto.setAlmacenId(stock.getAlmacenId());
                    retiroEntradaProducto.setProductoId(stock.getProductoId());
                    retiroEntradaProducto.setCreatedAt(fechaActualDateTime);
                    retiroEntradaProducto.setUpdatedAd(fechaActualDateTime);

                    retiroEntradaProducto = retiroEntradaProductoDAO.registrar(retiroEntradaProducto);

                }else if(cantDiferida < 0){
                    LocalDateTime fechaActualDateTime = LocalDateTime.now();
                    RetiroEntradaProducto retiroEntradaProducto = new RetiroEntradaProducto();

                    retiroEntradaProducto.setFecha(fechaActual);
                    retiroEntradaProducto.setMotivo(Constantes.MOTIVO_SALIDA_MODIFICACION_STOCK);
                    retiroEntradaProducto.setLoteId(Constantes.ID_INGRESO_SALIDA_STOCK_NO_LOTE);
                    retiroEntradaProducto.setHora(horaActual);
                    retiroEntradaProducto.setCantidadReal(Math.abs(cantDiferida));
                    retiroEntradaProducto.setUserId(stock.getUserId());
                    retiroEntradaProducto.setEmpresaId(stock.getEmpresaId());
                    retiroEntradaProducto.setActivo(Constantes.REGISTRO_ACTIVO);
                    retiroEntradaProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);
                    retiroEntradaProducto.setTipo(Constantes.TIPO_RETIRO_PRODUCTOS);
                    retiroEntradaProducto.setAlmacenId(stock.getAlmacenId());
                    retiroEntradaProducto.setProductoId(stock.getProductoId());
                    retiroEntradaProducto.setCreatedAt(fechaActualDateTime);
                    retiroEntradaProducto.setUpdatedAd(fechaActualDateTime);

                    retiroEntradaProducto = retiroEntradaProductoDAO.registrar(retiroEntradaProducto);
                }

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("ID", stock.getId());
                params.put("CANTIDAD", stock.getCantidad());

                stockMapper.updateByPrimaryKeySelective(params);

                return stock;
            }
            else{
                LocalDateTime fechaActualDateTime = LocalDateTime.now();
                RetiroEntradaProducto retiroEntradaProducto = new RetiroEntradaProducto();

                retiroEntradaProducto.setFecha(fechaActual);
                retiroEntradaProducto.setMotivo(Constantes.MOTIVO_INGRESO_CREACION_STOCK);
                retiroEntradaProducto.setLoteId(Constantes.ID_INGRESO_SALIDA_STOCK_NO_LOTE);
                retiroEntradaProducto.setHora(horaActual);
                retiroEntradaProducto.setCantidadReal(stock.getCantidad());
                retiroEntradaProducto.setUserId(stock.getUserId());
                retiroEntradaProducto.setEmpresaId(stock.getEmpresaId());
                retiroEntradaProducto.setActivo(Constantes.REGISTRO_ACTIVO);
                retiroEntradaProducto.setBorrado(Constantes.REGISTRO_NO_BORRADO);
                retiroEntradaProducto.setTipo(Constantes.TIPO_ENTRADA_PRODUCTOS);
                retiroEntradaProducto.setAlmacenId(stock.getAlmacenId());
                retiroEntradaProducto.setProductoId(stock.getProductoId());
                retiroEntradaProducto.setCreatedAt(fechaActualDateTime);
                retiroEntradaProducto.setUpdatedAd(fechaActualDateTime);

                retiroEntradaProducto = retiroEntradaProductoDAO.registrar(retiroEntradaProducto);


                return stockDAO.registrar(stock);

            }

        }


        return null;
    }

    @Override
    public int grabarRectificar(Stock stock) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long aLong) {

    }

    @Override
    public boolean validacionRegistro(Stock stock, Map<String, Object> resultValidacion) {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        /*
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("NOMBRE",stock.getNombre());
        params.put("EMPRESA_ID",stock.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);


        List<Almacen> almacensV1 = almacenMapper.listByParameterMap(params);



        if(almacensV1.size() > 0){
            resultado = false;
            error = "El nombre del local ingresado ya se encuentra registrado";
            errors.add(error);
        }

        params.clear();
        */

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Stock stock, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long aLong, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public List<Almacen> getAlmacens(Long idEmpresa) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID", idEmpresa);
        return almacenMapper.listByParameterMapOrderId(params);
    }

    @Override
    public Map<String, Object> listar(Long idAlmacen, Long idProducto, Long idEmpresa) throws Exception {

        Map<String, Object> resultado = new HashMap<>();
        Producto producto = productoDAO.listarPorId(idProducto);

        if(producto != null && producto.getActivoLotes() != null){

            if(producto.getActivoLotes().intValue() == Constantes.REGISTRO_ACTIVO.intValue()){

                //Hardoced idAlmacen, render en la vista
                Long idAlmacenHarcoded = Constantes.ALMACEN_GENERAL;

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("PRODUCTO_ID", idProducto);
                params.put("EMPRESA_ID", idEmpresa);
                params.put("CANTIDAD_ZERO", Constantes.CANTIDAD_ZERO);
                params.put("ACTIVO_LOTE", Constantes.REGISTRO_ACTIVO);
                params.put("ACTIVO_ALMACEN", Constantes.REGISTRO_ACTIVO);
                params.put("NO_BORRADO_LOTE", Constantes.REGISTRO_BORRADO);
                params.put("NO_BORRADO_ALMACEN", Constantes.REGISTRO_BORRADO);

                List<LoteAlmacenUnidadDTO> respuesta = stockMapper.listDTOByParameterMapLoteAlmacenUnidad(params);

                resultado.put("respuesta",respuesta);

            }
            else if(producto.getActivoLotes().intValue() == Constantes.REGISTRO_INACTIVO.intValue()){

                //Hardoced idAlmacen, render en la vista
                Long idAlmacenHarcoded = Constantes.ALMACEN_GENERAL;

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("PRODUCTO_ID", idProducto);
                params.put("EMPRESA_ID", idEmpresa);
                params.put("ACTIVO_STOCK", Constantes.REGISTRO_ACTIVO);
                params.put("ACTIVO_ALMACEN", Constantes.REGISTRO_ACTIVO);
                params.put("NO_BORRADO_STOCK", Constantes.REGISTRO_BORRADO);
                params.put("NO_BORRADO_ALMACEN", Constantes.REGISTRO_BORRADO);

                List<AlmacenStockDTO> respuesta = stockMapper.listDTOByParameterMapLoteAlmacenStock(params);

                resultado.put("respuesta",respuesta);
            }
        }


        return resultado;
    }
}

package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Stock;

import java.util.List;
import java.util.Map;

public interface StockService extends GeneralService<Stock, Long>  {

    List<Almacen> getAlmacens(Long idEmpresa) throws Exception;

    Map<String, Object> listar(Long idAlmacen, Long idProducto, Long idEmpresa) throws Exception;

    List<Almacen> getAlmacensProducts(Long idEmpresa, Long idProducto) throws Exception;

    Map<String, Object> getAlmacenProducts(Long idEmpresa, Long idAlmacen, Long idProducto) throws Exception;

    Map<String, Object> getAlmacenProductsLote(Long idEmpresa, Long idAlmacen, Long idProducto, Long idLote) throws Exception;
}

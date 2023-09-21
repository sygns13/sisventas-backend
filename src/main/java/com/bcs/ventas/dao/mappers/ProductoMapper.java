package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.dto.InventarioDTO;
import com.bcs.ventas.model.dto.ProductoBajoStockDTO;
import com.bcs.ventas.model.dto.ProductoVencidoDTO;
import com.bcs.ventas.model.dto.ProductosVentaDTO;
import com.bcs.ventas.model.entities.Marca;
import com.bcs.ventas.model.entities.Presentacion;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.TipoProducto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface ProductoMapper extends GeneralMapper<Producto> {

    List<TipoProducto> getTipoProducto(Map<String, Object> params);

    List<Marca> getMarca(Map<String, Object> params);

    List<Presentacion> getPresentacion(Map<String, Object> params);

    Long getTotalElements(Map<String, Object> var1);

    Long getTotalElementsInventario(Map<String, Object> var1);

    List<InventarioDTO> listByParameterMapInventario(Map<String, Object> var1);

    Long getTotalElementsBajoStock(Map<String, Object> var1);

    List<ProductoBajoStockDTO> listByParameterMapProductosBajoStock(Map<String, Object> var1);

    Long getTotalElementsProductosVencidos(Map<String, Object> var1);

    List<ProductoVencidoDTO> listByParameterMapProductosVencidos(Map<String, Object> var1);

    List<ProductosVentaDTO> listByParameterMapProductosVenta(Map<String, Object> var1);

    List<ProductosVentaDTO> listByParameterMapProductoVenta(Map<String, Object> var1);

    Long getTotalElementsProductosVenta(Map<String, Object> var1);
}

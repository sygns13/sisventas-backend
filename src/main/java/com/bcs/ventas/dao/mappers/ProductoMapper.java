package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.dto.InventarioDTO;
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

    int getTotalElements(Map<String, Object> var1);

    int getTotalElementsInventario(Map<String, Object> var1);

    List<InventarioDTO> listByParameterMapInventario(Map<String, Object> var1);
}

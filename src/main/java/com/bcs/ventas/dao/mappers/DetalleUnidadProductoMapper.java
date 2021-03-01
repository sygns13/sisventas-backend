package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.DetalleUnidadProducto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DetalleUnidadProductoMapper extends GeneralMapper<DetalleUnidadProducto> {

    List<DetalleUnidadProducto> listByParameterMapBaseUnidad(Map<String, Object> var1);

}

package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.TipoProducto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface TipoProductoMapper extends GeneralMapper<TipoProducto> {

    Long getTotalElements(Map<String, Object> var1);


}

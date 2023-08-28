package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Almacen;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlmacenMapper extends GeneralMapper<Almacen> {

    Long getTotalElements(Map<String, Object> var1);

    List<Almacen> listByParameterMapOrderId(Map<String, Object> var1);

}

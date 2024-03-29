package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Unidad;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface UnidadMapper extends GeneralMapper<Unidad> {

    Long getTotalElements(Map<String, Object> var1);

}

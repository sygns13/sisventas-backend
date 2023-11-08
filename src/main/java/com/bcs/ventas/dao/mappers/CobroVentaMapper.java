package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.CobroVenta;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface CobroVentaMapper extends GeneralMapper<CobroVenta> {

    Long getTotalElements(Map<String, Object> var1);

}

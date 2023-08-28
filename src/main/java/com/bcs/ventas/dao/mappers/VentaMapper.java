package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Venta;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface VentaMapper extends GeneralMapper<Venta> {

    Long getTotalElements(Map<String, Object> var1);

}

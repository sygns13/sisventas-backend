package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Presentacion;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface PresentacionMapper extends GeneralMapper<Presentacion> {

    int getTotalElements(Map<String, Object> var1);

}

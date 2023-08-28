package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Servicio;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ServicioMapper extends GeneralMapper<Servicio>{

    Long getTotalElements(Map<String, Object> var1);
}

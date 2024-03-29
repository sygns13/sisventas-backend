package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Caja;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface CajaMapper extends GeneralMapper<Caja> {

    Long getTotalElements(Map<String, Object> var1);

}

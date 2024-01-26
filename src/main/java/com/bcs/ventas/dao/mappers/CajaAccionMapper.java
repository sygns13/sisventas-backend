package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.CajaAccion;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface CajaAccionMapper extends GeneralMapper<CajaAccion> {

    Long getTotalElements(Map<String, Object> var1);

}

package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.CajaDato;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface CajaDatoMapper extends GeneralMapper<CajaDato> {

    Long getTotalElements(Map<String, Object> var1);

}

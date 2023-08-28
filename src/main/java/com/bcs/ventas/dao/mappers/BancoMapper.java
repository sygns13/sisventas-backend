package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Banco;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface BancoMapper extends GeneralMapper<Banco> {

    Long getTotalElements(Map<String, Object> var1);

}

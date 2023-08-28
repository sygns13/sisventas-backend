package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Cliente;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface ClienteMapper extends GeneralMapper<Cliente> {

    Long getTotalElements(Map<String, Object> var1);

}

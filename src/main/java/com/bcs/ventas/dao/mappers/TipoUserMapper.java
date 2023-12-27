package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.TipoUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TipoUserMapper extends GeneralMapper<TipoUser> {
    Long getTotalElements(Map<String, Object> var1);

}

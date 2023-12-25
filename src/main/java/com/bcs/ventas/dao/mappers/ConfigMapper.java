package com.bcs.ventas.dao.mappers;
import com.bcs.ventas.model.entities.Config;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface ConfigMapper extends GeneralMapper<Config> {

    Long getTotalElements(Map<String, Object> var1);
}

package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Marca;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface MarcaMapper extends GeneralMapper<Marca> {

    int getTotalElements(Map<String, Object> var1);

}

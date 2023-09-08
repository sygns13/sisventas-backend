package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Lote;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface LoteMapper extends GeneralMapper<Lote> {

    Long getTotalElements(Map<String, Object> var1);

}

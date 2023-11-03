package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.InitComprobante;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface InitComprobanteMapper extends GeneralMapper<InitComprobante> {

    Long getTotalElements(Map<String, Object> var1);
}

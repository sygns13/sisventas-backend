package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.CabeceraComprobante;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface CabeceraComprobanteMapper extends GeneralMapper<CabeceraComprobante> {

    Long getTotalElements(Map<String, Object> var1);

}

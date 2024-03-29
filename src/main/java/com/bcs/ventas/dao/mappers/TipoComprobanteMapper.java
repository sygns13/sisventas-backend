package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.TipoComprobante;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface TipoComprobanteMapper extends GeneralMapper<TipoComprobante> {

    Long getTotalElements(Map<String, Object> var1);

}

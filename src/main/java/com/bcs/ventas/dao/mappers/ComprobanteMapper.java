package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Comprobante;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface ComprobanteMapper extends GeneralMapper<Comprobante> {

    Long getTotalElements(Map<String, Object> var1);

}

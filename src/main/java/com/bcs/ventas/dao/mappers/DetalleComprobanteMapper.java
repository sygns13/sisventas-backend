package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.DetalleComprobante;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface DetalleComprobanteMapper extends GeneralMapper<DetalleComprobante> {

    Long getTotalElements(Map<String, Object> var1);

}

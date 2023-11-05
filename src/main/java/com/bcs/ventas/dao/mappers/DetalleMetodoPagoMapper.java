package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.DetalleMetodoPago;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface DetalleMetodoPagoMapper extends GeneralMapper<DetalleMetodoPago> {

    Long getTotalElements(Map<String, Object> var1);

}

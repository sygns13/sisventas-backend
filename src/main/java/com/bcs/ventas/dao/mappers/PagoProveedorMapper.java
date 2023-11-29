package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.PagoProveedor;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface PagoProveedorMapper extends GeneralMapper<PagoProveedor> {

    Long getTotalElements(Map<String, Object> var1);
}

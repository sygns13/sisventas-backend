package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.FacturaProveedor;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface FacturaProveedorMapper extends GeneralMapper<FacturaProveedor> {

    Long getTotalElements(Map<String, Object> var1);

}

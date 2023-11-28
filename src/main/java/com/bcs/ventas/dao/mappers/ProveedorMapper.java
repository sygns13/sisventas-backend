package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Proveedor;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface ProveedorMapper extends GeneralMapper<Proveedor>{

    Long getTotalElements(Map<String, Object> var1);

}

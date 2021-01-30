package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.CuentaProveedor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CuentaProveedorMapper {

    List<CuentaProveedor> getAllEntities();

    List<CuentaProveedor> listByParameterMap(Map<String, Object> var1);

    public int updateByPrimaryKeySelective(Map<String, Object> var1);
}

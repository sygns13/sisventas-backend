package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.DetalleOrdenCompra;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DetalleOrdenCompraMapper {

    List<DetalleOrdenCompra> getAllEntities();

    List<DetalleOrdenCompra> listByParameterMap(Map<String, Object> var1);

    public int updateByPrimaryKeySelective(Map<String, Object> var1);
}

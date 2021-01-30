package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.DetalleEntradaStock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DetalleEntradaStockMapper {

    List<DetalleEntradaStock> getAllEntities();

    List<DetalleEntradaStock> listByParameterMap(Map<String, Object> var1);

    public int updateByPrimaryKeySelective(Map<String, Object> var1);
}

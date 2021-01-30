package com.bcs.ventas.dao.mappers;


import com.bcs.ventas.model.entities.CobroVenta;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CobroVentaMapper {

    List<CobroVenta> getAllEntities();

    List<CobroVenta> listByParameterMap(Map<String, Object> var1);

    public int updateByPrimaryKeySelective(Map<String, Object> var1);
}

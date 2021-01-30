package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Comprobante;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ComprobanteMapper {

    List<Comprobante> getAllEntities();

    List<Comprobante> listByParameterMap(Map<String, Object> var1);

    public int updateByPrimaryKeySelective(Map<String, Object> var1);
}

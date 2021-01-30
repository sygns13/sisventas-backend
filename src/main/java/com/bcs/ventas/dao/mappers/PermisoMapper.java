package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Permiso;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PermisoMapper {

    List<Permiso> getAllEntities();

    List<Permiso> listByParameterMap(Map<String, Object> var1);

    public int updateByPrimaryKeySelective(Map<String, Object> var1);
}

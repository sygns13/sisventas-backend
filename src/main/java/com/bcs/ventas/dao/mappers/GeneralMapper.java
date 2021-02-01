package com.bcs.ventas.dao.mappers;

import java.util.List;
import java.util.Map;

public interface GeneralMapper<T> {

    List<T> getAllEntities();

    List<T> listByParameterMap(Map<String, Object> var1);

    int updateByPrimaryKeySelective(Map<String, Object> var1);
}

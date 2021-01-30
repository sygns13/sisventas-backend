package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.Deposito;

import java.util.List;
import java.util.Map;

public interface DepositoMapper {

    List<Deposito> getAllEntities();

    List<Deposito> listByParameterMap(Map<String, Object> var1);

    public int updateByPrimaryKeySelective(Map<String, Object> var1);
}

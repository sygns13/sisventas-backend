package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.CajaUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CajaUserMapper extends GeneralMapper<CajaUser> {

    Long getTotalElements(Map<String, Object> var1);

    Long getTotalElementsCajasAsigneds(Map<String, Object> var1);

    List<CajaUser> listCajasAsignedsByParameterMap(Map<String, Object> var1);

}
package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface IngresoSalidaCajaMapper extends GeneralMapper<IngresoSalidaCaja> {

    Long getTotalElements(Map<String, Object> var1);

}

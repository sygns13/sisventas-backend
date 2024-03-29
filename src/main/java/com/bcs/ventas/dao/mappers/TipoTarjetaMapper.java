package com.bcs.ventas.dao.mappers;


import com.bcs.ventas.model.entities.TipoTarjeta;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface TipoTarjetaMapper extends GeneralMapper<TipoTarjeta> {

    Long getTotalElements(Map<String, Object> var1);

}

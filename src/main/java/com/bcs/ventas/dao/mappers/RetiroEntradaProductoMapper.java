package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.dto.RetiroEntradaProductoDTO;
import com.bcs.ventas.model.entities.RetiroEntradaProducto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface RetiroEntradaProductoMapper extends GeneralMapper<RetiroEntradaProducto> {

    List<RetiroEntradaProductoDTO> getAllEntitiesGeneral(Map<String, Object> params);

}

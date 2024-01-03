package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.dto.ComprasDetallesDTO;
import com.bcs.ventas.model.entities.EntradaStock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface EntradaStockMapper extends GeneralMapper<EntradaStock> {

    Long getTotalElements(Map<String, Object> var1);

    Long getTotalElementsPagar(Map<String, Object> var1);

    Long getTotalElementsDetail(Map<String, Object> var1);
    List<ComprasDetallesDTO> listDetailByParameterMapPagar(Map<String, Object> var1);

}

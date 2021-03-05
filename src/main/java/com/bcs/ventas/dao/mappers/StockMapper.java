package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.dto.AlmacenStockDTO;
import com.bcs.ventas.model.dto.LoteAlmacenUnidadDTO;
import com.bcs.ventas.model.entities.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface StockMapper extends GeneralMapper<Stock> {

    List<LoteAlmacenUnidadDTO> listDTOByParameterMapLoteAlmacenUnidad(Map<String, Object> var1);

    List<AlmacenStockDTO> listDTOByParameterMapLoteAlmacenStock(Map<String, Object> var1);

}

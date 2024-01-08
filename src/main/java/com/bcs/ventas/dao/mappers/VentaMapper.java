package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.dto.TopProductosVendidosDTO;
import com.bcs.ventas.model.dto.VentasDetallesDTO;
import com.bcs.ventas.model.entities.CobroVenta;
import com.bcs.ventas.model.entities.Venta;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface VentaMapper extends GeneralMapper<Venta> {

    Long getTotalElements(Map<String, Object> var1);

    Long getTotalElementsCobrar(Map<String, Object> var1);

    List<Venta> listByParameterMapCobrar(Map<String, Object> var1);

    List<VentasDetallesDTO> listDetailByParameterMap(Map<String, Object> var1);

    Long getTotalElementsPagarDetail(Map<String, Object> var1);

    List<TopProductosVendidosDTO> listTopProductosVendidosByParameterMap(Map<String, Object> var1);

    Long getTotalElementsTopProductosVendidos(Map<String, Object> var1);

    List<CobroVenta> listByParameterMapCobrarVenta(Map<String, Object> var1);

    Long getTotalElementsCobrarVenta(Map<String, Object> var1);

}

package com.bcs.ventas.service;

import com.bcs.ventas.model.dto.RetiroEntradaProductoDTO;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.RetiroEntradaProducto;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RetiroEntradaProductoService extends GeneralService<RetiroEntradaProducto, Long> {

    Page<RetiroEntradaProductoDTO> listar(Pageable pageable, String buscar) throws Exception;

    List<Almacen> getAlmacens(Long idEmpresa) throws Exception;

    Page<RetiroEntradaProductoDTO> getMovimientosLibresProductos(Pageable pageable, FiltroGeneral filtros) throws Exception;


}

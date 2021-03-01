package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.DetalleUnidadProducto;

import java.util.List;

public interface DetalleUnidadProductoService extends GeneralService<DetalleUnidadProducto, Long>  {

    List<Almacen> getAlmacens(Long idEmpresa) throws Exception;

    List<DetalleUnidadProducto> listar(Long idAlmacen, Long idProducto, Long idEmpresa) throws Exception;
}

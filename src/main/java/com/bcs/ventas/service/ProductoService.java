package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.Marca;
import com.bcs.ventas.model.entities.Presentacion;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.TipoProducto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductoService extends GeneralService<Producto, Long> {

    List<TipoProducto> getTipoProductos() throws Exception;

    List<Marca> getMarcas() throws Exception;

    List<Presentacion> getPresentaciones() throws Exception;

    Page<Producto> listar(Pageable pageable, String buscar) throws Exception;

}

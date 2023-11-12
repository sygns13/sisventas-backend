package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.CobroVenta;
import com.bcs.ventas.model.entities.DetalleVenta;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.Venta;
import com.bcs.ventas.utils.beans.AgregarProductoBean;
import com.bcs.ventas.utils.beans.FiltroVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VentaService extends GeneralService<Venta, Long> {

    Page<Venta> listar(Pageable pageable, FiltroVenta filtros) throws Exception;

    Venta modificarVenta(Venta venta) throws Exception;

    Venta modificarVentaCliente(Venta venta) throws Exception;


    Venta registrarDetalle(DetalleVenta detalleVentaventa) throws Exception;

    Venta agregarProducto(AgregarProductoBean addProductoVenta) throws Exception;
    Venta eliminarDetalle(DetalleVenta detalleVentaventa) throws Exception;
    Venta modificarDetalle(DetalleVenta detalleVentaventa) throws Exception;

    Venta resetVenta(Venta venta) throws Exception;

    CobroVenta cobrarVenta(CobroVenta cobroVenta) throws Exception;
}

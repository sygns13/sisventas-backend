package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.PagoProveedor;
import com.bcs.ventas.model.entities.DetalleEntradaStock;
import com.bcs.ventas.model.entities.EntradaStock;
import com.bcs.ventas.utils.beans.AgregarProductoBean;
import com.bcs.ventas.utils.beans.FiltroEntradaStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntradaStockService extends GeneralService<EntradaStock, Long>{

    Page<EntradaStock> listar(Pageable pageable, FiltroEntradaStock filtros) throws Exception;

    EntradaStock modificarEntradaStock(EntradaStock venta) throws Exception;

    EntradaStock modificarEntradaStockProveedorFirst(EntradaStock venta) throws Exception;

    EntradaStock modificarEntradaStockProveedor(EntradaStock venta) throws Exception;


    EntradaStock registrarDetalle(DetalleEntradaStock detalleEntradaStockventa) throws Exception;

    EntradaStock agregarProducto(AgregarProductoBean addProductoEntradaStock) throws Exception;
    EntradaStock eliminarDetalle(DetalleEntradaStock detalleEntradaStockventa) throws Exception;
    EntradaStock modificarDetalle(DetalleEntradaStock detalleEntradaStockventa) throws Exception;

    EntradaStock resetEntradaStock(EntradaStock venta) throws Exception;

    PagoProveedor cobrarEntradaStock(PagoProveedor cobroEntradaStock) throws Exception;

    EntradaStock recalcularEntradaStockPublic(EntradaStock venta) throws Exception;

    EntradaStock generarComprobante(EntradaStock venta) throws Exception;

    void anular(Long id) throws Exception;

    Page<EntradaStock> listarPagado(Pageable pageable, FiltroEntradaStock filtros) throws Exception;

    Page<PagoProveedor> listarPagos(Pageable pageable, Long id) throws Exception;
}

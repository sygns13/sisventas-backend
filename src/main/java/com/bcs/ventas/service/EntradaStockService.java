package com.bcs.ventas.service;

import com.bcs.ventas.model.dto.ComprasDetallesDTO;
import com.bcs.ventas.model.dto.CuentasPagarDetallesDTO;
import com.bcs.ventas.model.dto.EgresosComprasDTO;
import com.bcs.ventas.model.entities.PagoProveedor;
import com.bcs.ventas.model.entities.DetalleEntradaStock;
import com.bcs.ventas.model.entities.EntradaStock;
import com.bcs.ventas.utils.beans.AgregarProductoBean;
import com.bcs.ventas.utils.beans.FiltroEntradaStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntradaStockService extends GeneralService<EntradaStock, Long>{

    Page<EntradaStock> listar(Pageable pageable, FiltroEntradaStock filtros) throws Exception;

    EntradaStock modificarEntradaStock(EntradaStock compra) throws Exception;

    EntradaStock modificarEntradaStockProveedorFirst(EntradaStock compra) throws Exception;

    EntradaStock modificarEntradaStockProveedor(EntradaStock compra) throws Exception;


    EntradaStock registrarDetalle(DetalleEntradaStock detalleEntradaStockcompra) throws Exception;

    EntradaStock agregarProducto(AgregarProductoBean addProductoEntradaStock) throws Exception;
    EntradaStock eliminarDetalle(DetalleEntradaStock detalleEntradaStockcompra) throws Exception;
    EntradaStock modificarDetalle(DetalleEntradaStock detalleEntradaStockcompra) throws Exception;

    EntradaStock resetEntradaStock(EntradaStock compra) throws Exception;

    PagoProveedor cobrarEntradaStock(PagoProveedor cobroEntradaStock) throws Exception;

    EntradaStock recalcularEntradaStockPublic(EntradaStock compra) throws Exception;

    EntradaStock generarComprobante(EntradaStock compra) throws Exception;

    void anular(Long id) throws Exception;

    Page<EntradaStock> listarPagado(Pageable pageable, FiltroEntradaStock filtros) throws Exception;

    Page<PagoProveedor> listarPagadoDetallado(Pageable pageable, FiltroEntradaStock filtros) throws Exception;

    Page<PagoProveedor> listarPagos(Pageable pageable, Long id) throws Exception;

    EntradaStock facturarEntradaStock(EntradaStock compra) throws Exception;

    EntradaStock actualizarEntradaStock(EntradaStock compra) throws Exception;

    EntradaStock revertirFacturaEntradaStock(EntradaStock compra) throws Exception;

    EntradaStock revertirActualizacionEntradaStock(EntradaStock compra) throws Exception;


    Page<ComprasDetallesDTO> listarDetail(Pageable pageable, FiltroEntradaStock filtros) throws Exception;

    EgresosComprasDTO listarIngresosVentas(Pageable pageable, FiltroEntradaStock filtros) throws Exception;
}

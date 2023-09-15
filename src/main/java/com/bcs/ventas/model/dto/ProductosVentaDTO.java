package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.DetalleUnidadProducto;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.Stock;

public class ProductosVentaDTO {

    private Producto producto;

    private Almacen almacen;

    private Stock stock;

    private DetalleUnidadProducto detalleUnidadProducto;

    public ProductosVentaDTO() {
    }

    public ProductosVentaDTO(Producto producto, Almacen almacen, Stock stock, DetalleUnidadProducto detalleUnidadProducto) {
        this.producto = producto;
        this.almacen = almacen;
        this.stock = stock;
        this.detalleUnidadProducto = detalleUnidadProducto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public DetalleUnidadProducto getDetalleUnidadProducto() {
        return detalleUnidadProducto;
    }

    public void setDetalleUnidadProducto(DetalleUnidadProducto detalleUnidadProducto) {
        this.detalleUnidadProducto = detalleUnidadProducto;
    }
}

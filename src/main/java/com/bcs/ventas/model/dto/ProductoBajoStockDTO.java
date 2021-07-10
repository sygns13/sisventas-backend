package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.Stock;

public class ProductoBajoStockDTO {

    private Producto producto;

    private Almacen almacen;

    private Stock stock;

    public ProductoBajoStockDTO() {
    }

    public ProductoBajoStockDTO(Producto producto, Almacen almacen, Stock stock) {
        this.producto = producto;
        this.almacen = almacen;
        this.stock = stock;
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
}

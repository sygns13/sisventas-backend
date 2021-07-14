package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.*;

public class SalidasIngresosProductosDTO {

    private Producto producto;

    private Almacen almacen;

    private Stock stock;

    private Lote lote;

    private RetiroEntradaProducto retiroEntradaProducto;

    private Unidad unidad;

    private User user;

    public SalidasIngresosProductosDTO() {
    }

    public SalidasIngresosProductosDTO(Producto producto, Almacen almacen, Stock stock, Lote lote, RetiroEntradaProducto retiroEntradaProducto, Unidad unidad, User user) {
        this.producto = producto;
        this.almacen = almacen;
        this.stock = stock;
        this.lote = lote;
        this.retiroEntradaProducto = retiroEntradaProducto;
        this.unidad = unidad;
        this.user = user;
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

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public RetiroEntradaProducto getRetiroEntradaProducto() {
        return retiroEntradaProducto;
    }

    public void setRetiroEntradaProducto(RetiroEntradaProducto retiroEntradaProducto) {
        this.retiroEntradaProducto = retiroEntradaProducto;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

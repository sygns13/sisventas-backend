package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.*;

public class RetiroEntradaProductoDTO {

    private Producto producto;

    private RetiroEntradaProducto retiroEntradaProducto;

    private Lote lote;

    private User user;

    private Almacen almacen;

    public RetiroEntradaProductoDTO() {
    }

    public RetiroEntradaProductoDTO(Producto producto, RetiroEntradaProducto retiroEntradaProducto, Lote lote, User user, Almacen almacen) {
        this.producto = producto;
        this.retiroEntradaProducto = retiroEntradaProducto;
        this.lote = lote;
        this.user = user;
        this.almacen = almacen;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public RetiroEntradaProducto getRetiroEntradaProducto() {
        return retiroEntradaProducto;
    }

    public void setRetiroEntradaProducto(RetiroEntradaProducto retiroEntradaProducto) {
        this.retiroEntradaProducto = retiroEntradaProducto;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }
}

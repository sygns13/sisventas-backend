package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Lote;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.Unidad;

public class ProductoVencidoDTO {

    private Producto producto;

    private Almacen almacen;

    private Lote lote;

    private Unidad unidad;

    private Double cantidadTotal;

    public ProductoVencidoDTO() {
    }

    public ProductoVencidoDTO(Producto producto, Almacen almacen, Lote lote, Unidad unidad, Double cantidadTotal) {
        this.producto = producto;
        this.almacen = almacen;
        this.lote = lote;
        this.unidad = unidad;
        this.cantidadTotal = cantidadTotal;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public Double getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Double cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}

package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Stock;

public class AlmacenStockDTO {

    private Almacen almacen;

    private Stock stock;

    private Double cantidadTotal;

    private String  editar;

    public AlmacenStockDTO() {
    }

    public AlmacenStockDTO(Almacen almacen, Stock stock, Double cantidadTotal, String editar) {
        this.almacen = almacen;
        this.stock = stock;
        this.cantidadTotal = cantidadTotal;
        this.editar = editar;
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

    public Double getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Double cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public String getEditar() {
        return editar;
    }

    public void setEditar(String editar) {
        this.editar = editar;
    }
}

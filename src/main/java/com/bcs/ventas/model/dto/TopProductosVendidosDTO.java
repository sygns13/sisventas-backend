package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.Producto;

import java.math.BigDecimal;

public class TopProductosVendidosDTO {

    private Producto producto;

    private BigDecimal cantidad;
    private BigDecimal ventas;
    private BigDecimal importe;

    public TopProductosVendidosDTO() {
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getVentas() {
        return ventas;
    }

    public void setVentas(BigDecimal ventas) {
        this.ventas = ventas;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
}

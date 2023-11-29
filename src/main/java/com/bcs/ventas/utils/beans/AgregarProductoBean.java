package com.bcs.ventas.utils.beans;

import com.bcs.ventas.model.entities.Lote;

public class AgregarProductoBean {

    private Long idVenta;

    private Long idEntradaStock;


    private String codigoUnidad;

    private Lote lote;

    public AgregarProductoBean() {
    }

    public AgregarProductoBean(Long idVenta, Long idEntradaStock, String codigoUnidad) {
        this.idVenta = idVenta;
        this.idEntradaStock = idEntradaStock;
        this.codigoUnidad = codigoUnidad;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public Long getIdEntradaStock() {
        return idEntradaStock;
    }

    public void setIdEntradaStock(Long idEntradaStock) {
        this.idEntradaStock = idEntradaStock;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }
}

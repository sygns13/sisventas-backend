package com.bcs.ventas.utils.beans;

public class AgregarProductoBean {

    private Long idVenta;


    private String codigoUnidad;

    public AgregarProductoBean() {
    }

    public AgregarProductoBean(Long idVenta, String codigoUnidad) {
        this.idVenta = idVenta;
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
}

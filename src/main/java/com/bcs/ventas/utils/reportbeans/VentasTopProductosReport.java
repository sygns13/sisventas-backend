package com.bcs.ventas.utils.reportbeans;

import java.math.BigDecimal;

public class VentasTopProductosReport {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    private Long nro;
    private String tipoProducto;
    private String marca;
    private String presentacion;
    private String producto;
    private BigDecimal unidadesVendidas;
    private BigDecimal numeroVentas;
    private BigDecimal importe;

    public VentasTopProductosReport() {
    }

    public Long getNro() {
        return nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public BigDecimal getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public void setUnidadesVendidas(BigDecimal unidadesVendidas) {
        this.unidadesVendidas = unidadesVendidas;
    }

    public BigDecimal getNumeroVentas() {
        return numeroVentas;
    }

    public void setNumeroVentas(BigDecimal numeroVentas) {
        this.numeroVentas = numeroVentas;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
}

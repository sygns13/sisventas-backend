package com.bcs.ventas.utils.reportbeans;

import java.math.BigDecimal;

public class ProductosInventarioReport {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    private Long nro;
    private String codigo;
    private String producto;
    private String tipoProducto;
    private String marca;
    private String presentacion;
    private String composicion;
    private Double stockActual;
    private Double stockMinimo;
    private BigDecimal precioVenta;
    private BigDecimal costoCompra;
    private String prioridad;
    private String ubicacion;
    private String igv;
    private String isc;
    private String lotes;

    public ProductosInventarioReport() {
    }

    public Long getNro() {
        return nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
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

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }

    public Double getStockActual() {
        return stockActual;
    }

    public void setStockActual(Double stockActual) {
        this.stockActual = stockActual;
    }

    public Double getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public BigDecimal getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(BigDecimal costoCompra) {
        this.costoCompra = costoCompra;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getIgv() {
        return igv;
    }

    public void setIgv(String igv) {
        this.igv = igv;
    }

    public String getIsc() {
        return isc;
    }

    public void setIsc(String isc) {
        this.isc = isc;
    }

    public String getLotes() {
        return lotes;
    }

    public void setLotes(String lotes) {
        this.lotes = lotes;
    }
}

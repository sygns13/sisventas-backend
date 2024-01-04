package com.bcs.ventas.utils.reportbeans;

import java.math.BigDecimal;

public class CuentasPagarGeneralReport {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    private Long nro;
    private String sucursal;
    private String numeroCompra;
    private String fecha;
    private String hora;
    private String estado;
    private String facturada;
    private String actualizada;
    private String comprobante;
    private String proveedor;
    private BigDecimal importeTotal;
    private BigDecimal importeCobrado;
    private BigDecimal importePendiente;
    private String usuario;

    public CuentasPagarGeneralReport() {
    }

    public Long getNro() {
        return nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(String numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFacturada() {
        return facturada;
    }

    public void setFacturada(String facturada) {
        this.facturada = facturada;
    }

    public String getActualizada() {
        return actualizada;
    }

    public void setActualizada(String actualizada) {
        this.actualizada = actualizada;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public BigDecimal getImporteCobrado() {
        return importeCobrado;
    }

    public void setImporteCobrado(BigDecimal importeCobrado) {
        this.importeCobrado = importeCobrado;
    }

    public BigDecimal getImportePendiente() {
        return importePendiente;
    }

    public void setImportePendiente(BigDecimal importePendiente) {
        this.importePendiente = importePendiente;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}

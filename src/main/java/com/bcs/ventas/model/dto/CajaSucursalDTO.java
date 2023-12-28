package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.Almacen;

import java.math.BigDecimal;

public class CajaSucursalDTO {

    private BigDecimal cajaInicial;
    private BigDecimal cajaTotal;

    private BigDecimal ingresosVentas;
    private BigDecimal ingresosOtros;

    private BigDecimal ingresosTotal;

    private BigDecimal egresosCompras;
    private BigDecimal egresosOtros;

    private BigDecimal egresosTotal;

    private Almacen almacen;

    public CajaSucursalDTO() {
    }

    public BigDecimal getCajaInicial() {
        return cajaInicial;
    }

    public void setCajaInicial(BigDecimal cajaInicial) {
        this.cajaInicial = cajaInicial;
    }

    public BigDecimal getCajaTotal() {
        return cajaTotal;
    }

    public void setCajaTotal(BigDecimal cajaTotal) {
        this.cajaTotal = cajaTotal;
    }

    public BigDecimal getIngresosVentas() {
        return ingresosVentas;
    }

    public void setIngresosVentas(BigDecimal ingresosVentas) {
        this.ingresosVentas = ingresosVentas;
    }

    public BigDecimal getIngresosOtros() {
        return ingresosOtros;
    }

    public void setIngresosOtros(BigDecimal ingresosOtros) {
        this.ingresosOtros = ingresosOtros;
    }

    public BigDecimal getIngresosTotal() {
        return ingresosTotal;
    }

    public void setIngresosTotal(BigDecimal ingresosTotal) {
        this.ingresosTotal = ingresosTotal;
    }

    public BigDecimal getEgresosCompras() {
        return egresosCompras;
    }

    public void setEgresosCompras(BigDecimal egresosCompras) {
        this.egresosCompras = egresosCompras;
    }

    public BigDecimal getEgresosOtros() {
        return egresosOtros;
    }

    public void setEgresosOtros(BigDecimal egresosOtros) {
        this.egresosOtros = egresosOtros;
    }

    public BigDecimal getEgresosTotal() {
        return egresosTotal;
    }

    public void setEgresosTotal(BigDecimal egresosTotal) {
        this.egresosTotal = egresosTotal;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }
}

package com.bcs.ventas.model.dto;

import java.math.BigDecimal;

public class CajaInicioCierreDto {

    private Long idCaja;
    private BigDecimal monto;

    private String sustento;

    public CajaInicioCierreDto() {
    }

    public Long getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Long idCaja) {
        this.idCaja = idCaja;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getSustento() {
        return sustento;
    }

    public void setSustento(String sustento) {
        this.sustento = sustento;
    }
}

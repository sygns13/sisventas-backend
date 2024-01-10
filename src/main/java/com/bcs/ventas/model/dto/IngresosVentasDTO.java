package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.Venta;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public class IngresosVentasDTO {

    Page<Venta> ventas;

    BigDecimal montoTotal;

    public IngresosVentasDTO() {
    }

    public Page<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(Page<Venta> ventas) {
        this.ventas = ventas;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }
}

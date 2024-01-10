package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public class IngresosOtrosDTO {

    Page<IngresoSalidaCaja> ingresos;

    BigDecimal montoTotal;

    public IngresosOtrosDTO() {
    }

    public Page<IngresoSalidaCaja> getIngresos() {
        return ingresos;
    }

    public void setIngresos(Page<IngresoSalidaCaja> ingresos) {
        this.ingresos = ingresos;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }
}

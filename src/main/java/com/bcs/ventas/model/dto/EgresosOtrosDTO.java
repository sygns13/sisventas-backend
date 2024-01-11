package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public class EgresosOtrosDTO {

    Page<IngresoSalidaCaja> egresos;

    BigDecimal montoTotal;

    public EgresosOtrosDTO() {
    }

    public Page<IngresoSalidaCaja> getEgresos() {
        return egresos;
    }

    public void setEgresos(Page<IngresoSalidaCaja> egresos) {
        this.egresos = egresos;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }
}

package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.EntradaStock;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public class EgresosComprasDTO {

    Page<EntradaStock> compras;

    BigDecimal montoTotal;

    public EgresosComprasDTO() {
    }

    public Page<EntradaStock> getCompras() {
        return compras;
    }

    public void setCompras(Page<EntradaStock> compras) {
        this.compras = compras;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }
}

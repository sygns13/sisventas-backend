package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.DetalleEntradaStock;
import com.bcs.ventas.model.entities.EntradaStock;

public class ComprasDetallesDTO {

    private EntradaStock entradaStock;

    private DetalleEntradaStock detalleEntradaStock;

    public ComprasDetallesDTO() {
    }

    public EntradaStock getEntradaStock() {
        return entradaStock;
    }

    public void setEntradaStock(EntradaStock entradaStock) {
        this.entradaStock = entradaStock;
    }

    public DetalleEntradaStock getDetalleEntradaStock() {
        return detalleEntradaStock;
    }

    public void setDetalleEntradaStock(DetalleEntradaStock detalleEntradaStock) {
        this.detalleEntradaStock = detalleEntradaStock;
    }
}

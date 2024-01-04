package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.EntradaStock;
import com.bcs.ventas.model.entities.PagoProveedor;

public class CuentasPagarDetallesDTO {

    private EntradaStock entradaStock;
    PagoProveedor pagoProveedor;

    public CuentasPagarDetallesDTO() {
    }

    public EntradaStock getEntradaStock() {
        return entradaStock;
    }

    public void setEntradaStock(EntradaStock entradaStock) {
        this.entradaStock = entradaStock;
    }

    public PagoProveedor getPagoProveedor() {
        return pagoProveedor;
    }

    public void setPagoProveedor(PagoProveedor pagoProveedor) {
        this.pagoProveedor = pagoProveedor;
    }
}

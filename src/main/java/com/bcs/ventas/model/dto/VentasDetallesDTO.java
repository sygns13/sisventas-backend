package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.DetalleVenta;
import com.bcs.ventas.model.entities.Venta;

public class VentasDetallesDTO {

    Venta venta;
    DetalleVenta detalleVenta;

    public VentasDetallesDTO() {
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public DetalleVenta getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(DetalleVenta detalleVenta) {
        this.detalleVenta = detalleVenta;
    }
}

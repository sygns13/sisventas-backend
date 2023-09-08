package com.bcs.ventas.model.dto;

import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Lote;

public class LotesChangeOrdenDTO {

    private Lote lote1;

    private Lote lote2;

    public LotesChangeOrdenDTO() {
    }

    public LotesChangeOrdenDTO(Lote lote1, Lote lote2) {
        this.lote1 = lote1;
        this.lote2 = lote2;
    }

    public Lote getLote1() {
        return lote1;
    }

    public void setLote1(Lote lote1) {
        this.lote1 = lote1;
    }

    public Lote getLote2() {
        return lote2;
    }

    public void setLote2(Lote lote2) {
        this.lote2 = lote2;
    }
}

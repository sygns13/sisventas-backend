package com.bcs.ventas.service;

import com.bcs.ventas.model.dto.LotesChangeOrdenDTO;
import com.bcs.ventas.model.entities.Lote;
import com.bcs.ventas.model.entities.Stock;

public interface LoteService extends GeneralService<Lote, Long> {

    Lote registrarNuevoLote(Lote lote) throws Exception;

    void modificarOrden(LotesChangeOrdenDTO lotes) throws Exception;
}

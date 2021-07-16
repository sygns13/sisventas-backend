package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.TipoDocumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoDocumentoService extends GeneralService<TipoDocumento, Long> {

    Page<TipoDocumento> listar(Pageable pageable, String buscar) throws Exception;
}

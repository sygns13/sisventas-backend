package com.bcs.ventas.dao.impl;

import com.bcs.ventas.dao.TipoDocumentoDAO;
import com.bcs.ventas.dao.repo.TipoDocumentoRepo;
import com.bcs.ventas.dao.repo.GenericRepo;
import com.bcs.ventas.model.entities.TipoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TipoDocumentoDAOImpl extends CRUDDAOImpl<TipoDocumento, Long> implements TipoDocumentoDAO {

    @Autowired
    private TipoDocumentoRepo repo;

    @Override
    protected GenericRepo<TipoDocumento, Long> getRepo() {
        return repo;
    }

}

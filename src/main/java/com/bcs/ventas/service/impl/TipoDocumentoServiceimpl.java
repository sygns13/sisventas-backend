package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.TipoDocumentoDAO;
import com.bcs.ventas.dao.mappers.TipoDocumentoMapper;
import com.bcs.ventas.model.entities.TipoDocumento;
import com.bcs.ventas.model.entities.TipoProducto;
import com.bcs.ventas.service.TipoDocumentoService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class TipoDocumentoServiceimpl implements TipoDocumentoService {

    @Autowired
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Autowired
    private TipoDocumentoMapper tipoDocumentoMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public TipoDocumento registrar(TipoDocumento tipoDocumento) throws Exception {
        return null;
    }

    @Override
    public int modificar(TipoDocumento tipoDocumento) throws Exception {
        return 0;
    }

    @Override
    public List<TipoDocumento> listar() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);

        return tipoDocumentoMapper.listByParameterMap(params);
    }

    @Override
    public TipoDocumento listarPorId(Long aLong) throws Exception {
        return null;
    }

    @Override
    public void eliminar(Long aLong) throws Exception {

    }

    @Override
    public TipoDocumento grabarRegistro(TipoDocumento tipoDocumento) throws Exception {
        return null;
    }

    @Override
    public int grabarRectificar(TipoDocumento tipoDocumento) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long aLong) throws Exception {

    }

    @Override
    public boolean validacionRegistro(TipoDocumento tipoDocumento, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionModificado(TipoDocumento tipoDocumento, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long aLong, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public Page<TipoDocumento> listar(Pageable pageable, String buscar) throws Exception {
        return null;
        /*
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("BUSCAR","%"+buscar+"%");

        int total = tipoDocumentoMapper.getTotalElements(params);
        int totalPages = (int) Math.ceil( ((double)total) / page.getPageSize());
        int offset = page.getPageSize()*(page.getPageNumber());

        params.put("LIMIT", page.getPageSize());
        params.put("OFFSET", offset);

        List<TipoDocumento> tipoProductos = tipoDocumentoMapper.listByParameterMap(params);

        return new PageImpl<>(tipoProductos, page, total);*/
    }
}

package com.bcs.ventas.service.impl;

import com.bcs.ventas.model.entities.FacturaProveedor;
import com.bcs.ventas.service.FacturaProveedorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class FacturaProveedorServiceImpl implements FacturaProveedorService {
    @Override
    public void altabaja(Long id, Integer valor) throws Exception {

    }

    @Override
    public Page<FacturaProveedor> listar(Pageable pageable, String buscar) throws Exception {
        return null;
    }

    @Override
    public FacturaProveedor registrar(FacturaProveedor facturaProveedor) throws Exception {
        return null;
    }

    @Override
    public int modificar(FacturaProveedor facturaProveedor) throws Exception {
        return 0;
    }

    @Override
    public List<FacturaProveedor> listar() throws Exception {
        return null;
    }

    @Override
    public FacturaProveedor listarPorId(Long aLong) throws Exception {
        return null;
    }

    @Override
    public void eliminar(Long aLong) throws Exception {

    }

    @Override
    public FacturaProveedor grabarRegistro(FacturaProveedor facturaProveedor) throws Exception {
        return null;
    }

    @Override
    public int grabarRectificar(FacturaProveedor facturaProveedor) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long aLong) throws Exception {

    }

    @Override
    public boolean validacionRegistro(FacturaProveedor facturaProveedor, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionModificado(FacturaProveedor facturaProveedor, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long aLong, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }
}

package com.bcs.ventas.service.impl;

import com.bcs.ventas.model.entities.Venta;
import com.bcs.ventas.service.VentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class VentaServiceImpl implements VentaService {
    @Override
    public Venta registrar(Venta venta) throws Exception {
        return null;
    }

    @Override
    public int modificar(Venta venta) throws Exception {
        return 0;
    }

    @Override
    public List<Venta> listar() throws Exception {
        return null;
    }

    @Override
    public Venta listarPorId(Long aLong) throws Exception {
        return null;
    }

    @Override
    public void eliminar(Long aLong) throws Exception {

    }

    @Override
    public Venta grabarRegistro(Venta venta) throws Exception {
        return null;
    }

    @Override
    public int grabarRectificar(Venta venta) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long aLong) throws Exception {

    }

    @Override
    public boolean validacionRegistro(Venta venta, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionModificado(Venta venta, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long aLong, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }
}

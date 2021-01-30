package com.bcs.ventas.service.impl;

import com.bcs.ventas.dao.AlmacenDAO;
import com.bcs.ventas.dao.mappers.AlmacenMapper;
import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.service.AlmacenService;
import com.bcs.ventas.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlmacenServiceImpl implements AlmacenService {

    @Autowired
    private AlmacenDAO almacenDAO;

    @Autowired
    private AlmacenMapper almacenMapper;


    @Override
    public Almacen registrar(Almacen a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setCreatedAt(fechaActual);
        a.setUpdatedAd(fechaActual);

        a.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        a.setActivo(Constantes.REGISTRO_ACTIVO);

        if(a.getNombre() == null)    a.setNombre("");
        if(a.getDireccion() == null) a.setDireccion("");
        if(a.getCodigo() == null)    a.setCodigo("");

        a.setNombre(a.getNombre().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setCodigo(a.getCodigo().trim());


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        List<Almacen> almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            throw new ModeloNotFoundException("El nombre del local ingresado ya se encuentra registrado");
        }
        params.clear();

        params.put("CODIGO",a.getCodigo());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            throw new ModeloNotFoundException("El código del local ingresado ya se encuentra registrado");
        }

        return almacenDAO.registrar(a);
    }

    @Override
    public Almacen modificar(Almacen a) throws Exception {
        //Date fechaActual = new Date();
        LocalDateTime fechaActual = LocalDateTime.now();
        a.setUpdatedAd(fechaActual);

        a.setNombre(a.getNombre().trim());
        a.setDireccion(a.getDireccion().trim());
        a.setCodigo(a.getCodigo().trim());


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_ID",a.getId());
        params.put("NOMBRE",a.getNombre());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);


        List<Almacen> almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            throw new ModeloNotFoundException("El nombre del local ingresado ya se encuentra registrado");
        }
        params.clear();

        params.put("NO_ID",a.getId());
        params.put("CODIGO",a.getCodigo());
        params.put("EMPRESA_ID",a.getEmpresaId());
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);

        almacensV1 = almacenMapper.listByParameterMap(params);

        if(almacensV1.size() > 0){
            throw new ModeloNotFoundException("El código del local ingresado ya se encuentra registrado");
        }

        return almacenDAO.modificar(a);
    }

    public List<Almacen> listar() throws Exception {
        //return almacenMapper.getAllEntities();
        return almacenDAO.listar();
    }

    @Override
    public Almacen listarPorId(Long id) throws Exception {
       // Map<String, Object> params = new HashMap<String, Object>();
        //params.put("ID",id);
        //return almacenMapper.listByParameterMap(params).get(0);
        return almacenDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {
       // almacenDAO.eliminar(id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ID",id);
        params.put("BORRADO",Constantes.REGISTRO_BORRADO);

        int res= almacenMapper.updateByPrimaryKeySelective(params);

        if(res == 0){
            throw new ModeloNotFoundException("No se pudo eliminar el Local indicado, por favorp robar nuevamente o comunicarse con un Administrador del Sistema");
        }

    }
}

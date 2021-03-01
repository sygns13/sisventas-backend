package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.Almacen;

import java.util.List;
import java.util.Map;

public interface GeneralService<T, ID> {

    T registrar(T t) throws Exception;

    int modificar(T t) throws Exception;

    List<T> listar() throws Exception;

    T listarPorId(ID id) throws Exception;

    void eliminar(ID id) throws Exception;


    //Metodos Complementarios
    T grabarRegistro(T t) throws Exception;

    int grabarRectificar(T t) throws Exception;

    void grabarEliminar(ID id);

    boolean validacionRegistro(T t, Map<String, Object> resultValidacion);

    boolean validacionModificado(T t, Map<String, Object> resultValidacion) throws Exception;

    boolean validacionEliminacion(ID id, Map<String, Object> resultValidacion) throws Exception;



}

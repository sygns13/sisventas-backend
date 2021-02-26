package com.bcs.ventas.service;


import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Departamento;
import com.bcs.ventas.model.entities.Distrito;
import com.bcs.ventas.model.entities.Provincia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface AlmacenService extends GeneralService<Almacen, Long> {

    List<Departamento> getDepartamentos(Long idPais) throws Exception;

    List<Provincia> getProvincias(Long idDep) throws Exception;

    List<Distrito> getDistritos(Long idProv) throws Exception;

    void altabaja(Long id, Integer valor) throws Exception;

    Page<Almacen> listar(Pageable pageable, String buscar) throws Exception;

}

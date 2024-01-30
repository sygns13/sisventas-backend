package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.Caja;
import com.bcs.ventas.model.entities.CajaUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CajaService extends GeneralService<Caja, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<Caja> listar(Pageable pageable, String buscar, Long idAlmacen) throws Exception;
    List<Caja> AllByAlmacen(String buscar, Long idAlmacen) throws Exception;
    List<CajaUser> AllByAlmacenAndUsers(String buscar, Long idAlmacen, Long idUser) throws Exception;

    CajaUser AsignarCaja(CajaUser a) throws Exception;

    CajaUser EliminarAsignacionCaja(CajaUser a) throws Exception;

}

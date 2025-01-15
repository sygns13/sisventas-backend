package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CajaService extends GeneralService<Caja, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<Caja> listar(Pageable pageable, String buscar, Long idAlmacen) throws Exception;
    List<Caja> AllByAlmacen(String buscar, Long idAlmacen) throws Exception;
    List<CajaUser> AllByAlmacenAndUsers(String buscar, Long idAlmacen, Long idUser) throws Exception;
    List<CajaUser> CajasByAlmacenUser(String buscar, Long idAlmacen, Long idUser) throws Exception;

    CajaUser AsignarCaja(CajaUser a) throws Exception;

    CajaUser EliminarAsignacionCaja(CajaUser a) throws Exception;

    CajaDato IniciarCaja(Long idCaja, Long idUser, BigDecimal monto, String sustento) throws Exception;

    CajaDato CerrarCaja(Long idCaja, Long idUser, BigDecimal monto) throws Exception;

    CajaDato getCajaIniciadaByUserSession() throws Exception;

    CajaAccion ingresoSalidaCaja(Long userId, CajaDato cajaDato, BigDecimal monto, Integer type, String descripcion) throws Exception;

    CajaAccion registrarCajaAccion(CajaDato cajaDato, CajaAccion a) throws Exception;

    CajaDato getLastCajaCerrada(Long idCaja) throws Exception;

}

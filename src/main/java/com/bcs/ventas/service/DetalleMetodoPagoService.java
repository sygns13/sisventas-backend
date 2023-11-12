package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.DetalleMetodoPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DetalleMetodoPagoService extends GeneralService<DetalleMetodoPago, Long> {

    void altabaja(Long id, Integer valor, Long userId) throws Exception;

    Page<DetalleMetodoPago> listar(Pageable pageable, String buscar, Long metodoPagoId , Long bancoId) throws Exception;

    List<DetalleMetodoPago> listar(Long metodoPagoId , Long bancoId) throws Exception;
}

package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService extends GeneralService<User, Long> {

    Page<User> listar(Pageable pageable, String buscar) throws Exception;

    void altabaja(Long id, Integer valor) throws Exception;

    User getByDocument(String document) throws Exception;
}

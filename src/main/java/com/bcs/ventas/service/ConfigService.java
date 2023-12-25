package com.bcs.ventas.service;

import com.bcs.ventas.model.entities.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConfigService extends GeneralService<Config, String>{

    void altabaja(String id, Integer valor) throws Exception;

    Page<Config> listar(Pageable pageable, String buscar) throws Exception;

    int modificarList(Config[] configs) throws Exception;
}

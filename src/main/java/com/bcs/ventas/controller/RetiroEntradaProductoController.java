package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;

import com.bcs.ventas.model.dto.RetiroEntradaProductoDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.RetiroEntradaProductoService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/retiroentradaproducto")
public class RetiroEntradaProductoController {

    @Autowired
    RetiroEntradaProductoService retiroEntradaProductoService;

    @PostMapping
    public ResponseEntity<RetiroEntradaProducto> registrar(@Valid @RequestBody RetiroEntradaProducto a) throws Exception{
        a.setId(null);
        RetiroEntradaProducto obj = retiroEntradaProductoService.registrar(a);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PostMapping("/listar")
    public ResponseEntity<Page<RetiroEntradaProductoDTO>> getMovimientosLibresProductos(@RequestBody FiltroGeneral filtros) throws Exception{

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());
        Page<RetiroEntradaProductoDTO> movimientosProductos = retiroEntradaProductoService.getMovimientosLibresProductos(pageable, filtros);

        return new ResponseEntity<>(movimientosProductos, HttpStatus.OK);
    }
}

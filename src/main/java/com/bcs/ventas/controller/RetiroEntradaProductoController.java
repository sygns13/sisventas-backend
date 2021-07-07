package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.DetalleUnidadProductoService;
import com.bcs.ventas.service.RetiroEntradaProductoService;
import org.springframework.beans.factory.annotation.Autowired;
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
}

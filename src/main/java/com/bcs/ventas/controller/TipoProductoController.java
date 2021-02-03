package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.TipoProducto;
import com.bcs.ventas.service.TipoProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tipoproducto")
public class TipoProductoController {

    @Autowired
    private TipoProductoService tipoProductoService;

    @GetMapping
    public ResponseEntity<List<TipoProducto>> listar() throws Exception{
        List<TipoProducto> obj = tipoProductoService.listar();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoProducto> listarPorId(@PathVariable("id") Long id) throws Exception{
        TipoProducto obj = tipoProductoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<TipoProducto>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TipoProducto> registrar(@Valid @RequestBody TipoProducto a) throws Exception{
        TipoProducto obj = tipoProductoService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<TipoProducto> modificar(@Valid @RequestBody TipoProducto a) throws Exception{
        TipoProducto obj = tipoProductoService.modificar(a);

        return new ResponseEntity<TipoProducto>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        TipoProducto obj = tipoProductoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        tipoProductoService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}

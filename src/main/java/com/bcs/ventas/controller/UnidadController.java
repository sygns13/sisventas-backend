package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Unidad;
import com.bcs.ventas.service.UnidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/unidad")
public class UnidadController {

    @Autowired
    private UnidadService unidadService;

    @GetMapping
    public ResponseEntity<List<Unidad>> listar() throws Exception{
        List<Unidad> obj = unidadService.listar();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unidad> listarPorId(@PathVariable("id") Long id) throws Exception{
        Unidad obj = unidadService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Unidad>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Unidad> registrar(@Valid @RequestBody Unidad a) throws Exception{
        Unidad obj = unidadService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Unidad> modificar(@Valid @RequestBody Unidad a) throws Exception{
        Unidad obj = unidadService.modificar(a);

        return new ResponseEntity<Unidad>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        Unidad obj = unidadService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        unidadService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}

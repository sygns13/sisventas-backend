package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.service.AlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.net.URI;

@RestController
@RequestMapping("/almacen")
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    @GetMapping
    public ResponseEntity<List<Almacen>> listar() throws Exception{
        List<Almacen> obj = almacenService.listar();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Almacen> listarPorId(@PathVariable("id") Long id) throws Exception{
        Almacen obj = almacenService.listarPorId(id);

        if(obj.getId()==null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Almacen>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Almacen> registrar(@Valid @RequestBody Almacen a) throws Exception{
        Almacen obj = almacenService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Almacen> modificar(@Valid @RequestBody Almacen a) throws Exception{
        Almacen obj = almacenService.modificar(a);

        return new ResponseEntity<Almacen>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        Almacen obj = almacenService.listarPorId(id);

        if(obj.getId()==null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        almacenService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}

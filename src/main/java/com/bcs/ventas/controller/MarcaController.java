package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Marca;
import com.bcs.ventas.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/marca")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<Marca>> listar() throws Exception{
        List<Marca> obj = marcaService.listar();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> listarPorId(@PathVariable("id") Long id) throws Exception{
        Marca obj = marcaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Marca>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Marca> registrar(@Valid @RequestBody Marca a) throws Exception{
        Marca obj = marcaService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Marca> modificar(@Valid @RequestBody Marca a) throws Exception{
        Marca obj = marcaService.modificar(a);

        return new ResponseEntity<Marca>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        Marca obj = marcaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        marcaService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}

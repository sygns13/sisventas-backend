package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.TipoTarjeta;
import com.bcs.ventas.service.TipoTarjetaService;
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
@RequestMapping("/tipotarjetas")
public class TipoTarjetaController {

    @Autowired
    private TipoTarjetaService tipoTarjetaService;

    @GetMapping
    public ResponseEntity<Page<TipoTarjeta>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "5") int size,
                                                     @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{
        Pageable pageable = PageRequest.of(page,size);
        Page<TipoTarjeta> resultado = tipoTarjetaService.listar(pageable, buscar);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<TipoTarjeta>> listarAll() throws Exception{
        List<TipoTarjeta> resultado = tipoTarjetaService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoTarjeta> listarPorId(@PathVariable("id") Long id) throws Exception{
        TipoTarjeta obj = tipoTarjetaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<TipoTarjeta>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TipoTarjeta> registrar(@Valid @RequestBody TipoTarjeta a) throws Exception{
        a.setId(null);
        TipoTarjeta obj = tipoTarjetaService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@Valid @RequestBody TipoTarjeta a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        TipoTarjeta objBD = tipoTarjetaService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = tipoTarjetaService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        TipoTarjeta obj = tipoTarjetaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        tipoTarjetaService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{
        TipoTarjeta obj = tipoTarjetaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        tipoTarjetaService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}

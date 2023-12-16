package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.service.IngresoSalidaCajaService;
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
@RequestMapping("/ingreso_salida_cajas")
public class IngresoSalidaCajaController {

    @Autowired
    private IngresoSalidaCajaService ingresoSalidaCajaService;

    @GetMapping
    public ResponseEntity<Page<IngresoSalidaCaja>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "5") int size,
                                              @RequestParam(name = "buscar", defaultValue = "") String buscar,
                                              @RequestParam(name = "almacen_id", defaultValue = "") Long almacen_id) throws Exception{

        Pageable pageable = PageRequest.of(page,size);
        Page<IngresoSalidaCaja> resultado = ingresoSalidaCajaService.listar(pageable, buscar, almacen_id);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngresoSalidaCaja> listarPorId(@PathVariable("id") Long id) throws Exception{
        IngresoSalidaCaja obj = ingresoSalidaCajaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<IngresoSalidaCaja>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<IngresoSalidaCaja> registrar(@Valid @RequestBody IngresoSalidaCaja a) throws Exception{
        a.setId(null);
        IngresoSalidaCaja obj = ingresoSalidaCajaService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@Valid @RequestBody IngresoSalidaCaja a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        IngresoSalidaCaja objBD = ingresoSalidaCajaService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = ingresoSalidaCajaService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        IngresoSalidaCaja obj = ingresoSalidaCajaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        ingresoSalidaCajaService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<IngresoSalidaCaja>> listarAll() throws Exception{
        List<IngresoSalidaCaja> resultado = ingresoSalidaCajaService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

}
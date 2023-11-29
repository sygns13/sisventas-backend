package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.PagoProveedor;
import com.bcs.ventas.service.PagoProveedorService;
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
@RequestMapping("/pago-proveedor")
public class PagoProveedorController {

    @Autowired
    private PagoProveedorService pagoProveedorService;

    @GetMapping
    public ResponseEntity<Page<PagoProveedor>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "5") int size,
                                                   @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        Pageable pageable = PageRequest.of(page,size);
        Page<PagoProveedor> resultado = pagoProveedorService.listar(pageable, buscar);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoProveedor> listarPorId(@PathVariable("id") Long id) throws Exception{
        PagoProveedor obj = pagoProveedorService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<PagoProveedor>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PagoProveedor> registrar(@Valid @RequestBody PagoProveedor a) throws Exception{
        a.setId(null);
        PagoProveedor obj = pagoProveedorService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@Valid @RequestBody PagoProveedor a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        PagoProveedor objBD = pagoProveedorService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = pagoProveedorService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        PagoProveedor obj = pagoProveedorService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        pagoProveedorService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{
        PagoProveedor obj = pagoProveedorService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        pagoProveedorService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<PagoProveedor>> listarAll() throws Exception{
        List<PagoProveedor> resultado = pagoProveedorService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}

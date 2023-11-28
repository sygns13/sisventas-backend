package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Proveedor;
import com.bcs.ventas.model.entities.TipoDocumento;
import com.bcs.ventas.service.ProveedorService;
import com.bcs.ventas.service.TipoDocumentoService;
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
@RequestMapping("/proveedors")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private TipoDocumentoService tipoDocumentoService;

    @GetMapping
    public ResponseEntity<Page<Proveedor>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "5") int size,
                                                @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        Pageable pageable = PageRequest.of(page,size);
        Page<Proveedor> resultado = proveedorService.listar(pageable, buscar);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> listarPorId(@PathVariable("id") Long id) throws Exception{
        Proveedor obj = proveedorService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Proveedor>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Proveedor> registrar(@Valid @RequestBody Proveedor a) throws Exception{
        a.setId(null);
        Proveedor obj = proveedorService.registrar(a);
        obj = proveedorService.listarPorId(obj.getId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        //return  ResponseEntity.created(location).build();
        return ResponseEntity.created(location)
                .body(obj);
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@Valid @RequestBody Proveedor a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        Proveedor objBD = proveedorService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = proveedorService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        Proveedor obj = proveedorService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        proveedorService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    /*
    @GetMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{
        Proveedor obj = proveedorService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        proveedorService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }*/

    //Otros Endpoints

    @GetMapping("/tipodocumentos")
    public ResponseEntity<List<TipoDocumento>> getTipoDocumentos() throws Exception{
        List<TipoDocumento> obj = tipoDocumentoService.listar();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/getbydoc/{document}")
    public ResponseEntity<Proveedor> listarPorDNI(@PathVariable("document") String document) throws Exception{
        Proveedor obj = proveedorService.getByDocument(document);

        if(obj == null) {
            throw new ModeloNotFoundException("DOCUMENTO DE IDENTIDAD " + document + " NO ENCONTRADO ");
        }

        return new ResponseEntity<Proveedor>(obj, HttpStatus.OK);
    }
}

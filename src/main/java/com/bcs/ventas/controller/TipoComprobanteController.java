package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Marca;
import com.bcs.ventas.model.entities.TipoComprobante;
import com.bcs.ventas.service.TipoComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/tipocomprobantes")
public class TipoComprobanteController {

    @Autowired
    private TipoComprobanteService tipoComprobanteService;

    @GetMapping
    public ResponseEntity<Page<TipoComprobante>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "5") int size,
                                                     @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{
        Pageable pageable = PageRequest.of(page,size);
        Page<TipoComprobante> resultado = tipoComprobanteService.listar(pageable, buscar);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<TipoComprobante>> listarAll() throws Exception{
        List<TipoComprobante> resultado = tipoComprobanteService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoComprobante> listarPorId(@PathVariable("id") Long id) throws Exception{
        TipoComprobante obj = tipoComprobanteService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<TipoComprobante>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TipoComprobante> registrar(@Valid @RequestBody TipoComprobante a) throws Exception{
        a.setId(null);
        TipoComprobante obj = tipoComprobanteService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@Valid @RequestBody TipoComprobante a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        TipoComprobante objBD = tipoComprobanteService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = tipoComprobanteService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        TipoComprobante obj = tipoComprobanteService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        tipoComprobanteService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{
        TipoComprobante obj = tipoComprobanteService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        tipoComprobanteService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}

package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.InitComprobante;
import com.bcs.ventas.model.entities.TipoTarjeta;
import com.bcs.ventas.service.InitComprobanteService;
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
@RequestMapping("/init-comprobante")
public class InitComprobanteController {

    @Autowired
    private InitComprobanteService initComprobanteService;

    @GetMapping
    public ResponseEntity<Page<InitComprobante>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                                        @RequestParam(name = "tipo_comprobante_id", defaultValue = "0") Long tipoComprobante,
                                                        @RequestParam(name = "almacen_id", defaultValue = "-1") Long almacenId,
                                                        @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        Pageable pageable = PageRequest.of(page,size);
        Page<InitComprobante> resultado = initComprobanteService.listar(pageable, buscar, tipoComprobante, almacenId);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<InitComprobante>> listarAll( @RequestParam(name = "tipo_comprobante_id", defaultValue = "0") Long tipoComprobante,
                                                            @RequestParam(name = "almacen_id", defaultValue = "0") Long almacenId) throws Exception{
        List<InitComprobante> resultado = initComprobanteService.listar(tipoComprobante, almacenId);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InitComprobante> listarPorId(@PathVariable("id") Long id) throws Exception{
        InitComprobante obj = initComprobanteService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<InitComprobante>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InitComprobante> registrar(@Valid @RequestBody InitComprobante a) throws Exception{
        a.setId(null);
        InitComprobante obj = initComprobanteService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@Valid @RequestBody InitComprobante a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        InitComprobante objBD = initComprobanteService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = initComprobanteService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        InitComprobante obj = initComprobanteService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        initComprobanteService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{
        InitComprobante obj = initComprobanteService.listarPorId(id);

        Long userId = 2L;

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        initComprobanteService.altabaja(id, valor, userId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

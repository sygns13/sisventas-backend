package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.DetalleMetodoPago;
import com.bcs.ventas.model.entities.InitComprobante;
import com.bcs.ventas.service.DetalleMetodoPagoService;
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
@RequestMapping("/detalle-metodo-pago")
public class DetalleMetodoPagoController {

    @Autowired
    private DetalleMetodoPagoService detalleMetodoPagoService;

    @GetMapping
    public ResponseEntity<Page<DetalleMetodoPago>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                                        @RequestParam(name = "metodos_pago_id", defaultValue = "0") Long metodoPagoId,
                                                        @RequestParam(name = "banco_id", defaultValue = "0") Long bancoId,
                                                        @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        Pageable pageable = PageRequest.of(page,size);
        Page<DetalleMetodoPago> resultado = detalleMetodoPagoService.listar(pageable, buscar, metodoPagoId, bancoId);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<DetalleMetodoPago>> listarAll() throws Exception{
        List<DetalleMetodoPago> resultado = detalleMetodoPagoService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleMetodoPago> listarPorId(@PathVariable("id") Long id) throws Exception{
        DetalleMetodoPago obj = detalleMetodoPagoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<DetalleMetodoPago>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DetalleMetodoPago> registrar(@Valid @RequestBody DetalleMetodoPago a) throws Exception{
        a.setId(null);
        DetalleMetodoPago obj = detalleMetodoPagoService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@Valid @RequestBody DetalleMetodoPago a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        DetalleMetodoPago objBD = detalleMetodoPagoService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = detalleMetodoPagoService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        DetalleMetodoPago obj = detalleMetodoPagoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        detalleMetodoPagoService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{
        DetalleMetodoPago obj = detalleMetodoPagoService.listarPorId(id);

        Long userId = 2L;

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        detalleMetodoPagoService.altabaja(id, valor, userId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

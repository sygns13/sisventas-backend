package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Departamento;
import com.bcs.ventas.model.entities.DetalleUnidadProducto;
import com.bcs.ventas.model.entities.Distrito;
import com.bcs.ventas.service.DetalleUnidadProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/detalleunidadproducto")
public class DetalleUnidadProductoController {

    @Autowired
    private DetalleUnidadProductoService detalleUnidadProductoService;

    @GetMapping("/almacens")
    public ResponseEntity<List<Almacen>> getAlmacens() throws Exception{

        Long idEmpresa=1L;
        List<Almacen> obj = detalleUnidadProductoService.getAlmacens(idEmpresa);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/almacens-date")
    public ResponseEntity<Map<String, Object>> getAlmacensDate() throws Exception{

        Map<String, Object> resultado = new HashMap<>();
        Long idEmpresa=1L;
        List<Almacen> obj = detalleUnidadProductoService.getAlmacens(idEmpresa);

        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        String fecha = fechaActual.format(formatter);
        String hora = fechaActual.format(formatter2);

        resultado.put("almacens", obj);
        resultado.put("fecha", fecha);
        resultado.put("hora", hora);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }


    @GetMapping("/{idAlmacen}/{idProducto}")
    public ResponseEntity<List<DetalleUnidadProducto>> listar(@PathVariable("idAlmacen") Long idAlmacen,
                                                              @PathVariable("idProducto") Long idProducto) throws Exception{

        Long idEmpresa=1L;
        List<DetalleUnidadProducto> obj = detalleUnidadProductoService.listar(idAlmacen, idProducto, idEmpresa);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DetalleUnidadProducto> registrar(@Valid @RequestBody DetalleUnidadProducto a) throws Exception{

        DetalleUnidadProducto obj = detalleUnidadProductoService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        DetalleUnidadProducto obj = detalleUnidadProductoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        detalleUnidadProductoService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}

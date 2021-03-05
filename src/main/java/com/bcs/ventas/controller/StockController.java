package com.bcs.ventas.controller;

import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.DetalleUnidadProducto;
import com.bcs.ventas.model.entities.Stock;
import com.bcs.ventas.service.StockService;
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
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/almacens")
    public ResponseEntity<Map<String, Object>> getAlmacens() throws Exception{

        Map<String, Object> resultado = new HashMap<>();

        Long idEmpresa=1L;
        List<Almacen> obj = stockService.getAlmacens(idEmpresa);
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaIngreso = fechaActual.format(formatter);

        resultado.put("almacens", obj);
        resultado.put("fechaIngreso", fechaIngreso);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{idAlmacen}/{idProducto}")
    public ResponseEntity<Map<String, Object>> listar(@PathVariable("idAlmacen") Long idAlmacen,
                                                      @PathVariable("idProducto") Long idProducto) throws Exception{

        Long idEmpresa=1L;

        Map<String, Object> obj = stockService.listar(idAlmacen, idProducto, idEmpresa);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Stock> registrar(@Valid @RequestBody Stock a) throws Exception{

        Stock obj = stockService.registrar(a);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }
}

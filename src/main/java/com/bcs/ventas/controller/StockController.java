package com.bcs.ventas.controller;

import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.DetalleUnidadProducto;
import com.bcs.ventas.model.entities.Stock;
import com.bcs.ventas.service.StockService;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    private void SetClaims(String Authorization) throws Exception {
        String[] bearerAuth = Authorization.split(" ");
        Claims claims = jwtUtils.verify(bearerAuth[1]);

        claimsAuthorization.setClaims(claims);
        claimsAuthorization.setAuthorization(Authorization);
        claimsAuthorization.setUsername(claims.get("user_email").toString());

        if(claims.get("auth_level_3") != null)
            claimsAuthorization.setUserId(Long.parseLong(claims.get("auth_level_3").toString()));
        if(claims.get("auth_level_2") != null)
            claimsAuthorization.setEmpresaId(Long.parseLong(claims.get("auth_level_2").toString()));
    }

    @GetMapping("/almacens")
    public ResponseEntity<Map<String, Object>> getAlmacens(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{

        this.SetClaims(Authorization);

        Map<String, Object> resultado = new HashMap<>();

        Long idEmpresa = claimsAuthorization.getEmpresaId();
        List<Almacen> obj = stockService.getAlmacens(idEmpresa);
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaIngreso = fechaActual.format(formatter);

        resultado.put("almacens", obj);
        resultado.put("fechaIngreso", fechaIngreso);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{idAlmacen}/{idProducto}")
    public ResponseEntity<Map<String, Object>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @PathVariable("idAlmacen") Long idAlmacen,
                                                      @PathVariable("idProducto") Long idProducto) throws Exception{

        this.SetClaims(Authorization);

        Long idEmpresa = claimsAuthorization.getEmpresaId();

        Map<String, Object> obj = stockService.listar(idAlmacen, idProducto, idEmpresa);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/almacens-producto/{idProducto}")
    public ResponseEntity<Map<String, Object>> getAlmacensProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                   @PathVariable("idProducto") Long idProducto) throws Exception{

        this.SetClaims(Authorization);

        Map<String, Object> resultado = new HashMap<>();

        Long idEmpresa = claimsAuthorization.getEmpresaId();
        List<Almacen> obj = stockService.getAlmacensProducts(idEmpresa, idProducto);
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaIngreso = fechaActual.format(formatter);

        resultado.put("almacens", obj);
        resultado.put("fechaIngreso", fechaIngreso);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/almacen-productos/{idAlmacen}/{idProducto}")
    public ResponseEntity<Map<String, Object>> getAlmacenProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                  @PathVariable("idAlmacen") Long idAlmacen,
                                                                  @PathVariable("idProducto") Long idProducto) throws Exception{

        this.SetClaims(Authorization);

        Map<String, Object> resultado = new HashMap<>();

        Long idEmpresa = claimsAuthorization.getEmpresaId();
        resultado = stockService.getAlmacenProducts(idEmpresa, idAlmacen, idProducto);
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaIngreso = fechaActual.format(formatter);

        resultado.put("fechaIngreso", fechaIngreso);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/almacen-productos-lote/{idAlmacen}/{idProducto}/{idLote}")
    public ResponseEntity<Map<String, Object>> getAlmacenProductsAndLote(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                         @PathVariable("idAlmacen") Long idAlmacen,
                                                                         @PathVariable("idProducto") Long idProducto,
                                                                         @PathVariable("idLote") Long idLote) throws Exception{

        this.SetClaims(Authorization);

        Map<String, Object> resultado = new HashMap<>();

        Long idEmpresa = claimsAuthorization.getEmpresaId();
        resultado = stockService.getAlmacenProductsLote(idEmpresa, idAlmacen, idProducto, idLote);
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaIngreso = fechaActual.format(formatter);

        resultado.put("fechaIngreso", fechaIngreso);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Stock> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                           @Valid @RequestBody Stock a) throws Exception{

        this.SetClaims(Authorization);

        Stock obj = stockService.registrar(a);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }
}

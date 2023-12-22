package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Departamento;
import com.bcs.ventas.model.entities.DetalleUnidadProducto;
import com.bcs.ventas.model.entities.Distrito;
import com.bcs.ventas.service.DetalleUnidadProductoService;
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
@RequestMapping("/detalleunidadproducto")
public class DetalleUnidadProductoController {

    @Autowired
    private DetalleUnidadProductoService detalleUnidadProductoService;

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
    public ResponseEntity<List<Almacen>> getAlmacens(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{

        this.SetClaims(Authorization);

        Long idEmpresa = claimsAuthorization.getEmpresaId();
        List<Almacen> obj = detalleUnidadProductoService.getAlmacens(idEmpresa);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/almacens-date")
    public ResponseEntity<Map<String, Object>> getAlmacensDate(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{

        this.SetClaims(Authorization);

        Map<String, Object> resultado = new HashMap<>();
        Long idEmpresa = claimsAuthorization.getEmpresaId();
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
    public ResponseEntity<List<DetalleUnidadProducto>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                              @PathVariable("idAlmacen") Long idAlmacen,
                                                              @PathVariable("idProducto") Long idProducto) throws Exception{

        this.SetClaims(Authorization);

        Long idEmpresa = claimsAuthorization.getEmpresaId();
        List<DetalleUnidadProducto> obj = detalleUnidadProductoService.listar(idAlmacen, idProducto, idEmpresa);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DetalleUnidadProducto> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                           @Valid @RequestBody DetalleUnidadProducto a) throws Exception{

        this.SetClaims(Authorization);

        DetalleUnidadProducto obj = detalleUnidadProductoService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                         @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        DetalleUnidadProducto obj = detalleUnidadProductoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        detalleUnidadProductoService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}

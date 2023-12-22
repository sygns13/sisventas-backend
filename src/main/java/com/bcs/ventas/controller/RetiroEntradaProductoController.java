package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;

import com.bcs.ventas.model.dto.RetiroEntradaProductoDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.RetiroEntradaProductoService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/retiroentradaproducto")
public class RetiroEntradaProductoController {

    @Autowired
    RetiroEntradaProductoService retiroEntradaProductoService;

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

    @PostMapping
    public ResponseEntity<RetiroEntradaProducto> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                           @Valid @RequestBody RetiroEntradaProducto a) throws Exception{

        this.SetClaims(Authorization);

        a.setId(null);
        RetiroEntradaProducto obj = retiroEntradaProductoService.registrar(a);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PostMapping("/listar")
    public ResponseEntity<Page<RetiroEntradaProductoDTO>> getMovimientosLibresProductos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                                        @RequestBody FiltroGeneral filtros) throws Exception{

        this.SetClaims(Authorization);

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());
        Page<RetiroEntradaProductoDTO> movimientosProductos = retiroEntradaProductoService.getMovimientosLibresProductos(pageable, filtros);

        return new ResponseEntity<>(movimientosProductos, HttpStatus.OK);
    }
}

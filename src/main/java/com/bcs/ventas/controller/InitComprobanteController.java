package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.InitComprobante;
import com.bcs.ventas.model.entities.TipoTarjeta;
import com.bcs.ventas.service.InitComprobanteService;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
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
@RequestMapping("/init-comprobante")
public class InitComprobanteController {

    @Autowired
    private InitComprobanteService initComprobanteService;

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

    @GetMapping
    public ResponseEntity<Page<InitComprobante>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                                        @RequestParam(name = "tipo_comprobante_id", defaultValue = "0") Long tipoComprobante,
                                                        @RequestParam(name = "almacen_id", defaultValue = "-1") Long almacenId,
                                                        @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<InitComprobante> resultado = initComprobanteService.listar(pageable, buscar, tipoComprobante, almacenId);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<InitComprobante>> listarAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                           @RequestParam(name = "tipo_comprobante_id", defaultValue = "0") Long tipoComprobante,
                                                           @RequestParam(name = "almacen_id", defaultValue = "0") Long almacenId) throws Exception{

        this.SetClaims(Authorization);

        List<InitComprobante> resultado = initComprobanteService.listar(tipoComprobante, almacenId);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InitComprobante> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                       @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        InitComprobante obj = initComprobanteService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<InitComprobante>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InitComprobante> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                     @Valid @RequestBody InitComprobante a) throws Exception{

        this.SetClaims(Authorization);

        a.setId(null);
        InitComprobante obj = initComprobanteService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                             @Valid @RequestBody InitComprobante a) throws Exception{

        this.SetClaims(Authorization);

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
    public ResponseEntity<Void> eliminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                         @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        InitComprobante obj = initComprobanteService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        initComprobanteService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                         @PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{

        this.SetClaims(Authorization);

        InitComprobante obj = initComprobanteService.listarPorId(id);

        Long userId = claimsAuthorization.getUserId();

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        initComprobanteService.altabaja(id, valor, userId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

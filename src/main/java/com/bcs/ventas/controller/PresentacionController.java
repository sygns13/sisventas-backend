package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Presentacion;
import com.bcs.ventas.model.entities.TipoProducto;
import com.bcs.ventas.service.PresentacionService;
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
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/presentacions")
public class PresentacionController {

    @Autowired
    private PresentacionService presentacionService;

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
    public ResponseEntity<Page<Presentacion>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "5") int size,
                                                     @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<Presentacion> resultado = presentacionService.listar(pageable, buscar);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<Presentacion>> listarAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{

        this.SetClaims(Authorization);

        List<Presentacion> resultado = presentacionService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Presentacion> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                    @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Presentacion obj = presentacionService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Presentacion>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Presentacion> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                  @Valid @RequestBody Presentacion a) throws Exception{

        this.SetClaims(Authorization);

        a.setId(null);
        Presentacion obj = presentacionService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                             @Valid @RequestBody Presentacion a) throws Exception{

        this.SetClaims(Authorization);

        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        Presentacion objBD = presentacionService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = presentacionService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                         @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Presentacion obj = presentacionService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        presentacionService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                         @PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{

        this.SetClaims(Authorization);

        Presentacion obj = presentacionService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        presentacionService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}

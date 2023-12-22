package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Banco;
import com.bcs.ventas.model.entities.TipoTarjeta;
import com.bcs.ventas.service.BancoService;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bancos")
public class BancoController {

    @Autowired
    private BancoService bancoService;

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
    public ResponseEntity<Page<Banco>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "5") int size,
                                              @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        // @RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestHeader Map<String, String> headers
        this.SetClaims(Authorization);
        /*headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });*/

        Pageable pageable = PageRequest.of(page,size);
        Page<Banco> resultado = bancoService.listar(pageable, buscar);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banco> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Banco obj = bancoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Banco>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Banco> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @Valid @RequestBody Banco a) throws Exception{

        this.SetClaims(Authorization);

        a.setId(null);
        Banco obj = bancoService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @Valid @RequestBody Banco a) throws Exception{

        this.SetClaims(Authorization);

        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        Banco objBD = bancoService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = bancoService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Banco obj = bancoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        bancoService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{

        this.SetClaims(Authorization);

        Banco obj = bancoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        bancoService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<Banco>> listarAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{

        this.SetClaims(Authorization);

        List<Banco> resultado = bancoService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

}

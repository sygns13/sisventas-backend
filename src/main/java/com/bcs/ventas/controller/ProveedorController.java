package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Proveedor;
import com.bcs.ventas.model.entities.TipoDocumento;
import com.bcs.ventas.service.ProveedorService;
import com.bcs.ventas.service.TipoDocumentoService;
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
@RequestMapping("/proveedors")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private TipoDocumentoService tipoDocumentoService;

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
    public ResponseEntity<Page<Proveedor>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "5") int size,
                                                  @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        Pageable pageable = PageRequest.of(page,size);
        Page<Proveedor> resultado = proveedorService.listar(pageable, buscar);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                 @PathVariable("id") Long id) throws Exception{
        Proveedor obj = proveedorService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Proveedor>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Proveedor> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                               @Valid @RequestBody Proveedor a) throws Exception{
        a.setId(null);
        Proveedor obj = proveedorService.registrar(a);
        obj = proveedorService.listarPorId(obj.getId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        //return  ResponseEntity.created(location).build();
        return ResponseEntity.created(location)
                .body(obj);
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                             @Valid @RequestBody Proveedor a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        Proveedor objBD = proveedorService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = proveedorService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                         @PathVariable("id") Long id) throws Exception{
        Proveedor obj = proveedorService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        proveedorService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    /*
    @GetMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{
        Proveedor obj = proveedorService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        proveedorService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }*/

    //Otros Endpoints

    @GetMapping("/tipodocumentos")
    public ResponseEntity<List<TipoDocumento>> getTipoDocumentos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{
        List<TipoDocumento> obj = tipoDocumentoService.listar();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/getbydoc/{document}")
    public ResponseEntity<Proveedor> listarPorDNI(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                  @PathVariable("document") String document) throws Exception{
        Proveedor obj = proveedorService.getByDocument(document);

        if(obj == null) {
            throw new ModeloNotFoundException("DOCUMENTO DE IDENTIDAD " + document + " NO ENCONTRADO ");
        }

        return new ResponseEntity<Proveedor>(obj, HttpStatus.OK);
    }
}

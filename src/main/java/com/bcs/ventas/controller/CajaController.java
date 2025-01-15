package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.dto.CajaInicioCierreDto;
import com.bcs.ventas.model.entities.Caja;
import com.bcs.ventas.model.entities.CajaAccion;
import com.bcs.ventas.model.entities.CajaDato;
import com.bcs.ventas.model.entities.CajaUser;
import com.bcs.ventas.service.CajaService;
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
@RequestMapping("/cajas")
public class CajaController {
    @Autowired
    private CajaService cajaService;

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
    public ResponseEntity<Page<Caja>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "5") int size,
                                              @RequestParam(name = "buscar", defaultValue = "") String buscar,
                                             @RequestParam(name = "almacen_id", defaultValue = "5") long idAlmacen) throws Exception{

        // @RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestHeader Map<String, String> headers
        this.SetClaims(Authorization);
        /*headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });*/

        Pageable pageable = PageRequest.of(page,size);
        Page<Caja> resultado = cajaService.listar(pageable, buscar, idAlmacen);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/get_by_almacen")
    public ResponseEntity<List<Caja>> listarAllByAlmacen(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                             @RequestParam(name = "buscar", defaultValue = "") String buscar,
                                             @RequestParam(name = "almacen_id", defaultValue = "5") long idAlmacen) throws Exception{

        // @RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestHeader Map<String, String> headers
        this.SetClaims(Authorization);
        /*headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });*/

        //Pageable pageable = PageRequest.of(page,size);
        List<Caja> resultado = cajaService.AllByAlmacen(buscar, idAlmacen);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/get_by_almacen_and_user")
    public ResponseEntity<List<CajaUser>> AllByAlmacenAndUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                               @RequestParam(name = "buscar", defaultValue = "") String buscar,
                                                               @RequestParam(name = "almacen_id", defaultValue = "0") long idAlmacen,
                                                               @RequestParam(name = "user_id", defaultValue = "0") long idUser) throws Exception{

        // @RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestHeader Map<String, String> headers
        this.SetClaims(Authorization);
        /*headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });*/

        //Pageable pageable = PageRequest.of(page,size);
        List<CajaUser> resultado = cajaService.AllByAlmacenAndUsers(buscar, idAlmacen, idUser);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/get_by_almacen_user")
    public ResponseEntity<List<CajaUser>> listarAllByAlmacenAndUserSession(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                         @RequestParam(name = "buscar", defaultValue = "") String buscar,
                                                         @RequestParam(name = "almacen_id", defaultValue = "5") long idAlmacen) throws Exception{

        // @RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestHeader Map<String, String> headers
        this.SetClaims(Authorization);
        /*headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });*/
        Long idUser = claimsAuthorization.getUserId();

        //Pageable pageable = PageRequest.of(page,size);
        List<CajaUser> resultado = cajaService.CajasByAlmacenUser(buscar, idAlmacen, idUser);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caja> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Caja obj = cajaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Caja>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Caja> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @Valid @RequestBody Caja a) throws Exception{

        this.SetClaims(Authorization);

        a.setId(null);
        Caja obj = cajaService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @Valid @RequestBody Caja a) throws Exception{

        this.SetClaims(Authorization);

        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        Caja objBD = cajaService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = cajaService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Caja obj = cajaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        cajaService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{

        this.SetClaims(Authorization);

        Caja obj = cajaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        cajaService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<Caja>> listarAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{

        this.SetClaims(Authorization);

        List<Caja> resultado = cajaService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @PostMapping("/asignar_caja_to_user")
    public ResponseEntity<CajaUser> asignarCajaToUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestBody CajaUser a) throws Exception{

        this.SetClaims(Authorization);

        a.setId(null);
        CajaUser obj = cajaService.AsignarCaja(a);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/eliminar_asignacion_caja_to_user")
    public ResponseEntity<CajaUser> eliminarAsignacionCajaToUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestBody CajaUser a) throws Exception{

        this.SetClaims(Authorization);

        CajaUser obj = cajaService.EliminarAsignacionCaja(a);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/get_caja_iniciada")
    public ResponseEntity<CajaDato> getCajaIniciadaByUserSession(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{
        this.SetClaims(Authorization);

        CajaDato obj = cajaService.getCajaIniciadaByUserSession();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/get_last_caja")
    public ResponseEntity<CajaDato> getLastMontoCaja(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestBody CajaInicioCierreDto a) throws Exception{
        this.SetClaims(Authorization);

        CajaDato obj = cajaService.getLastCajaCerrada(a.getIdCaja());

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    @PostMapping("/iniciar_caja")
    public ResponseEntity<CajaDato> iniciarCaja(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestBody CajaInicioCierreDto a) throws Exception{

        this.SetClaims(Authorization);

        CajaDato obj = cajaService.IniciarCaja(a.getIdCaja(), claimsAuthorization.getUserId(), a.getMonto(), a.getSustento());

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/cierre_caja")
    public ResponseEntity<CajaDato> cerrarCaja(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestBody CajaInicioCierreDto a) throws Exception{

        this.SetClaims(Authorization);

        CajaDato obj = cajaService.CerrarCaja(a.getIdCaja(), claimsAuthorization.getUserId(), a.getMonto());

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/caja_accion")
    public ResponseEntity<CajaAccion> cerrarCaja(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestBody CajaAccion a) throws Exception{

        this.SetClaims(Authorization);

        CajaDato cajaDato = cajaService.getCajaIniciadaByUserSession();

        CajaAccion response = cajaService.registrarCajaAccion(cajaDato, a);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}


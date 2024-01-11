package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.dto.EgresosOtrosDTO;
import com.bcs.ventas.model.dto.IngresosOtrosDTO;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.service.IngresoSalidaCajaService;
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
@RequestMapping("/ingreso_salida_cajas")
public class IngresoSalidaCajaController {

    @Autowired
    private IngresoSalidaCajaService ingresoSalidaCajaService;

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
    public ResponseEntity<Page<IngresoSalidaCaja>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "5") int size,
                                                          @RequestParam(name = "buscar", defaultValue = "") String buscar,
                                                          @RequestParam(name = "almacen_id", defaultValue = "") Long almacen_id) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<IngresoSalidaCaja> resultado = ingresoSalidaCajaService.listar(pageable, buscar, almacen_id);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @PostMapping("/ingresos_otros")
    public ResponseEntity<IngresosOtrosDTO> listarReporteIngresos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                         @RequestBody FiltroGeneral filtros) throws Exception{

        this.SetClaims(Authorization);

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());

        IngresosOtrosDTO resultado = ingresoSalidaCajaService.listarIngresosReporte(pageable, filtros);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @PostMapping("/egresos_otros")
    public ResponseEntity<EgresosOtrosDTO> listarReporteEgresos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                @RequestBody FiltroGeneral filtros) throws Exception{

        this.SetClaims(Authorization);

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());

        EgresosOtrosDTO resultado = ingresoSalidaCajaService.listarEgresosReporte(pageable, filtros);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngresoSalidaCaja> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                         @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        IngresoSalidaCaja obj = ingresoSalidaCajaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<IngresoSalidaCaja>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<IngresoSalidaCaja> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                       @Valid @RequestBody IngresoSalidaCaja a) throws Exception{

        this.SetClaims(Authorization);

        a.setId(null);
        IngresoSalidaCaja obj = ingresoSalidaCajaService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                             @Valid @RequestBody IngresoSalidaCaja a) throws Exception{

        this.SetClaims(Authorization);

        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        IngresoSalidaCaja objBD = ingresoSalidaCajaService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = ingresoSalidaCajaService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                         @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        IngresoSalidaCaja obj = ingresoSalidaCajaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        ingresoSalidaCajaService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar-all")
    public ResponseEntity<List<IngresoSalidaCaja>> listarAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{

        this.SetClaims(Authorization);

        List<IngresoSalidaCaja> resultado = ingresoSalidaCajaService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

}
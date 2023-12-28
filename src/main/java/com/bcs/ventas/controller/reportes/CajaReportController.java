package com.bcs.ventas.controller.reportes;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.dto.CajaSucursalDTO;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.service.reportes.CajaReportService;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caja_reportes")
public class CajaReportController {

    @Autowired
    private CajaReportService cajaReportService;

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

    @GetMapping("/get-caja-diaria")
    public ResponseEntity<CajaSucursalDTO> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                       @RequestParam(name = "almacen_id", defaultValue = "0") Long idAlmacen,
                                                       @RequestParam(name = "fecha", defaultValue = "01/01/2024") String fecha) throws Exception{

        this.SetClaims(Authorization);

        CajaSucursalDTO obj = cajaReportService.cajaDiariaSucursalReporte(fecha, idAlmacen);

        if(obj == null) {
            throw new ModeloNotFoundException("DATA NO ENCONTRADA CONTACTE CON UN ADMINISTRADOR");
        }

        return new ResponseEntity<CajaSucursalDTO>(obj, HttpStatus.OK);
    }

}
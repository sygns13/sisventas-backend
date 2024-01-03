package com.bcs.ventas.controller.reportes;

import com.bcs.ventas.service.reportes.MovimientosProductosReportService;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movimiento_productos_reportes")
public class MovimientoProductosReportController {

    @Autowired
    private MovimientosProductosReportService movimientosProductosReportService;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private JwtUtils jwtUtils;

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

    @PostMapping("/movimiento/export-pdf")
    public ResponseEntity<byte[]> exportPdfInventario(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @RequestBody FiltroGeneral filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("MovimientoProductosReporte", "MovimientoProductosReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(movimientosProductosReportService.exportPdf(filtros));
    }

    @PostMapping("/movimiento/export-xls")
    public ResponseEntity<byte[]> exportXlsInventario(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @RequestBody FiltroGeneral filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("MovimientoProductosReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(movimientosProductosReportService.exportXls(filtros));
    }
}

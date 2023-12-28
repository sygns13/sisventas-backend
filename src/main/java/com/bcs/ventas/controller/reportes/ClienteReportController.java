package com.bcs.ventas.controller.reportes;

import com.bcs.ventas.model.Pet;
import com.bcs.ventas.service.reportes.ClienteReportService;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import io.jsonwebtoken.Claims;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cliente_reportes")
public class ClienteReportController {

    @Autowired
    private ClienteReportService clienteReportService;

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

    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPdf(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("ClienteReporte", "ClienteReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(clienteReportService.exportPdf());
    }

    @GetMapping("/export-xls")
    public ResponseEntity<byte[]> exportXls(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("ClienteReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(clienteReportService.exportXls());
    }
}

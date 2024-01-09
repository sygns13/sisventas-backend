package com.bcs.ventas.controller.reportes;

import com.bcs.ventas.service.reportes.*;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroVenta;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venta_report")
public class VentaReportController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;
    
    @Autowired
    private VentasGeneralReportService ventasGeneralReportService;

    @Autowired
    private VentasDetalladoReportService ventasDetalladoReportService;
    @Autowired
    private VentasTopProductosReportService ventasTopProductosReportService;

    @Autowired
    private CuentasCobrarGeneralReportService cuentasCobrarGeneralReportService;

    @Autowired
    private CuentasCobrarDetalladoReportService cuentasCobrarDetalladoReportService;

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

    @PostMapping("/general/export-pdf")
    public ResponseEntity<byte[]> exportPdfCompras(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                   @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("VentasGeneralesReporte", "VentasGeneralesReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(ventasGeneralReportService.exportPdf(filtros));
    }

    @PostMapping("/general/export-xls")
    public ResponseEntity<byte[]> exportXlsCompras(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                   @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("VentasGeneralesReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(ventasGeneralReportService.exportXls(filtros));
    }
    @PostMapping("/detallada/export-pdf")
    public ResponseEntity<byte[]> exportPdfComprasDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                   @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("VentasDetalladasReporte", "VentasDetalladasReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(ventasDetalladoReportService.exportPdf(filtros));
    }

    @PostMapping("/detallada/export-xls")
    public ResponseEntity<byte[]> exportXlsComprasDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                   @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("VentasDetalladasReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(ventasDetalladoReportService.exportXls(filtros));
    }

    @PostMapping("/top_productos/export-pdf")
    public ResponseEntity<byte[]> exportPdfTopProductos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                         @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("TopProductosVentas", "TopProductosVentas.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(ventasTopProductosReportService.exportPdf(filtros));
    }

    @PostMapping("/top_productos/export-xls")
    public ResponseEntity<byte[]> exportXlsTopProductos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                         @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("TopProductosVentas" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(ventasTopProductosReportService.exportXls(filtros));
    }

    @PostMapping("/cuentas_cobrar_general/export-pdf")
    public ResponseEntity<byte[]> exportPdfCuentasCobrarGeneral(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                        @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("CuentasCobrarGeneralReporte", "CuentasCobrarGeneralReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(cuentasCobrarGeneralReportService.exportPdf(filtros));
    }

    @PostMapping("/cuentas_cobrar_general/export-xls")
    public ResponseEntity<byte[]> exportXlsCuentasCobrarGeneral(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                        @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("CuentasCobrarGeneralReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(cuentasCobrarGeneralReportService.exportXls(filtros));
    }


    @PostMapping("/cuentas_cobrar_detallado/export-pdf")
    public ResponseEntity<byte[]> exportPdfCuentasCobrarDetallado(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("CuentasCobrarDetalladoReporte", "CuentasCobrarDetalladoReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(cuentasCobrarDetalladoReportService.exportPdf(filtros));
    }

    @PostMapping("/cuentas_cobrar_detallado/export-xls")
    public ResponseEntity<byte[]> exportXlsCuentasCobrarDetallado(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("CuentasCobrarDetalladoReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(cuentasCobrarDetalladoReportService.exportXls(filtros));
    }
}

package com.bcs.ventas.controller.reportes;

import com.bcs.ventas.dao.FacturaProveedorDAO;
import com.bcs.ventas.service.EntradaStockService;
import com.bcs.ventas.service.reportes.ComprasDetalladasReportService;
import com.bcs.ventas.service.reportes.ComprasGeneralReportService;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroEntradaStock;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entrada_stock_report")
public class EntradaStockReportController {

    @Autowired
    private EntradaStockService entradaStockService;

    @Autowired
    private FacturaProveedorDAO facturaProveedorDAO;

    @Autowired
    private ComprasGeneralReportService comprasGeneralReportService;

    @Autowired
    private ComprasDetalladasReportService comprasDetalladasReportService;

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

    @PostMapping("/general/export-pdf")
    public ResponseEntity<byte[]> exportPdfInventario(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @RequestBody FiltroEntradaStock filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("ComprasGeneralesReporte", "ComprasGeneralesReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(comprasGeneralReportService.exportPdf(filtros));
    }

    @PostMapping("/general/export-xls")
    public ResponseEntity<byte[]> exportXlsInventario(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @RequestBody FiltroEntradaStock filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("ComprasGeneralesReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(comprasGeneralReportService.exportXls(filtros));
    }

    @PostMapping("/detallada/export-pdf")
    public ResponseEntity<byte[]> exportPdfInventarioDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @RequestBody FiltroEntradaStock filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("ComprasDetalladasReporte", "ComprasDetalladasReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(comprasDetalladasReportService.exportPdf(filtros));
    }

    @PostMapping("/detallada/export-xls")
    public ResponseEntity<byte[]> exportXlsInventarioDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @RequestBody FiltroEntradaStock filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("ComprasDetalladasReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(comprasDetalladasReportService.exportXls(filtros));
    }
}

package com.bcs.ventas.controller.reportes;

import com.bcs.ventas.service.reportes.ProductosInventarioReportService;
import com.bcs.ventas.service.reportes.ProductosPrecioReportService;
import com.bcs.ventas.service.reportes.ProductosSucursalReportService;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroInventario;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producto_reportes")
public class ProductoReportController {
    @Autowired
    private ProductosSucursalReportService productosSucursalReportService;

    @Autowired
    private ProductosInventarioReportService productosInventarioReportService;

    @Autowired
    private ProductosPrecioReportService productosPrecioReportService;

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

    @GetMapping("/sucursal/export-pdf")
    public ResponseEntity<byte[]> exportPdf(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                            @RequestParam(name = "almacen_id", defaultValue = "0") Long almacenId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("ProductoSucursalReporte", "ProductoSucursalReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(productosSucursalReportService.exportPdf(almacenId));
    }

    @GetMapping("/sucursal/export-xls")
    public ResponseEntity<byte[]> exportXls(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                            @RequestParam(name = "almacen_id", defaultValue = "0") Long almacenId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("ProductoSucursalReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(productosSucursalReportService.exportXls(almacenId));
    }

    @PostMapping("/inventario/export-pdf")
    public ResponseEntity<byte[]> exportPdfInventario(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @RequestBody FiltroInventario filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("ProductoInventarioReporte", "ProductoInventarioReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(productosInventarioReportService.exportPdf(filtros));
    }

    @PostMapping("/inventario/export-xls")
    public ResponseEntity<byte[]> exportXlsInventario(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @RequestBody FiltroInventario filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("ProductoInventarioReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(productosInventarioReportService.exportXls(filtros));
    }

    @GetMapping("/precio/export-pdf")
    public ResponseEntity<byte[]> exportPdfPrecio(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                            @RequestParam(name = "almacen_id", defaultValue = "0") Long almacenId,
                                            @RequestParam(name = "unidad_id", defaultValue = "0") Long unidadId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("ProductoPrecioReporte", "ProductoPrecioReporte.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(productosPrecioReportService.exportPdf(almacenId, unidadId));
    }

    @GetMapping("/precio/export-xls")
    public ResponseEntity<byte[]> exportXlsPrecio(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                            @RequestParam(name = "almacen_id", defaultValue = "0") Long almacenId,
                                            @RequestParam(name = "unidad_id", defaultValue = "0") Long unidadId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("ProductoPrecioReporte" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(productosPrecioReportService.exportXls(almacenId, unidadId));
    }
}

package com.bcs.ventas.controller.reportes;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.dto.CajaSucursalDTO;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.service.reportes.*;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroEntradaStock;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.beans.FiltroVenta;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caja_reportes")
public class CajaReportController {

    @Autowired
    private CajaReportService cajaReportService;

    @Autowired
    private ResumenCajaReportService resumenCajaReportService;
    @Autowired
    private CajaIngresoVentasReportService cajaIngresoVentasReportService;

    @Autowired
    private CajaIngresoOtrosReportService cajaIngresoOtrosReportService;

    @Autowired
    private CajaEgresosComprasReportService cajaEgresosComprasReportService;

    @Autowired
    private CajaEgresosOtrosReportService cajaEgresosOtrosReportService;

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

    @PostMapping("/get_resumen_caja")
    public ResponseEntity<CajaSucursalDTO> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                       @RequestBody FiltroGeneral filtros) throws Exception{

        this.SetClaims(Authorization);

        CajaSucursalDTO obj = cajaReportService.resumenCajaReporte(filtros);

        if(obj == null) {
            throw new ModeloNotFoundException("DATA NO ENCONTRADA CONTACTE CON UN ADMINISTRADOR");
        }

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/resumen/export-pdf")
    public ResponseEntity<byte[]> exportPdf(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                            @RequestBody FiltroGeneral filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("ResumenCaja", "ResumenCaja.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(resumenCajaReportService.exportPdf(filtros));
    }

    @PostMapping("/resumen/export-xls")
    public ResponseEntity<byte[]> exportXls(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                            @RequestBody FiltroGeneral filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("ResumenCaja" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resumenCajaReportService.exportXls(filtros));
    }

    @PostMapping("/ingresos_ventas/export-pdf")
    public ResponseEntity<byte[]> exportPdfIngresosVentas(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                            @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("IngresosCajaVentas", "IngresosCajaVentas.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(cajaIngresoVentasReportService.exportPdf(filtros));
    }

    @PostMapping("/ingresos_ventas/export-xls")
    public ResponseEntity<byte[]> exportXlsIngresosVentas(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                            @RequestBody FiltroVenta filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("IngresosCajaVentas" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(cajaIngresoVentasReportService.exportXls(filtros));
    }

    @PostMapping("/ingresos_otros/export-pdf")
    public ResponseEntity<byte[]> exportPdfIngresosOtros(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                          @RequestBody FiltroGeneral filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("IngresosCajaOtros", "IngresosCajaOtros.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(cajaIngresoOtrosReportService.exportPdf(filtros));
    }

    @PostMapping("/ingresos_otros/export-xls")
    public ResponseEntity<byte[]> exportXlsIngresosOtros(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                          @RequestBody FiltroGeneral filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("IngresosCajaOtros" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(cajaIngresoOtrosReportService.exportXls(filtros));
    }

    @PostMapping("/egresos_compras/export-pdf")
    public ResponseEntity<byte[]> exportPdfEgresosVentas(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                          @RequestBody FiltroEntradaStock filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("EgresosCajaCompras", "EgresosCajaCompras.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(cajaEgresosComprasReportService.exportPdf(filtros));
    }

    @PostMapping("/egresos_compras/export-xls")
    public ResponseEntity<byte[]> exportXlsEgresosVentas(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                          @RequestBody FiltroEntradaStock filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("EgresosCajaCompras" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(cajaEgresosComprasReportService.exportXls(filtros));
    }


    @PostMapping("/egresos_otros/export-pdf")
    public ResponseEntity<byte[]> exportPdfEgresosOtros(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                          @RequestBody FiltroGeneral filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("EgresosCajaOtros", "EgresosCajaOtros.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(cajaEgresosOtrosReportService.exportPdf(filtros));
    }

    @PostMapping("/egresos_otros/export-xls")
    public ResponseEntity<byte[]> exportXlsEgresosOtros(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                          @RequestBody FiltroGeneral filtros) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("EgresosCajaOtros" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        this.SetClaims(Authorization);

        return ResponseEntity.ok()
                .headers(headers)
                .body(cajaEgresosOtrosReportService.exportXls(filtros));
    }
}
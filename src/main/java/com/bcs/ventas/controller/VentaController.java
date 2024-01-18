package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.dto.IngresosVentasDTO;
import com.bcs.ventas.model.dto.TopProductosVendidosDTO;
import com.bcs.ventas.model.dto.VentasDetallesDTO;
import com.bcs.ventas.model.entities.CobroVenta;
import com.bcs.ventas.model.entities.DetalleVenta;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.Venta;
import com.bcs.ventas.service.ProductoService;
import com.bcs.ventas.service.VentaService;
import com.bcs.ventas.service.comprobantes.FacturaDetailReportService;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.AgregarProductoBean;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.beans.FiltroVenta;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private FacturaDetailReportService facturaDetailReportService;

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

    @PostMapping("/get-ventas")
    public ResponseEntity<Page<Venta>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "5") int size,
                                              @RequestBody FiltroVenta filtros) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<Venta> obj = ventaService.listar(pageable, filtros);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/get_ventas_detallado")
    public ResponseEntity<Page<VentasDetallesDTO>> listarDetallado(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "5") int size,
                                                          @RequestBody FiltroVenta filtros) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<VentasDetallesDTO> obj = ventaService.listarDetallado(pageable, filtros);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/get_top_productos_vendidos")
    public ResponseEntity<Page<TopProductosVendidosDTO>> listarTopProductosVendidos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                    @RequestParam(name = "size", defaultValue = "5") int size,
                                                                                    @RequestBody FiltroVenta filtros) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<TopProductosVendidosDTO> obj = ventaService.listarTopProductosVendidos(pageable, filtros);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                             @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Venta obj = ventaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Venta> registrarVentaInicial(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                       @Valid @RequestBody Venta a) throws Exception{

        this.SetClaims(Authorization);

        a.setId(null);
        Venta obj = ventaService.registrar(a);
        obj = ventaService.listarPorId(obj.getId());
        obj = ventaService.modificarVentaClienteFirst(obj);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        //return  ResponseEntity.created(location).build();
        return ResponseEntity.created(location)
                .body(obj);
    }

    @PutMapping
    public ResponseEntity<Venta> modificar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                           @Valid @RequestBody Venta v) throws Exception{

        this.SetClaims(Authorization);

        if(v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        Venta objBD = ventaService.listarPorId(v.getId());
        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ v.getId());
        }

        Venta obj = ventaService.modificarVenta(v);

        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @PutMapping("/updateclient")
    public ResponseEntity<Venta> modificarCliente(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                  @Valid @RequestBody Venta v) throws Exception{

        this.SetClaims(Authorization);

        if(v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        Venta objBD = ventaService.listarPorId(v.getId());
        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ v.getId());
        }

        Venta obj = ventaService.modificarVentaCliente(v);
        obj = ventaService.listarPorId(obj.getId());

        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                         @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Venta obj = ventaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        ventaService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/anular/{id}")
    public ResponseEntity<Void> anular(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                       @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Venta obj = ventaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        ventaService.anular(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add-detalle-venta")
    public ResponseEntity<Venta> registrarDetalleVenta(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                       @Valid @RequestBody DetalleVenta d) throws Exception{

        this.SetClaims(Authorization);

        d.setId(null);
        Venta obj = ventaService.registrarDetalle(d);
        Venta res = ventaService.recalcularVentaPublic(obj);

        //URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        //return  ResponseEntity.created(location).build();
        return new ResponseEntity<Venta>(res, HttpStatus.OK);
    }

    @PostMapping("/add-producto-venta")
    public ResponseEntity<Venta> agregarProductoVenta(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                      @RequestBody AgregarProductoBean addProductoVenta) throws Exception{

        this.SetClaims(Authorization);

        Venta obj = ventaService.agregarProducto(addProductoVenta);
        Venta res = ventaService.recalcularVentaPublic(obj);
        return new ResponseEntity<Venta>(res, HttpStatus.OK);
    }

    @PostMapping("/delete-detalle-venta")
    public ResponseEntity<Venta> deleteDetalleVenta(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                    @Valid @RequestBody DetalleVenta d) throws Exception{

        this.SetClaims(Authorization);

        Venta obj = ventaService.eliminarDetalle(d);
        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @PostMapping("/modificar-detalle-venta")
    public ResponseEntity<Venta> modificarDetalleVenta(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                       @Valid @RequestBody DetalleVenta d) throws Exception{

        this.SetClaims(Authorization);

        Venta obj = ventaService.modificarDetalle(d);
        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @PutMapping("/resetventa")
    public ResponseEntity<Venta> resetVenta(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                            @RequestBody Venta v) throws Exception{

        this.SetClaims(Authorization);

        if(v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        Venta objBD = ventaService.listarPorId(v.getId());
        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ v.getId());
        }

        Venta obj = ventaService.resetVenta(v);

        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @PostMapping("/cobrarventa")
    public ResponseEntity<CobroVenta> cobrarVenta(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                  @Valid @RequestBody CobroVenta a) throws Exception{

        this.SetClaims(Authorization);

        if(a.getVenta() == null || a.getVenta().getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        CobroVenta obj = ventaService.cobrarVenta(a);
        return new ResponseEntity<CobroVenta>(obj, HttpStatus.OK);
    }

    @PutMapping("/generarcomprobante")
    public ResponseEntity<Venta> generarComprobante(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                    @RequestBody Venta v) throws Exception{

        this.SetClaims(Authorization);

        if(v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        Venta objBD = ventaService.listarPorId(v.getId());
        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ v.getId());
        }

        Venta obj = ventaService.generarComprobante(v);

        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @PostMapping("/get-ventas-cobrar")
    public ResponseEntity<Page<Venta>> listarVentasCobrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "5") int size,
                                                          @RequestBody FiltroVenta filtros) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<Venta> obj = ventaService.listarCobrado(pageable, filtros);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/get-pagos/{id}")
    public ResponseEntity<Page<CobroVenta>> listarPagos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                                        @PathVariable("id") Long id) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<CobroVenta> obj = ventaService.listarPagos(pageable, id);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    @PostMapping("/get_pagos_reporte")
    public ResponseEntity<Page<CobroVenta>> listarPagos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                                        @RequestBody FiltroVenta filtros) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<CobroVenta> obj = ventaService.listarCobradoDetalle(pageable, filtros);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/get_ingresos_ventas")
    public ResponseEntity<IngresosVentasDTO> listarIngresosVentas(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                                                        @RequestBody FiltroVenta filtros) throws Exception{

        this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        IngresosVentasDTO obj = ventaService.listarIngresosVentas(pageable, filtros);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/comprobante/factura/{id}")
    public ResponseEntity<byte[]> exportPdfCuentasCobrarDetallado(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                  @PathVariable("id") Long id) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("FacturaVenta", "FacturaVenta.pdf");

        this.SetClaims(Authorization);

        return ResponseEntity.ok().headers(headers).body(facturaDetailReportService.exportPdf(id));
    }
}

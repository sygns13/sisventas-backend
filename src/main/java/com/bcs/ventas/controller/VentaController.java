package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.CobroVenta;
import com.bcs.ventas.model.entities.DetalleVenta;
import com.bcs.ventas.model.entities.Producto;
import com.bcs.ventas.model.entities.Venta;
import com.bcs.ventas.service.ProductoService;
import com.bcs.ventas.service.VentaService;
import com.bcs.ventas.utils.beans.AgregarProductoBean;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.beans.FiltroVenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/get-ventas")
    public ResponseEntity<Page<Venta>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size,
                                                 @RequestBody FiltroVenta filtros) throws Exception{
        Pageable pageable = PageRequest.of(page,size);
        Page<Venta> obj = ventaService.listar(pageable, filtros);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> listarPorId(@PathVariable("id") Long id) throws Exception{
        Venta obj = ventaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Venta> registrarVentaInicial(@Valid @RequestBody Venta a) throws Exception{
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
    public ResponseEntity<Venta> modificar(@Valid @RequestBody Venta v) throws Exception{
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
    public ResponseEntity<Venta> modificarCliente(@Valid @RequestBody Venta v) throws Exception{
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
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        Venta obj = ventaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        ventaService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add-detalle-venta")
    public ResponseEntity<Venta> registrarDetalleVenta(@Valid @RequestBody DetalleVenta d) throws Exception{
        d.setId(null);
        Venta obj = ventaService.registrarDetalle(d);
        Venta res = ventaService.recalcularVentaPublic(obj);

        //URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        //return  ResponseEntity.created(location).build();
        return new ResponseEntity<Venta>(res, HttpStatus.OK);
    }

    @PostMapping("/add-producto-venta")
    public ResponseEntity<Venta> agregarProductoVenta(@RequestBody AgregarProductoBean addProductoVenta) throws Exception{
        Venta obj = ventaService.agregarProducto(addProductoVenta);
        Venta res = ventaService.recalcularVentaPublic(obj);
        return new ResponseEntity<Venta>(res, HttpStatus.OK);
    }

    @PostMapping("/delete-detalle-venta")
    public ResponseEntity<Venta> deleteDetalleVenta(@Valid @RequestBody DetalleVenta d) throws Exception{
        Venta obj = ventaService.eliminarDetalle(d);
        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @PostMapping("/modificar-detalle-venta")
    public ResponseEntity<Venta> modificarDetalleVenta(@Valid @RequestBody DetalleVenta d) throws Exception{
        Venta obj = ventaService.modificarDetalle(d);
        return new ResponseEntity<Venta>(obj, HttpStatus.OK);
    }

    @PutMapping("/resetventa")
    public ResponseEntity<Venta> resetVenta(@RequestBody Venta v) throws Exception{
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
    public ResponseEntity<CobroVenta> cobrarVenta(@Valid @RequestBody CobroVenta a) throws Exception{
        if(a.getVenta() == null || a.getVenta().getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        CobroVenta obj = ventaService.cobrarVenta(a);
        return new ResponseEntity<CobroVenta>(obj, HttpStatus.OK);
    }

    @PutMapping("/generarcomprobante")
    public ResponseEntity<Venta> generarComprobante(@RequestBody Venta v) throws Exception{
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
}

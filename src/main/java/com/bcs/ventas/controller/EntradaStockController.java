package com.bcs.ventas.controller;

import com.bcs.ventas.dao.FacturaProveedorDAO;
import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.PagoProveedor;
import com.bcs.ventas.model.entities.DetalleEntradaStock;
import com.bcs.ventas.model.entities.EntradaStock;
import com.bcs.ventas.service.EntradaStockService;
import com.bcs.ventas.utils.beans.AgregarProductoBean;
import com.bcs.ventas.utils.beans.FiltroEntradaStock;
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
@RequestMapping("/entrada-stock")
public class EntradaStockController {

    @Autowired
    private EntradaStockService entradaStockService;

    @Autowired
    private FacturaProveedorDAO facturaProveedorDAO;

    @PostMapping("/get-entrada-stocks")
    public ResponseEntity<Page<EntradaStock>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "5") int size,
                                              @RequestBody FiltroEntradaStock filtros) throws Exception{
        Pageable pageable = PageRequest.of(page,size);
        Page<EntradaStock> obj = entradaStockService.listar(pageable, filtros);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaStock> listarPorId(@PathVariable("id") Long id) throws Exception{
        EntradaStock obj = entradaStockService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<EntradaStock>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EntradaStock> registrarEntradaStockInicial(@Valid @RequestBody EntradaStock a) throws Exception{
        a.setId(null);
        EntradaStock obj = entradaStockService.registrar(a);
        obj = entradaStockService.listarPorId(obj.getId());
        obj = entradaStockService.modificarEntradaStockProveedorFirst(obj);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        //return  ResponseEntity.created(location).build();
        return ResponseEntity.created(location)
                .body(obj);
    }

    @PutMapping
    public ResponseEntity<EntradaStock> modificar(@Valid @RequestBody EntradaStock v) throws Exception{
        if(v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        EntradaStock objBD = entradaStockService.listarPorId(v.getId());
        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ v.getId());
        }

        EntradaStock obj = entradaStockService.modificarEntradaStock(v);

        return new ResponseEntity<EntradaStock>(obj, HttpStatus.OK);
    }

    @PutMapping("/updateproveedor")
    public ResponseEntity<EntradaStock> modificarProveedor(@Valid @RequestBody EntradaStock v) throws Exception{
        if(v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        EntradaStock objBD = entradaStockService.listarPorId(v.getId());
        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ v.getId());
        }

        EntradaStock obj = entradaStockService.modificarEntradaStockProveedor(v);
        obj = entradaStockService.listarPorId(obj.getId());

        return new ResponseEntity<EntradaStock>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        EntradaStock obj = entradaStockService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        entradaStockService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/anular/{id}")
    public ResponseEntity<Void> anular(@PathVariable("id") Long id) throws Exception{
        EntradaStock obj = entradaStockService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        entradaStockService.anular(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add-detalle-entrada-stock")
    public ResponseEntity<EntradaStock> registrarDetalleEntradaStock(@Valid @RequestBody DetalleEntradaStock d) throws Exception{
        d.setId(null);
        EntradaStock obj = entradaStockService.registrarDetalle(d);
        EntradaStock res = entradaStockService.recalcularEntradaStockPublic(obj);

        //URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        //return  ResponseEntity.created(location).build();
        return new ResponseEntity<EntradaStock>(res, HttpStatus.OK);
    }

    @PostMapping("/add-producto-entrada-stock")
    public ResponseEntity<EntradaStock> agregarProductoEntradaStock(@RequestBody AgregarProductoBean addProductoEntradaStock) throws Exception{
        EntradaStock obj = entradaStockService.agregarProducto(addProductoEntradaStock);
        EntradaStock res = entradaStockService.recalcularEntradaStockPublic(obj);
        return new ResponseEntity<EntradaStock>(res, HttpStatus.OK);
    }

    @PostMapping("/delete-detalle-entrada-stock")
    public ResponseEntity<EntradaStock> deleteDetalleEntradaStock(@Valid @RequestBody DetalleEntradaStock d) throws Exception{
        EntradaStock obj = entradaStockService.eliminarDetalle(d);
        return new ResponseEntity<EntradaStock>(obj, HttpStatus.OK);
    }

    @PostMapping("/modificar-detalle-entrada-stock")
    public ResponseEntity<EntradaStock> modificarDetalleEntradaStock(@Valid @RequestBody DetalleEntradaStock d) throws Exception{
        EntradaStock obj = entradaStockService.modificarDetalle(d);
        EntradaStock res = entradaStockService.recalcularEntradaStockPublic(obj);
        return new ResponseEntity<EntradaStock>(res, HttpStatus.OK);
    }

    @PutMapping("/resetentrada-stock")
    public ResponseEntity<EntradaStock> resetEntradaStock(@RequestBody EntradaStock v) throws Exception{
        if(v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        EntradaStock objBD = entradaStockService.listarPorId(v.getId());
        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ v.getId());
        }

        EntradaStock obj = entradaStockService.resetEntradaStock(v);

        return new ResponseEntity<EntradaStock>(obj, HttpStatus.OK);
    }

    @PostMapping("/pago-proveedor")
    public ResponseEntity<PagoProveedor> pagarEntradaStock(@Valid @RequestBody PagoProveedor a) throws Exception{
        if(a.getEntradaStock() == null || a.getEntradaStock().getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        PagoProveedor obj = entradaStockService.cobrarEntradaStock(a);
        return new ResponseEntity<PagoProveedor>(obj, HttpStatus.OK);
    }

    @PostMapping("/get-entrada-stocks-cobrar")
    public ResponseEntity<Page<EntradaStock>> listarEntradaStocksCobrar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "5") int size,
                                                          @RequestBody FiltroEntradaStock filtros) throws Exception{
        Pageable pageable = PageRequest.of(page,size);
        Page<EntradaStock> obj = entradaStockService.listarPagado(pageable, filtros);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping("/get-pagos/{id}")
    public ResponseEntity<Page<PagoProveedor>> listarPagos(@RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                                        @PathVariable("id") Long id) throws Exception{
        Pageable pageable = PageRequest.of(page,size);
        Page<PagoProveedor> obj = entradaStockService.listarPagos(pageable, id);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PutMapping("/facturar")
    public ResponseEntity<EntradaStock> facturarEntradaStock(@Valid @RequestBody EntradaStock v) throws Exception{
        if(v == null || v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        EntradaStock obj = entradaStockService.facturarEntradaStock(v);
        return new ResponseEntity<EntradaStock>(obj, HttpStatus.OK);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<EntradaStock> actualizarEntradaStock(@Valid @RequestBody EntradaStock v) throws Exception{
        if(v == null || v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        EntradaStock obj = entradaStockService.actualizarEntradaStock(v);
        return new ResponseEntity<EntradaStock>(obj, HttpStatus.OK);
    }

    @PutMapping("/revertir-facturar")
    public ResponseEntity<EntradaStock> revertirFacturaEntradaStock(@Valid @RequestBody EntradaStock v) throws Exception{
        if(v == null || v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }
        Long idFactura = null;
        if(v.getFacturaProveedor() != null && v.getFacturaProveedor().getId() != null)
            idFactura = v.getFacturaProveedor().getId();

        EntradaStock obj = entradaStockService.revertirFacturaEntradaStock(v);
        if(idFactura != null)
            facturaProveedorDAO.eliminar(idFactura);

        return new ResponseEntity<EntradaStock>(obj, HttpStatus.OK);
    }

    @PutMapping("/revertir-actualizar")
    public ResponseEntity<EntradaStock> revertirActualizacionEntradaStock(@Valid @RequestBody EntradaStock v) throws Exception{
        if(v == null || v.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        EntradaStock obj = entradaStockService.revertirActualizacionEntradaStock(v);
        return new ResponseEntity<EntradaStock>(obj, HttpStatus.OK);
    }
}

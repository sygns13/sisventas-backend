package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<Producto>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size,
                                                 @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{
        Pageable pageable = PageRequest.of(page,size);
        Page<Producto> obj = productoService.listar(pageable, buscar);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> listarPorId(@PathVariable("id") Long id) throws Exception{
        Producto obj = productoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Producto>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Producto> registrar(@Valid @RequestBody Producto a) throws Exception{
        a.setId(null);
        Producto obj = productoService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@Valid @RequestBody Producto a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        Producto objBD = productoService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }


        int obj = productoService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        Producto obj = productoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        productoService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }




    //Otros Endpoints

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoProducto>> getTipoProductos() throws Exception{
        List<TipoProducto> obj = productoService.getTipoProductos();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<Marca>> getMarcas() throws Exception{
        List<Marca> obj = productoService.getMarcas();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/presentaciones")
    public ResponseEntity<List<Presentacion>> getPresentaciones() throws Exception{
        List<Presentacion> obj = productoService.getPresentaciones();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

}

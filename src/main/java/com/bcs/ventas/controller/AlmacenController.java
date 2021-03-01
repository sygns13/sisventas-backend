package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.Departamento;
import com.bcs.ventas.model.entities.Distrito;
import com.bcs.ventas.model.entities.Provincia;
import com.bcs.ventas.service.AlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;

@RestController
@RequestMapping("/almacens")
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    @GetMapping
    public ResponseEntity<Page<Almacen>> listar(@RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "5") int size,
                                                @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        Pageable pageable = PageRequest.of(page,size);
        Page<Almacen> resultado = almacenService.listar(pageable, buscar);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Almacen> listarPorId(@PathVariable("id") Long id) throws Exception{
        Almacen obj = almacenService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Almacen>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Almacen> registrar(@Valid @RequestBody Almacen a) throws Exception{
        a.setId(null);
        Almacen obj = almacenService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@Valid @RequestBody Almacen a) throws Exception{
        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        Almacen objBD = almacenService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO " + a.getId());
        }

        int obj = almacenService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws Exception{
        Almacen obj = almacenService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        almacenService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/altabaja/{id}/{valor}")
    public ResponseEntity<Void> altabaja(@PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{
        Almacen obj = almacenService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        almacenService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }




    //Otros Endpoints

    @GetMapping("/departamentos/{idPais}")
    public ResponseEntity<List<Departamento>> getDepartamentos(@PathVariable("idPais") Long idPais) throws Exception{
        List<Departamento> obj = almacenService.getDepartamentos(idPais);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/provincias/{idDep}")
    public ResponseEntity<List<Provincia>> getProvincias(@PathVariable("idDep") Long idDep) throws Exception{
        List<Provincia> obj = almacenService.getProvincias(idDep);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/distritos/{idProv}")
    public ResponseEntity<List<Distrito>> getDistritos(@PathVariable("idProv") Long idProv) throws Exception{
        List<Distrito> obj = almacenService.getDistritos(idProv);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

}

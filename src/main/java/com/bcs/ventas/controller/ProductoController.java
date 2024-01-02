package com.bcs.ventas.controller;

import com.bcs.ventas.exception.ModeloNotFoundException;
import com.bcs.ventas.model.dto.InventarioDTO;
import com.bcs.ventas.model.dto.ProductoBajoStockDTO;
import com.bcs.ventas.model.dto.ProductoVencidoDTO;
import com.bcs.ventas.model.dto.ProductosVentaDTO;
import com.bcs.ventas.model.entities.*;
import com.bcs.ventas.service.ProductoService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.JwtUtils;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.beans.FiltroGeneral;
import com.bcs.ventas.utils.beans.FiltroInventario;
import com.bcs.ventas.utils.beans.FiltroProductosVenta;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @GetMapping
    public ResponseEntity<Page<Producto>> listar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size,
                                                 @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{
        Pageable pageable = PageRequest.of(page,size);
        Page<Producto> obj = productoService.listar(pageable, buscar);

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> listarPorId(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                @PathVariable("id") Long id) throws Exception{
        Producto obj = productoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Producto>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Producto> registrar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                              @Valid @RequestBody Producto a) throws Exception{
        a.setId(null);
        Producto obj = productoService.registrar(a);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Integer> modificar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                             @Valid @RequestBody Producto a) throws Exception{
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
    public ResponseEntity<Void> eliminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                         @PathVariable("id") Long id) throws Exception{
        Producto obj = productoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        productoService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/productosventa")
    public ResponseEntity<Page<ProductosVentaDTO>> getProductosVentas(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                      @RequestBody FiltroProductosVenta filtros) throws Exception{

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());
        Page<ProductosVentaDTO> productosVentas = productoService.getProductosVentas(pageable, filtros);

        return new ResponseEntity<>(productosVentas, HttpStatus.OK);
    }

    //EndPoint Inventario

    @PostMapping("/productoinventario")
    public ResponseEntity<Page<InventarioDTO>> getProductoInventario(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                     @RequestBody FiltroInventario filtros) throws Exception{

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());
        Page<InventarioDTO> inventario = productoService.getInventario(pageable, filtros);

        return new ResponseEntity<>(inventario, HttpStatus.OK);
    }

    @PostMapping("/productoprecio")
    public ResponseEntity<Page<ProductosVentaDTO>> getProductoPrecio(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                     @RequestBody FiltroGeneral filtros) throws Exception{

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());
        Page<ProductosVentaDTO> inventario = productoService.ProductosPrecioReport(pageable, filtros.getAlmacenId(), filtros.getUnidadId());

        return new ResponseEntity<>(inventario, HttpStatus.OK);
    }

    //EndPoint GestionLotes

    @PostMapping("/productogestionlotes")
    public ResponseEntity<Page<InventarioDTO>> getProductoLotes(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                @RequestBody FiltroGeneral filtros) throws Exception{

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());
        Page<InventarioDTO> inventario = productoService.getProductosGestionLotes(pageable, filtros);

        return new ResponseEntity<>(inventario, HttpStatus.OK);
    }


    //EndPoint Productos Bajos de Stock

    @PostMapping("/productosbajostock")
    public ResponseEntity<Page<ProductoBajoStockDTO>> getProductosBajoStock(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                            @RequestBody FiltroGeneral filtros) throws Exception{

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());
        Page<ProductoBajoStockDTO> productosBajoStock = productoService.getProductosBajoStock(pageable, filtros);

        return new ResponseEntity<>(productosBajoStock, HttpStatus.OK);
    }

    //EndPoint Productos Vencidos

    @PostMapping("/productosvencidos")
    public ResponseEntity<Page<ProductoVencidoDTO>> getProductosVencidos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
                                                                         @RequestBody FiltroGeneral filtros) throws Exception{

        if(filtros.getPage() == null) filtros.setPage(Constantes.CANTIDAD_ZERO);
        if(filtros.getSize() == null) filtros.setSize(Constantes.CANTIDAD_MINIMA_PAGE);

        Pageable pageable = PageRequest.of(filtros.getPage().intValue(), filtros.getSize().intValue());
        Page<ProductoVencidoDTO> productosVencidos = productoService.getProductosVencidos(pageable, filtros);

        return new ResponseEntity<>(productosVencidos, HttpStatus.OK);
    }




    //Otros Endpoints

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoProducto>> getTipoProductos(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{
        List<TipoProducto> obj = productoService.getTipoProductos();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<Marca>> getMarcas(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{
        List<Marca> obj = productoService.getMarcas();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/presentaciones")
    public ResponseEntity<List<Presentacion>> getPresentaciones(@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization) throws Exception{
        List<Presentacion> obj = productoService.getPresentaciones();

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

}

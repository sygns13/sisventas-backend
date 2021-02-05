package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Producto Model")
@Entity
@Table(name = "productos")
public class Producto implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del Producto")
    @NotNull( message = "{producto.nombre.notnull}")
    @Size(min = 1, max = 150, message = "{producto.nombre.size}")
    @Column(name="nombre", nullable = true, length= 150)
    private String nombre;

    @Schema(description = "Tipo de Producto")
    @ManyToOne
    @JoinColumn(name = "tipo_producto_id", nullable = false, foreignKey = @ForeignKey(name = "FK_tipo_producto"))
    private TipoProducto tipoProducto;

    @Schema(description = "Marca de Producto")
    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false, foreignKey = @ForeignKey(name = "FK_marca_producto"))
    private Marca marca;

    @Schema(description = "Stock mínimo de Producto")
    @Min(value = 0, message = "{producto.stockMinimo.min}")
    @Max(value = 999999999, message = "{producto.stockMinimo.max}")
    @Column(name="stock_minimo", nullable = true)
    private Double stockMinimo;

    @Schema(description = "Precio de Venta de Producto")
    @NotNull( message = "{producto.precio_unidad.notnull}")
    @DecimalMin(value = "0.01", message = "{producto.precio_unidad.min}")
    @DecimalMax(value = "999999999", message = "{producto.precio_unidad.max}")
    @Column(name="precio_unidad", nullable = true)
    private Double precioUnidad;

    @Schema(description = "Precio de Compra de Producto")
    @NotNull( message = "{producto.precio_compra.notnull}")
    @DecimalMin(value = "0.01", message = "{producto.precio_compra.min}")
    @DecimalMax(value = "999999999", message = "{producto.precio_compra.max}")
    @Column(name="precio_compra", nullable = true)
    private Double precioCompra;

    @Schema(description = "Fecha de registro del Producto")
    @NotNull( message = "{producto.fecha.notnull}")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    @Schema(description = "Código Unitario del Producto")
    @NotNull( message = "{producto.codigo_unidad.notnull}")
    @Size(min = 1, max = 200, message = "{producto.codigo_unidad.size}")
    @Column(name="codigo_unidad", nullable = true, length= 200)
    private String codigoUnidad;

    @Schema(description = "Código General del Producto")
    @Column(name="codigo_producto", nullable = true, length= 200)
    private String codigoProducto;

    @Schema(description = "Presentación del Producto")
    @ManyToOne
    @JoinColumn(name = "presentacion_id", nullable = false, foreignKey = @ForeignKey(name = "FK_presentacion_producto"))
    private Presentacion presentacion;

    @Schema(description = "Composición del Producto")
    @Size(max = 500, message = "{producto.composicion.size}")
    @Column(name="composicion", nullable = true, length= 500)
    private String composicion;

    @Schema(description = "Prioridad del Producto")
    @Size(max = 100, message = "{producto.prioridad.size}")
    @Column(name="prioridad", nullable = true, length= 100)
    private String prioridad;

    @Schema(description = "Ubicación del Producto")
    @Size(max = 100, message = "{producto.ubicacion.size}")
    @Column(name="ubicacion", nullable = true, length= 100)
    private String ubicacion;

    @Schema(description = "Uso de Lotes en el control del Producto")
    @NotNull( message = "{producto.activo_lotes.notnull}")
    @Column(name="activo_lotes", nullable = true)
    private Integer activoLotes;

    @Schema(description = "Indicador de ISC del Producto")
    @NotNull( message = "{producto.afecto_isc.notnull}")
    @Column(name="afecto_isc", nullable = true)
    private Integer afectoIsc;

    @Schema(description = "Tipo de Tasa de ISC del Producto")
    @NotNull( message = "{producto.tipo_tasa_isc.notnull}")
    @Column(name="tipo_tasa_isc", nullable = true)
    private Integer tipoTasaIsc;

    @Schema(description = "Tasa de ISC del Producto")
    @NotNull( message = "{producto.tasa_isc.notnull}")
    @Column(name="tasa_isc", nullable = true)
    private Double tasaIsc;

    @Schema(description = "Indicador de IGV del Producto")
    @NotNull( message = "{producto.afecto_igv.notnull}")
    @Column(name="afecto_igv", nullable = true)
    private Integer afectoIgv;

    @Schema(description = "ID User Padre")
    @NotNull( message = "{producto.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID Empresa Padre")
    @NotNull( message = "{producto.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado de Producto")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de Producto")
    @Column(name="borrado", nullable = true)
    private Integer borrado;

    @Schema(description = "Fecha de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="created_at", nullable = true)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de Update del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="updated_at", nullable = true)
    private LocalDateTime updatedAd;

    public Producto() {
    }

    public Producto(Long id, String nombre, TipoProducto tipoProducto, Marca marca,Double stockMinimo, Double precioUnidad,  Double precioCompra, LocalDate fecha,   String codigoUnidad, String codigoProducto, Presentacion presentacion, String composicion, String prioridad,  String ubicacion, Integer activoLotes, Integer afectoIsc, Integer tipoTasaIsc, Double tasaIsc,  Integer afectoIgv, Long userId,  Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
        this.marca = marca;
        this.stockMinimo = stockMinimo;
        this.precioUnidad = precioUnidad;
        this.precioCompra = precioCompra;
        this.fecha = fecha;
        this.codigoUnidad = codigoUnidad;
        this.codigoProducto = codigoProducto;
        this.presentacion = presentacion;
        this.composicion = composicion;
        this.prioridad = prioridad;
        this.ubicacion = ubicacion;
        this.activoLotes = activoLotes;
        this.afectoIsc = afectoIsc;
        this.tipoTasaIsc = tipoTasaIsc;
        this.tasaIsc = tasaIsc;
        this.afectoIgv = afectoIgv;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public Producto(Long id, String nombre, TipoProducto tipoProducto, Marca marca,Double stockMinimo, Double precioUnidad,  Double precioCompra, LocalDate fecha,   String codigoUnidad, String codigoProducto, Presentacion presentacion, String composicion, String prioridad,  String ubicacion, Integer activoLotes, Integer afectoIsc, Integer tipoTasaIsc, Double tasaIsc,  Integer afectoIgv, Long userId,  Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
        this.marca = marca;
        this.stockMinimo = stockMinimo;
        this.precioUnidad = precioUnidad;
        this.precioCompra = precioCompra;
        this.fecha = fecha;
        this.codigoUnidad = codigoUnidad;
        this.codigoProducto = codigoProducto;
        this.presentacion = presentacion;
        this.composicion = composicion;
        this.prioridad = prioridad;
        this.ubicacion = ubicacion;
        this.activoLotes = activoLotes;
        this.afectoIsc = afectoIsc;
        this.tipoTasaIsc = tipoTasaIsc;
        this.tasaIsc = tasaIsc;
        this.afectoIgv = afectoIgv;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Double getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Double getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(Double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public Presentacion getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getActivoLotes() {
        return activoLotes;
    }

    public void setActivoLotes(Integer activoLotes) {
        this.activoLotes = activoLotes;
    }

    public Integer getAfectoIsc() {
        return afectoIsc;
    }

    public void setAfectoIsc(Integer afectoIsc) {
        this.afectoIsc = afectoIsc;
    }

    public Integer getTipoTasaIsc() {
        return tipoTasaIsc;
    }

    public void setTipoTasaIsc(Integer tipoTasaIsc) {
        this.tipoTasaIsc = tipoTasaIsc;
    }

    public Double getTasaIsc() {
        return tasaIsc;
    }

    public void setTasaIsc(Double tasaIsc) {
        this.tasaIsc = tasaIsc;
    }

    public Integer getAfectoIgv() {
        return afectoIgv;
    }

    public void setAfectoIgv(Integer afectoIgv) {
        this.afectoIgv = afectoIgv;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getBorrado() {
        return borrado;
    }

    public void setBorrado(Integer borrado) {
        this.borrado = borrado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAd() {
        return updatedAd;
    }

    public void setUpdatedAd(LocalDateTime updatedAd) {
        this.updatedAd = updatedAd;
    }
}

package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Detalle Unidades Producto Model")
@Entity
@Table(name = "detalle_unidad_productos")
public class DetalleUnidadProducto implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID del Producto")
    @NotNull( message = "{detalleunidadproducto.producto_id.notnull}")
    @Column(name="producto_id", nullable = true)
    private Long productoId;

    /*
    @Schema(description = "ID de la Unidad")
    @NotNull( message = "{detalleunidadproducto.unidad_id.notnull}")
    @Column(name="unidad_id", nullable = true)
    private Long unidadId;
     */

    @Schema(description = "Unidad")
    @ManyToOne
    @JoinColumn(name = "unidad_id", nullable = false, foreignKey = @ForeignKey(name = "FK_unidad"))
    private Unidad unidad;

    @Schema(description = "Código Unitario de la Unidad del Producto")
    @NotNull( message = "{detalleunidadproducto.codigo_unidad.notnull}")
    @Size(min = 1, max = 200, message = "{detalleunidadproducto.codigo_unidad.size}")
    @Column(name="codigo_unidad", nullable = true, length= 50)
    private String codigoUnidad;


    @Schema(description = "Precio de Venta de Unidad de Producto")
    @NotNull( message = "{detalleunidadproducto.precio.notnull}")
    @DecimalMin(value = "0.01", message = "{detalleunidadproducto.precio.min}")
    @DecimalMax(value = "999999999", message = "{detalleunidadproducto.precio.max}")
    @Column(name="precio", nullable = true)
    private Double precio;

    @Schema(description = "Precio de Compra de Unidad de Producto")
    @NotNull( message = "{detalleunidadproducto.costo_compra.notnull}")
    @DecimalMin(value = "0.01", message = "{detalleunidadproducto.costo_compra.min}")
    @DecimalMax(value = "999999999", message = "{detalleunidadproducto.costo_compra.max}")
    @Column(name="costo_compra", nullable = true)
    private Double costoCompra;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{detalleunidadproducto.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{detalleunidadproducto.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado de Detalle de Unidad de Producto")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de Detalle de Unidad de Producto")
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

    @Schema(description = "ID del Local")
    @NotNull( message = "{detalleunidadproducto.almacen_id.notnull}")
    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    public DetalleUnidadProducto() {
    }

    public DetalleUnidadProducto(Long id, Long productoId, Unidad unidad, String codigoUnidad, Double precio, Double costoCompra, Long userId, Long empresaId, Integer activo, Integer borrado, Long almacenId) {
        this.id = id;
        this.productoId = productoId;
        this.unidad = unidad;
        this.codigoUnidad = codigoUnidad;
        this.precio = precio;
        this.costoCompra = costoCompra;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
        this.almacenId = almacenId;
    }

    public DetalleUnidadProducto(Long id, Long productoId, Unidad unidad, String codigoUnidad, Double precio, Double costoCompra, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd, Long almacenId) {
        this.id = id;
        this.productoId = productoId;
        this.unidad = unidad;
        this.codigoUnidad = codigoUnidad;
        this.precio = precio;
        this.costoCompra = costoCompra;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
        this.almacenId = almacenId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(Double costoCompra) {
        this.costoCompra = costoCompra;
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

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }
}

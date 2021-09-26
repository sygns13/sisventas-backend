package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Detalle Ventas Model")
@Entity
@Table(name = "detalle_ventas")
public class DetalleVenta implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID de la Venta")
    @Column(name="venta_id", nullable = true)
    private Long ventaId;

    //@Column(name="producto_id", nullable = true)
    //private Long productoId;
    @Schema(description = "Producto de la Venta")
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false, foreignKey = @ForeignKey(name = "FK_producto_detalle?venta"))
    private Producto producto;

    @Schema(description = "Precio de Venta del Producto")
    @NotNull( message = "{detalle_venta.precio_venta.notnull}")
    @DecimalMin(value = "0.01", message = "{detalle_venta.precio_venta.min}")
    @DecimalMax(value = "999999999", message = "{detalle_venta.precio_venta.max}")
    @Column(name="precio_venta", nullable = true)
    private Long precioVenta;

    @Schema(description = "Precio de Compra del Producto")
    @NotNull( message = "{detalle_venta.precio_compra.notnull}")
    @DecimalMin(value = "0.01", message = "{detalle_venta.precio_compra.min}")
    @DecimalMax(value = "999999999", message = "{detalle_venta.precio_compra.max}")
    @Column(name="precio_compra", nullable = true)
    private Long precioCompra;

    @Schema(description = "Cantidad de Producto vendido")
    @NotNull( message = "{detalle_venta.cantidad.notnull}")
    @DecimalMin(value = "0.00", message = "{detalle_venta.cantidad.min}")
    @DecimalMax(value = "999999999", message = "{detalle_venta.cantidad.max}")
    @Column(name="cantidad", nullable = true)
    private Double cantidad;

    @Schema(description = "ID del Local")
    @NotNull( message = "{detalle_venta.almacen_id.notnull}")
    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    @Schema(description = "Producto es Grabado")
    @Column(name="es_grabado", nullable = true)
    private Integer esGrabado;

    @Schema(description = "Descuento de Producto vendido")
    @NotNull( message = "{detalle_venta.descuento.notnull}")
    @DecimalMin(value = "0.00", message = "{detalle_venta.descuento.min}")
    @DecimalMax(value = "999999999", message = "{detalle_venta.descuento.max}")
    @Column(name="descuento", nullable = true)
    private Double descuento;

    @Schema(description = "Tipo de Descuento de Producto vendido")
    @Column(name="tipo_descuento", nullable = true)
    private Integer tipDescuento;

    @Schema(description = "Cantidad Real de Unidad de Producto vendido")
    @NotNull( message = "{detalle_venta.cantidad_real.notnull}")
    @DecimalMin(value = "0.00", message = "{detalle_venta.cantidad_real.min}")
    @DecimalMax(value = "999999999", message = "{detalle_venta.cantidad_real.max}")
    @Column(name="cantidad_real", nullable = true)
    private Double cantidadReal;

    @Schema(description = "Descripción de Unidad de Producto vendido")
    @Column(name="unidad", nullable = true, length= 500)
    private String unidad;

    @Schema(description = "Lote de Producto vendido")
    @Column(name="lote_id", nullable = true)
    private Long loteId;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{producto.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{producto.empresa_id.notnull}")
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

    public DetalleVenta() {
    }

    public DetalleVenta(Long id, Long ventaId, Producto producto, Long precioVenta, Long precioCompra, Double cantidad, Long almacenId, Integer esGrabado, Double descuento, Integer tipDescuento, Double cantidadReal, String unidad, Long loteId, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.ventaId = ventaId;
        this.producto = producto;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
        this.cantidad = cantidad;
        this.almacenId = almacenId;
        this.esGrabado = esGrabado;
        this.descuento = descuento;
        this.tipDescuento = tipDescuento;
        this.cantidadReal = cantidadReal;
        this.unidad = unidad;
        this.loteId = loteId;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public DetalleVenta(Long id, Long ventaId, Producto producto, Long precioVenta, Long precioCompra, Double cantidad, Long almacenId, Integer esGrabado, Double descuento, Integer tipDescuento, Double cantidadReal, String unidad, Long loteId, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.ventaId = ventaId;
        this.producto = producto;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
        this.cantidad = cantidad;
        this.almacenId = almacenId;
        this.esGrabado = esGrabado;
        this.descuento = descuento;
        this.tipDescuento = tipDescuento;
        this.cantidadReal = cantidadReal;
        this.unidad = unidad;
        this.loteId = loteId;
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

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Long getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Long precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Long getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Long precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Integer getEsGrabado() {
        return esGrabado;
    }

    public void setEsGrabado(Integer esGrabado) {
        this.esGrabado = esGrabado;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Integer getTipDescuento() {
        return tipDescuento;
    }

    public void setTipDescuento(Integer tipDescuento) {
        this.tipDescuento = tipDescuento;
    }

    public Double getCantidadReal() {
        return cantidadReal;
    }

    public void setCantidadReal(Double cantidadReal) {
        this.cantidadReal = cantidadReal;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Long getLoteId() {
        return loteId;
    }

    public void setLoteId(Long loteId) {
        this.loteId = loteId;
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

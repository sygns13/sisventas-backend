package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Column(name="venta_id", nullable = true)
    private Long ventaId;

    @Column(name="producto_id", nullable = true)
    private Long productoId;

    @Column(name="precio_venta", nullable = true)
    private Long precioVenta;

    @Column(name="precio_compra", nullable = true)
    private Long precioCompra;

    @Column(name="cantidad", nullable = true)
    private Double cantidad;

    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    @Column(name="es_grabado", nullable = true)
    private Integer esGrabado;

    @Column(name="descuento", nullable = true)
    private Double descuento;

    @Column(name="tipo_descuento", nullable = true)
    private Integer tipDescuento;

    @Column(name="cantidad_real", nullable = true)
    private Double cantidadReal;

    @Column(name="unidad", nullable = true, length= 500)
    private String unidad;

    @Column(name="lote_id", nullable = true)
    private Long loteId;

    @Column(name="user_id", nullable = true)
    private Long userId;

    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Column(name="activo", nullable = true)
    private Integer activo;

    @Column(name="borrado", nullable = true)
    private Integer borrado;

    @Column(name="created_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name="updated_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAd;

    public DetalleVenta() {
    }

    public DetalleVenta(Long id, Long ventaId, Long productoId, Long precioVenta, Long precioCompra, Double cantidad, Long almacenId, Integer esGrabado, Double descuento, Integer tipDescuento, Double cantidadReal, String unidad, Long loteId, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.ventaId = ventaId;
        this.productoId = productoId;
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

    public DetalleVenta(Long id, Long ventaId, Long productoId, Long precioVenta, Long precioCompra, Double cantidad, Long almacenId, Integer esGrabado, Double descuento, Integer tipDescuento, Double cantidadReal, String unidad, Long loteId, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.ventaId = ventaId;
        this.productoId = productoId;
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

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAd() {
        return updatedAd;
    }

    public void setUpdatedAd(Date updatedAd) {
        this.updatedAd = updatedAd;
    }
}

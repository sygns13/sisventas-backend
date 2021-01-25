package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "detalle_entrada_stocks")
public class DetalleEntradaStock implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="entrada_stock_id", nullable = true)
    private Long entradaStockId;

    @Column(name="producto_id", nullable = true)
    private Long productoId;

    @Column(name="cantidad", nullable = true)
    private Double cantidad;

    @Column(name="costo", nullable = true)
    private Double costo;

    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    @Column(name="cantreal", nullable = true)
    private Integer cantreal;

    @Column(name="unidad", nullable = true, length= 500)
    private String unidad;

    @Column(name="lote_id", nullable = true)
    private Long loteId;

    @Column(name="user_id", nullable = true)
    private Long userId;

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

    public DetalleEntradaStock() {
    }

    public DetalleEntradaStock(Long id, Long entradaStockId, Long productoId, Double cantidad, Double costo, Long almacenId, Integer cantreal, String unidad, Long loteId, Long userId, Integer activo, Integer borrado) {
        this.id = id;
        this.entradaStockId = entradaStockId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.costo = costo;
        this.almacenId = almacenId;
        this.cantreal = cantreal;
        this.unidad = unidad;
        this.loteId = loteId;
        this.userId = userId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public DetalleEntradaStock(Long id, Long entradaStockId, Long productoId, Double cantidad, Double costo, Long almacenId, Integer cantreal, String unidad, Long loteId, Long userId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.entradaStockId = entradaStockId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.costo = costo;
        this.almacenId = almacenId;
        this.cantreal = cantreal;
        this.unidad = unidad;
        this.loteId = loteId;
        this.userId = userId;
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

    public Long getEntradaStockId() {
        return entradaStockId;
    }

    public void setEntradaStockId(Long entradaStockId) {
        this.entradaStockId = entradaStockId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Integer getCantreal() {
        return cantreal;
    }

    public void setCantreal(Integer cantreal) {
        this.cantreal = cantreal;
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

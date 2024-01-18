package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Detalle Comprobante Model")
@Entity
@Table(name = "detalles_comprobantes")
public class DetalleComprobante implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="cabecera_id", nullable = true)
    private Long cabeceraId;

    @Column(name="item_orden", nullable = true, length= 45)
    private String itemOrden;

    @Column(name="item_unidad", nullable = true, length= 45)
    private String itemUnidad;

    @Column(name="item_cantidad", nullable = true, length= 45)
    private String itemCantidad;

    @Column(name="item_codproducto", nullable = true, length= 45)
    private String itemCodproducto;

    @Column(name="item_descripcion", nullable = true, length= 225)
    private String itemDescripcion;

    @Column(name="item_afectacion", nullable = true, length= 45)
    private String itemAfectacion;

    @Column(name="item_tipo_precio_venta", nullable = true, length= 45)
    private String itemTipoPrecioVenta;

    @Column(name="item_pventa", nullable = true, length= 45)
    private String itemPventa;

    @Column(name="item_pventa_no_onerosa", nullable = true, length= 45)
    private String itemPventaNoOnerosa;

    @Column(name="item_to_subtotal", nullable = true, length= 45)
    private String itemToSubtotal;

    @Column(name="item_to_igv", nullable = true, length= 45)
    private String itemToIgv;

    @Column(name="item_preunitfin", nullable = true, length= 45)
    private String itemPreunitfin;

    @Column(name="user_id", nullable = true)
    private Long userId;

    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Column(name="activo", nullable = true)
    private Integer activo;

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

    public DetalleComprobante() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCabeceraId() {
        return cabeceraId;
    }

    public void setCabeceraId(Long cabeceraId) {
        this.cabeceraId = cabeceraId;
    }

    public String getItemOrden() {
        return itemOrden;
    }

    public void setItemOrden(String itemOrden) {
        this.itemOrden = itemOrden;
    }

    public String getItemUnidad() {
        return itemUnidad;
    }

    public void setItemUnidad(String itemUnidad) {
        this.itemUnidad = itemUnidad;
    }

    public String getItemCantidad() {
        return itemCantidad;
    }

    public void setItemCantidad(String itemCantidad) {
        this.itemCantidad = itemCantidad;
    }

    public String getItemCodproducto() {
        return itemCodproducto;
    }

    public void setItemCodproducto(String itemCodproducto) {
        this.itemCodproducto = itemCodproducto;
    }

    public String getItemDescripcion() {
        return itemDescripcion;
    }

    public void setItemDescripcion(String itemDescripcion) {
        this.itemDescripcion = itemDescripcion;
    }

    public String getItemAfectacion() {
        return itemAfectacion;
    }

    public void setItemAfectacion(String itemAfectacion) {
        this.itemAfectacion = itemAfectacion;
    }

    public String getItemTipoPrecioVenta() {
        return itemTipoPrecioVenta;
    }

    public void setItemTipoPrecioVenta(String itemTipoPrecioVenta) {
        this.itemTipoPrecioVenta = itemTipoPrecioVenta;
    }

    public String getItemPventa() {
        return itemPventa;
    }

    public void setItemPventa(String itemPventa) {
        this.itemPventa = itemPventa;
    }

    public String getItemPventaNoOnerosa() {
        return itemPventaNoOnerosa;
    }

    public void setItemPventaNoOnerosa(String itemPventaNoOnerosa) {
        this.itemPventaNoOnerosa = itemPventaNoOnerosa;
    }

    public String getItemToSubtotal() {
        return itemToSubtotal;
    }

    public void setItemToSubtotal(String itemToSubtotal) {
        this.itemToSubtotal = itemToSubtotal;
    }

    public String getItemToIgv() {
        return itemToIgv;
    }

    public void setItemToIgv(String itemToIgv) {
        this.itemToIgv = itemToIgv;
    }

    public String getItemPreunitfin() {
        return itemPreunitfin;
    }

    public void setItemPreunitfin(String itemPreunitfin) {
        this.itemPreunitfin = itemPreunitfin;
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

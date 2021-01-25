package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "orden_pedidos")
public class OrdenPedido  implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="fecha_emision", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fechaEmision;

    @Column(name="venta_id", nullable = true)
    private Long ventaId;

    @Column(name="referencia", nullable = true, length= 100)
    private String referencia;

    @Column(name="comprobante_id", nullable = true)
    private Long comprobanteId;

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

    public OrdenPedido() {
    }

    public OrdenPedido(Long id, Date fechaEmision, Long ventaId, String referencia, Long comprobanteId, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.fechaEmision = fechaEmision;
        this.ventaId = ventaId;
        this.referencia = referencia;
        this.comprobanteId = comprobanteId;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public OrdenPedido(Long id, Date fechaEmision, Long ventaId, String referencia, Long comprobanteId, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.fechaEmision = fechaEmision;
        this.ventaId = ventaId;
        this.referencia = referencia;
        this.comprobanteId = comprobanteId;
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

    public Date getFechE() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Long getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(Long comprobanteId) {
        this.comprobanteId = comprobanteId;
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

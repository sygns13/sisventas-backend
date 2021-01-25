package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "ventas")
public class Venta implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="fecha", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fecha;

    @Column(name="cliente_id", nullable = true)
    private Long clienteId;

    @Column(name="comprobante_id", nullable = true)
    private Long comprobanteId;

    @Column(name="subtotal_inafecto", nullable = true)
    private Double subtotalInafecto;

    @Column(name="subtotal_afecto", nullable = true)
    private Double subtotalAfecto;

    @Column(name="igv", nullable = true)
    private Double igv;

    @Column(name="estado", nullable = true)
    private Integer estado;

    @Column(name="pagado", nullable = true)
    private Integer pagado;

    @Column(name="hora", nullable = true)
    //@Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern="HH:mm:ss")
    private Time hora;

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

    public Venta() {
    }

    public Venta(Long id, Date fecha, Long clienteId, Long comprobanteId, Double subtotalInafecto, Double subtotalAfecto, Double igv, Integer estado, Integer pagado, Time hora, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.fecha = fecha;
        this.clienteId = clienteId;
        this.comprobanteId = comprobanteId;
        this.subtotalInafecto = subtotalInafecto;
        this.subtotalAfecto = subtotalAfecto;
        this.igv = igv;
        this.estado = estado;
        this.pagado = pagado;
        this.hora = hora;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public Venta(Long id, Date fecha, Long clienteId, Long comprobanteId, Double subtotalInafecto, Double subtotalAfecto, Double igv, Integer estado, Integer pagado, Time hora, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.fecha = fecha;
        this.clienteId = clienteId;
        this.comprobanteId = comprobanteId;
        this.subtotalInafecto = subtotalInafecto;
        this.subtotalAfecto = subtotalAfecto;
        this.igv = igv;
        this.estado = estado;
        this.pagado = pagado;
        this.hora = hora;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(Long comprobanteId) {
        this.comprobanteId = comprobanteId;
    }

    public Double getSubtotalInafecto() {
        return subtotalInafecto;
    }

    public void setSubtotalInafecto(Double subtotalInafecto) {
        this.subtotalInafecto = subtotalInafecto;
    }

    public Double getSubtotalAfecto() {
        return subtotalAfecto;
    }

    public void setSubtotalAfecto(Double subtotalAfecto) {
        this.subtotalAfecto = subtotalAfecto;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getPagado() {
        return pagado;
    }

    public void setPagado(Integer pagado) {
        this.pagado = pagado;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
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

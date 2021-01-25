package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "comprobantes")
public class Comprobante implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="serie", nullable = true, length= 500)
    private String serie;

    @Column(name="numero", nullable = true, length= 500)
    private String numero;

    @Column(name="cantidad_digitos", nullable = true)
    private Integer cantidadDigitos;

    @Column(name="tipo_comprobante_id", nullable = true)
    private Long tipoComprobanteId;

    @Column(name="estado", nullable = true, length= 45)
    private String estado;

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

    public Comprobante() {
    }

    public Comprobante(Long id, String serie, String numero, Integer cantidadDigitos, Long tipoComprobanteId, String estado, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.serie = serie;
        this.numero = numero;
        this.cantidadDigitos = cantidadDigitos;
        this.tipoComprobanteId = tipoComprobanteId;
        this.estado = estado;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public Comprobante(Long id, String serie, String numero, Integer cantidadDigitos, Long tipoComprobanteId, String estado, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.serie = serie;
        this.numero = numero;
        this.cantidadDigitos = cantidadDigitos;
        this.tipoComprobanteId = tipoComprobanteId;
        this.estado = estado;
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

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getCantidadDigitos() {
        return cantidadDigitos;
    }

    public void setCantidadDigitos(Integer cantidadDigitos) {
        this.cantidadDigitos = cantidadDigitos;
    }

    public Long getTipoComprobanteId() {
        return tipoComprobanteId;
    }

    public void setTipoComprobanteId(Long tipoComprobanteId) {
        this.tipoComprobanteId = tipoComprobanteId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

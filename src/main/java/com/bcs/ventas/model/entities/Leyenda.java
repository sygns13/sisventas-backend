package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "leyendas")
public class Leyenda implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="cabecera_id", nullable = true)
    private Long cabeceraId;

    @Column(name="leyenda_codigo", nullable = true, length= 10)
    private String leyendaCodigo;

    @Column(name="leyenda_texto", nullable = true, length= 500)
    private String leyendaTexto;

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

    public Leyenda() {
    }

    public Leyenda(Long id, Long cabeceraId, String leyendaCodigo, String leyendaTexto, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.cabeceraId = cabeceraId;
        this.leyendaCodigo = leyendaCodigo;
        this.leyendaTexto = leyendaTexto;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public Leyenda(Long id, Long cabeceraId, String leyendaCodigo, String leyendaTexto, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.cabeceraId = cabeceraId;
        this.leyendaCodigo = leyendaCodigo;
        this.leyendaTexto = leyendaTexto;
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

    public Long getCabeceraId() {
        return cabeceraId;
    }

    public void setCabeceraId(Long cabeceraId) {
        this.cabeceraId = cabeceraId;
    }

    public String getLeyendaCodigo() {
        return leyendaCodigo;
    }

    public void setLeyendaCodigo(String leyendaCodigo) {
        this.leyendaCodigo = leyendaCodigo;
    }

    public String getLeyendaTexto() {
        return leyendaTexto;
    }

    public void setLeyendaTexto(String leyendaTexto) {
        this.leyendaTexto = leyendaTexto;
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

package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notas_creditos")
public class NotasCredito implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="cabecera_id", nullable = true)
    private Long cabeceraId;

    @Column(name="docu_numero", nullable = true, length= 50)
    private String docuNumero;

    @Column(name="serie", nullable = true, length= 4)
    private String serie;

    @Column(name="numero", nullable = true)
    private Integer numero;

    @Column(name="codigo", nullable = true, length= 50)
    private String codigo;

    @Column(name="descripcion", nullable = true, length= 500)
    private String descripcion;

    @Column(name="hashcode", nullable = true, length= 100)
    private String hashcode;

    @Column(name="cdr", nullable = true, length= 200)
    private String cdr;

    @Column(name="cdr_nota", nullable = true, length= 500)
    private String cdrNota;

    @Column(name="cdr_observacion", nullable = true, length= 2000)
    private String cdrObservacion;

    @Column(name="docu_enviaws", nullable = true, length= 45)
    private String docuEnviaws;

    @Column(name="docu_proce_status", nullable = true, length= 45)
    private String docuProceStatus;

    @Column(name="tipo", nullable = true, length= 45)
    private String tipo;

    @Column(name="proce_fecha", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date proceFecha;

    @Column(name="tipoanulado", nullable = true, length= 45)
    private String tipoAnulado;

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

    public NotasCredito() {
    }

    public NotasCredito(Long id, Long cabeceraId, String docuNumero, String serie, Integer numero, String codigo, String descripcion, String hashcode, String cdr, String cdrNota, String cdrObservacion, String docuEnviaws, String docuProceStatus, String tipo, Date proceFecha, String tipoAnulado, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.cabeceraId = cabeceraId;
        this.docuNumero = docuNumero;
        this.serie = serie;
        this.numero = numero;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.hashcode = hashcode;
        this.cdr = cdr;
        this.cdrNota = cdrNota;
        this.cdrObservacion = cdrObservacion;
        this.docuEnviaws = docuEnviaws;
        this.docuProceStatus = docuProceStatus;
        this.tipo = tipo;
        this.proceFecha = proceFecha;
        this.tipoAnulado = tipoAnulado;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public NotasCredito(Long id, Long cabeceraId, String docuNumero, String serie, Integer numero, String codigo, String descripcion, String hashcode, String cdr, String cdrNota, String cdrObservacion, String docuEnviaws, String docuProceStatus, String tipo, Date proceFecha, String tipoAnulado, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.cabeceraId = cabeceraId;
        this.docuNumero = docuNumero;
        this.serie = serie;
        this.numero = numero;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.hashcode = hashcode;
        this.cdr = cdr;
        this.cdrNota = cdrNota;
        this.cdrObservacion = cdrObservacion;
        this.docuEnviaws = docuEnviaws;
        this.docuProceStatus = docuProceStatus;
        this.tipo = tipo;
        this.proceFecha = proceFecha;
        this.tipoAnulado = tipoAnulado;
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

    public String getDocuNumero() {
        return docuNumero;
    }

    public void setDocuNumero(String docuNumero) {
        this.docuNumero = docuNumero;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getCdr() {
        return cdr;
    }

    public void setCdr(String cdr) {
        this.cdr = cdr;
    }

    public String getCdrNota() {
        return cdrNota;
    }

    public void setCdrNota(String cdrNota) {
        this.cdrNota = cdrNota;
    }

    public String getCdrObservacion() {
        return cdrObservacion;
    }

    public void setCdrObservacion(String cdrObservacion) {
        this.cdrObservacion = cdrObservacion;
    }

    public String getDocuEnviaws() {
        return docuEnviaws;
    }

    public void setDocuEnviaws(String docuEnviaws) {
        this.docuEnviaws = docuEnviaws;
    }

    public String getDocuProceStatus() {
        return docuProceStatus;
    }

    public void setDocuProceStatus(String docuProceStatus) {
        this.docuProceStatus = docuProceStatus;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getProceFecha() {
        return proceFecha;
    }

    public void setProceFecha(Date proceFecha) {
        this.proceFecha = proceFecha;
    }

    public String getTipoAnulado() {
        return tipoAnulado;
    }

    public void setTipoAnulado(String tipoAnulado) {
        this.tipoAnulado = tipoAnulado;
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

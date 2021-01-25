package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "resumen_boletas")
public class ResumenBoleta implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="razonsocial", nullable = true, length= 2000)
    private String razonsocial;

    @Column(name="ruc", nullable = true, length= 15)
    private String ruc;

    @Column(name="fecha_emision", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fechaEmision;

    @Column(name="fecha_envio", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fechaEnvio;

    @Column(name="enviar", nullable = true, length= 45)
    private String enviar;

    @Column(name="docstatus", nullable = true, length= 45)
    private String docstatus;

    @Column(name="hashcode", nullable = true, length= 500)
    private String hashcode;

    @Column(name="cdr", nullable = true, length= 500)
    private String cdr;

    @Column(name="cdr_nota", nullable = true, length= 2000)
    private String cdrNota;

    @Column(name="docu_link_pdf", nullable = true, length= 2000)
    private String docuLinkPdf;

    @Column(name="docu_link_cdr", nullable = true, length= 2000)
    private String docuLinkCdr;

    @Column(name="docu_link_xml", nullable = true, length= 2000)
    private String docuLinkXml;

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

    public ResumenBoleta() {
    }

    public ResumenBoleta(Long id, String razonsocial, String ruc, Date fechaEmision, Date fechaEnvio, String enviar, String docstatus, String hashcode, String cdr, String cdrNota, String docuLinkPdf, String docuLinkCdr, String docuLinkXml, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.razonsocial = razonsocial;
        this.ruc = ruc;
        this.fechaEmision = fechaEmision;
        this.fechaEnvio = fechaEnvio;
        this.enviar = enviar;
        this.docstatus = docstatus;
        this.hashcode = hashcode;
        this.cdr = cdr;
        this.cdrNota = cdrNota;
        this.docuLinkPdf = docuLinkPdf;
        this.docuLinkCdr = docuLinkCdr;
        this.docuLinkXml = docuLinkXml;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public ResumenBoleta(Long id, String razonsocial, String ruc, Date fechaEmision, Date fechaEnvio, String enviar, String docstatus, String hashcode, String cdr, String cdrNota, String docuLinkPdf, String docuLinkCdr, String docuLinkXml, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.razonsocial = razonsocial;
        this.ruc = ruc;
        this.fechaEmision = fechaEmision;
        this.fechaEnvio = fechaEnvio;
        this.enviar = enviar;
        this.docstatus = docstatus;
        this.hashcode = hashcode;
        this.cdr = cdr;
        this.cdrNota = cdrNota;
        this.docuLinkPdf = docuLinkPdf;
        this.docuLinkCdr = docuLinkCdr;
        this.docuLinkXml = docuLinkXml;
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

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getEnviar() {
        return enviar;
    }

    public void setEnviar(String enviar) {
        this.enviar = enviar;
    }

    public String getDocstatus() {
        return docstatus;
    }

    public void setDocstatus(String docstatus) {
        this.docstatus = docstatus;
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

    public String getDocuLinkPdf() {
        return docuLinkPdf;
    }

    public void setDocuLinkPdf(String docuLinkPdf) {
        this.docuLinkPdf = docuLinkPdf;
    }

    public String getDocuLinkCdr() {
        return docuLinkCdr;
    }

    public void setDocuLinkCdr(String docuLinkCdr) {
        this.docuLinkCdr = docuLinkCdr;
    }

    public String getDocuLinkXml() {
        return docuLinkXml;
    }

    public void setDocuLinkXml(String docuLinkXml) {
        this.docuLinkXml = docuLinkXml;
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

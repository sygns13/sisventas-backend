package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "datos_users")
public class DatosUser implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombres", nullable = true, length= 500)
    private String nombres;

    @Column(name="apellido_paterno", nullable = true, length= 250)
    private String apellidoPaterno;

    @Column(name="apellido_materno", nullable = true, length= 250)
    private String apellidoMaterno;

    @Column(name="direccion", nullable = true, length= 45)
    private String direccion;

    @Column(name="telefono", nullable = true, length= 45)
    private String telefono;

    @Column(name="tipo_documento_id", nullable = true)
    private Long tipoDocumentoId;

    @Column(name="documento", nullable = true, length= 45)
    private String documento;

    @Column(name="email", nullable = true, length= 500)
    private String email;

    @Column(name="user_id", nullable = true)
    private Long userId;

    @Column(name="user_gestiona_id", nullable = true)
    private Long userGestionaId;

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

    public DatosUser() {
    }

    public DatosUser(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String direccion, String telefono, Long tipoDocumentoId, String documento, String email, Long userId, Long userGestionaId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoDocumentoId = tipoDocumentoId;
        this.documento = documento;
        this.email = email;
        this.userId = userId;
        this.userGestionaId = userGestionaId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public DatosUser(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String direccion, String telefono, Long tipoDocumentoId, String documento, String email, Long userId, Long userGestionaId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoDocumentoId = tipoDocumentoId;
        this.documento = documento;
        this.email = email;
        this.userId = userId;
        this.userGestionaId = userGestionaId;
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

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(Long tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserGestionaId() {
        return userGestionaId;
    }

    public void setUserGestionaId(Long userGestionaId) {
        this.userGestionaId = userGestionaId;
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

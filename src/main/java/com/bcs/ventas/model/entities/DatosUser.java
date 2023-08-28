package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Datos User Model")
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

    @Schema(description = "Nombres del Usuario")
    @NotNull(message = "{datos_users.nombres.notnull}")
    @Size(min = 1, max = 500, message = "{datos_users.nombres.size}")
    @Column(name = "nombres", nullable = true, length = 500)
    private String nombres;

    @Schema(description = "Apellido Paterno del Usuario")
    @NotNull(message = "{datos_users.apellido_paterno.notnull}")
    @Size(min = 1, max = 250, message = "{datos_users.apellido_paterno.size}")
    @Column(name = "apellido_paterno", nullable = true, length = 250)
    private String apellidoPaterno;

    @Schema(description = "Apellido Materno del Usuario")
    @NotNull(message = "{datos_users.apellido_materno.notnull}")
    @Size(min = 1, max = 250, message = "{datos_users.apellido_materno.size}")
    @Column(name = "apellido_materno", nullable = true, length = 250)
    private String apellidoMaterno;

    @Schema(description = "Dirección")
    @Size(max = 500, message = "{datos_users.direccion.size}")
    @Column(name = "direccion", nullable = true, length = 500)
    private String direccion;

    @Schema(description = "Telefono")
    @Size(max = 45, message = "{datos_users.telefono.size}")
    @Column(name = "telefono", nullable = true, length = 45)
    private String telefono;

    @Schema(description = "Tipo de Documento de Identidad")
    @ManyToOne
    @JoinColumn(name = "tipo_documento_id", nullable = false, foreignKey = @ForeignKey(name = "FK_tipodocumento_datouser"))
    private TipoDocumento tipoDocumento;

    @Schema(description = "Documento de Identidad")
    @NotNull(message = "{datos_users.documento.notnull}")
    @Size(max = 45, message = "{datos_users.documento.size}")
    @Column(name = "documento", nullable = true, length = 45)
    private String documento;

    @Schema(description = "Email de Datos Usuario")
    @Size(max = 500, message = "{datos_users.email.size}")
    @Email(message = "{datos_users.email.email}")
    @Column(name = "email", nullable = true, length = 500)
    private String email;

    @Schema(description = "ID User")
    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID User Padre")
    @Column(name = "user_gestiona_id", nullable = true)
    private Long userGestionaId;

    @Schema(description = "ID Empresa Padre")
    @Column(name = "empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado de la Empresa")
    @Column(name = "activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de la Empresa")
    @Column(name = "borrado", nullable = true)
    private Integer borrado;

    @Schema(description = "Fecha de Creación del Registro")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de Update del Registro")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAd;

    public DatosUser() {
    }

    public DatosUser(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String direccion, String telefono, TipoDocumento tipoDocumento, String documento, String email, Long userId, Long userGestionaId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.email = email;
        this.userId = userId;
        this.userGestionaId = userGestionaId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public DatosUser(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String direccion, String telefono, TipoDocumento tipoDocumento, String documento, String email, Long userId, Long userGestionaId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoDocumento = tipoDocumento;
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

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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

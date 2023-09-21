package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "User Model")
@Entity
@Table(name = "users")
public class User implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del Usuario")
    @NotNull( message = "{users.name.notnull}")
    @Size(min = 1, max = 250, message = "{users.name.size}")
    @Column(name="name", nullable = true, length = 250)
    private String name;

    @Schema(description = "Email del Usuario")
    @NotNull( message = "{users.email.notnull}")
    @Size(min = 1, max = 500, message = "{users.email.size}")
    @Email(message = "{users.email.email}")
    @Column(name="email", nullable = true, length = 500)
    private String email;

    @Schema(description = "Password del Usuario")
    @NotNull( message = "{users.password.notnull}")
    @Size(min = 1, max = 500, message = "{users.password.size}")
    @Column(name="password", nullable = true, length = 500)
    private String password;

    @Schema(description = "Remember Token OLD")
    @Column(name="remember_token", nullable = true, length = 500)
    private String remember_token;

    //@Column(name="tipo_user_id", nullable = true)
    //private Long tipoUserId;

    @Schema(description = "Tipo de usuario")
    @ManyToOne
    @JoinColumn(name = "tipo_user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_tipouser_user"))
    private TipoUser tipoUser;

    @Schema(description = "Verificacion de Token OLD")
    @Column(name="verification_token", nullable = true, length = 500)
    private String verificationToken;

    @Schema(description = "ID Empresa Padre")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Almacen del Usuario")
    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    @Schema(description = "ID User Padre")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "Estado de Usuario")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de Usuario")
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

    @Schema(description = "Datos del Usuario")
    @Transient
    private DatosUser datos;

    public User() {
    }

    public User(Long id, String name, String email, String password, String remember_token, TipoUser tipoUser, String verificationToken, Long empresaId, Long userId, Integer activo, Integer borrado) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.remember_token = remember_token;
        this.tipoUser = tipoUser;
        this.verificationToken = verificationToken;
        this.empresaId = empresaId;
        this.userId = userId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public User(Long id, String name, String email, String password, String remember_token, TipoUser tipoUser, String verificationToken, Long empresaId, Long userId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.remember_token = remember_token;
        this.tipoUser = tipoUser;
        this.verificationToken = verificationToken;
        this.empresaId = empresaId;
        this.userId = userId;
        this.activo = activo;
        this.borrado = borrado;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
    }

    public User(Long id, String name, String email, String password, String remember_token, TipoUser tipoUser, String verificationToken, Long empresaId, Long userId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd, DatosUser datos) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.remember_token = remember_token;
        this.tipoUser = tipoUser;
        this.verificationToken = verificationToken;
        this.empresaId = empresaId;
        this.userId = userId;
        this.activo = activo;
        this.borrado = borrado;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
        this.datos = datos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public TipoUser getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(TipoUser tipoUser) {
        this.tipoUser = tipoUser;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
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

    public DatosUser getDatos() {
        return datos;
    }

    public void setDatos(DatosUser datos) {
        this.datos = datos;
    }
}

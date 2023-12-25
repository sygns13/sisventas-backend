package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Configurations Model")
@Entity
@Table(name = "configs")
public class Config implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull( message = "{configs.id.notnull}")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Schema(description = "Nombre de la Configuración")
    @NotNull( message = "{configs.nombre.notnull}")
    @Size(min = 1, max = 500, message = "{configs.nombre.size}")
    @Column(name="nombre", nullable = true, length= 500)
    private String nombre;

    @Schema(description = "Nivel de la Configuración")  // 0: General, 1: Empresa, 2: Usuario
    //@NotNull( message = "{almacen.activo.notnull}")
    //@Min(value = 0, message = "{almacen.activo.min}")
    //@Max(value = 2, message = "{almacen.activo.max}")
    @Column(name="nivel", nullable = true)
    private Integer nivel;

    @Schema(description = "Valor de la Configuración")
    @NotNull( message = "{configs.valor.notnull}")
    @Size(min = 1, max = 500, message = "{configs.valor.size}")
    @Column(name="valor", nullable = true, length= 500)
    private String valor;

    @Schema(description = "ID User Padre")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{banco.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado de Banco")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de Banco")
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

    public Config() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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

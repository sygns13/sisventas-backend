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

@Schema(description = "Tipo de Usuario Model")
@Entity
@Table(name = "tipo_users")
public class TipoUser implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del Tipo de Usuario")
    @NotNull( message = "{tipo_users.nombre.notnull}")
    @Size(min = 1, max = 250, message = "{tipo_users.nombre.size}")
    @Column(name="nombre", nullable = true, length = 200)
    private String nombre;

    @Schema(description = "Descripción del Tipo de Usuario")
    @Size(max = 5000, message = "{tipo_users.descripcion.size}")
    @Column(name="descripcion", nullable = true)
    private String descripcion;

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

    public TipoUser() {
    }

    public TipoUser(Long id, String nombre, String descripcion, Integer activo, Integer borrado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.borrado = borrado;
    }

    public TipoUser(Long id, String nombre, String descripcion, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

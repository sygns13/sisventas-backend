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

@Schema(description = "Tipo Documento Model")
@Entity
@Table(name = "tipo_documentos")
public class TipoDocumento implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Tipo de Documento")
    @NotNull( message = "{tipoDocumento.tipo.notnull}")
    @Size(min = 1, max = 500, message = "{tipoDocumento.tipo.size}")
    @Column(name="tipo", nullable = true, length = 500)
    private String tipo;

    @Schema(description = "Key del Documento")
    @NotNull( message = "{tipoDocumento.key.notnull}")
    @Size(min = 1, max = 25, message = "{tipoDocumento.key.size}")
    @Column(name="key", nullable = true, length = 25)
    private String key;

    @Schema(description = "Descripción del Documento")
    @NotNull( message = "{tipoDocumento.descripcion.notnull}")
    @Size(min = 1, max = 500, message = "{tipoDocumento.descripcion.size}")
    @Column(name="descripcion", nullable = true, length = 500)
    private String descripcion;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{producto.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "Estado de Producto")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de Producto")
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

    public TipoDocumento() {
    }

    public TipoDocumento(Long id, String tipo, String key, String descripcion, Long userId, Integer activo, Integer borrado) {
        this.id = id;
        this.tipo = tipo;
        this.key = key;
        this.descripcion = descripcion;
        this.userId = userId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public TipoDocumento(Long id, String tipo, String key, String descripcion, Long userId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.tipo = tipo;
        this.key = key;
        this.descripcion = descripcion;
        this.userId = userId;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
}

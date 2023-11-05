package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "Metodo de Pago Model")
@Entity
@Table(name = "metodos_pagos")
public class MetodoPago {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del Metodo de Pago")
    @NotNull( message = "{metodos_pagos.nombre.notnull}")
    @Size(min = 1, max = 250, message = "{metodos_pagos.nombre.size}")
    @Column(name="nombre", nullable = true, length = 250)
    private String nombre;

    @Schema(description = "ID de Tipo del Metodo de Pago")
    @NotNull( message = "{metodos_pagos.tipo_id.notnull}")
    @Size(min = 1, max = 20, message = "{metodos_pagos.tipo_id.size}")
    @Column(name="tipo_id", nullable = true, length = 20)
    private String tipoId;

    @Schema(description = "Tipo del Metodo de Pago")
    @NotNull( message = "{metodos_pagos.tipo.notnull}")
    @Size(min = 1, max = 50, message = "{metodos_pagos.tipo.size}")
    @Column(name="tipo", nullable = true, length = 50)
    private String tipo;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{unidad.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{unidad.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado de la Unidad")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de la Unidad")
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

    public MetodoPago() {
    }

    public MetodoPago(Long id, String nombre, String tipoId, String tipo, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.nombre = nombre;
        this.tipoId = tipoId;
        this.tipo = tipo;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public MetodoPago(Long id, String nombre, String tipoId, String tipo, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.nombre = nombre;
        this.tipoId = tipoId;
        this.tipo = tipo;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoId() {
        return tipoId;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

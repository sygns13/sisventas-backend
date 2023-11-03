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

@Schema(description = "Tipo Comprobante Model")
@Entity
@Table(name = "tipo_comprobantes")
public class TipoComprobante implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del Tipo de Comprobante")
    @NotNull( message = "{tipo_comprobante.nombre.notnull}")
    @Size(min = 1, max = 200, message = "{tipo_comprobante.nombre.size}")
    @Column(name="nombre", nullable = true, length= 200)
    private String nombre;

    @Schema(description = "Indicador si el Tipo Comprobante es para venta")
    @NotNull( message = "{tipo_comprobante.para_venta.notnull}")
    @Column(name="para_venta", nullable = true)
    private Integer paraVenta;

    @Schema(description = "Prefijo del Tipo de Comprobante")
    @NotNull( message = "{tipo_comprobante.prefix.notnull}")
    @Size(min = 1, max = 2, message = "{tipo_comprobante.prefix.size}")
    @Column(name="prefix", nullable = true, length= 200)
    private String prefix;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{producto.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{producto.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

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



    public TipoComprobante() {
    }

    public TipoComprobante(Long id, String nombre, String prefix, Integer paraVenta, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.nombre = nombre;
        this.prefix = prefix;
        this.paraVenta = paraVenta;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public TipoComprobante(Long id, String nombre, String prefix, Integer paraVenta, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.nombre = nombre;
        this.prefix = prefix;
        this.paraVenta = paraVenta;
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

    public Integer getParaVenta() {
        return paraVenta;
    }

    public void setParaVenta(Integer paraVenta) {
        this.paraVenta = paraVenta;
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

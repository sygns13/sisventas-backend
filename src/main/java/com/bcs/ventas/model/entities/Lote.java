package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Schema(description = "Lote Model")
@Entity
@Table(name = "lotes")
public class Lote implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del Lote")
    @NotNull( message = "{lote.nombre.notnull}")
    @Size(min = 1, max = 150, message = "{lote.nombre.size}")
    @Column(name="nombre", nullable = true, length= 250)
    private String nombre;

    @Schema(description = "Fecha de Ingreso de Lote")
    @NotNull( message = "{lote.fecha_ingreso.notnull}")
    @Column(name="fecha_ingreso", nullable = true)
    private LocalDate fechaIngreso;

    @Schema(description = "Fecha de Vencimiento de Lote")
    @Column(name="fecha_vencimiento", nullable = true)
    private LocalDate fechaVencimiento;

    @Schema(description = "Indicador de uso de fecha de vencimiento")
    @NotNull( message = "{lote.activoVencimiento.notnull}")
    @Column(name="activo_vencimiento", nullable = true)
    private Integer activoVencimiento;

    @Schema(description = "Producto de Lote")
    @NotNull( message = "{lote.producto_id.notnull}")
    @Column(name="producto_id", nullable = true)
    private Long productoId;

    @Schema(description = "Cantidad de Unidades de Producto por Lote")
    @NotNull( message = "{lote.cantidad.notnull}")
    @DecimalMin(value = "0.00", message = "{lote.cantidad.min}")
    //@DecimalMax(value = "999999999", message = "{lote.cantidad.max}")
    @Column(name="cantidad", nullable = true)
    private Double cantidad;

    @Schema(description = "Observación de Lote")
    @Column(name="observacion", nullable = true)
    private String observacion;

    @Schema(description = "ID del Local")
    @NotNull( message = "{lote.almacen_id.notnull}")
    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    @Schema(description = "ID de la Unidad")
    //@NotNull( message = "{lote.unidad_id.notnull}")
    @Column(name="unidad_id", nullable = true)
    private Long unidadId;

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

    public Lote() {
    }

    public Lote(Long id, String nombre, LocalDate fechaIngreso, LocalDate fechaVencimiento, Integer activoVencimiento, Long productoId, Double cantidad, String observacion, Long almacenId, Long unidadId, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.nombre = nombre;
        this.fechaIngreso = fechaIngreso;
        this.fechaVencimiento = fechaVencimiento;
        this.activoVencimiento = activoVencimiento;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.observacion = observacion;
        this.almacenId = almacenId;
        this.unidadId = unidadId;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public Lote(Long id, String nombre, LocalDate fechaIngreso, LocalDate fechaVencimiento, Integer activoVencimiento, Long productoId, Double cantidad, String observacion, Long almacenId, Long unidadId, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.nombre = nombre;
        this.fechaIngreso = fechaIngreso;
        this.fechaVencimiento = fechaVencimiento;
        this.activoVencimiento = activoVencimiento;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.observacion = observacion;
        this.almacenId = almacenId;
        this.unidadId = unidadId;
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

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getActivoVencimiento() {
        return activoVencimiento;
    }

    public void setActivoVencimiento(Integer activoVencimiento) {
        this.activoVencimiento = activoVencimiento;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
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

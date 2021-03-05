package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Schema(description = "Retiro Entrada Producto Model")
@Entity
@Table(name = "retiro_entrada_productos")
public class RetiroEntradaProducto implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Fecha de Entrada - Retiro de Producto")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    @Schema(description = "Motivo de Entrada - Retiro de Producto")
    @Column(name="motivo", nullable = true)
    private String motivo;

    @Schema(description = "Id Lote Padre")
    @Column(name="lote_id", nullable = true)
    private Long loteId;

    @Schema(description = "Hora de Entrada - Retiro de Producto")
    @Column(name="hora", nullable = true)
    //@Temporal(TemporalType.TIME)
    //@DateTimeFormat(pattern="HH:mm:ss")
    private LocalTime hora;

    @Schema(description = "Cantidad Real de Entrada - Retiro de Producto")
    @Column(name="cantidad_real", nullable = true)
    private Double cantidadReal;

    @Schema(description = "Tipo de Entrada - Retiro de Producto")
    @Column(name="tipo", nullable = true)
    private Integer tipo;

    @Schema(description = "Id Almacen Padre")
    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    @Schema(description = "Id Producto Padre")
    @Column(name="producto_id", nullable = true)
    private Long productoId;

    @Schema(description = "Id User Padre")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "Id Empresa Padre")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado Activo")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Estado Borrado")
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

    public RetiroEntradaProducto() {
    }

    public RetiroEntradaProducto(Long id, LocalDate fecha, String motivo, Long loteId, LocalTime hora, Double cantidadReal, Integer tipo, Long almacenId, Long productoId, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.fecha = fecha;
        this.motivo = motivo;
        this.loteId = loteId;
        this.hora = hora;
        this.cantidadReal = cantidadReal;
        this.tipo = tipo;
        this.almacenId = almacenId;
        this.productoId = productoId;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getLoteId() {
        return loteId;
    }

    public void setLoteId(Long loteId) {
        this.loteId = loteId;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Double getCantidadReal() {
        return cantidadReal;
    }

    public void setCantidadReal(Double cantidadReal) {
        this.cantidadReal = cantidadReal;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
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

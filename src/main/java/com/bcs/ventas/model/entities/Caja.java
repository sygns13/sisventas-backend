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

@Schema(description = "Caja Model")
@Entity
@Table(name = "cajas")
public class Caja implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre de la Caja")
    @NotNull( message = "{caja.nombre.notnull}")
    @Size(min = 1, max = 200, message = "{caja.nombre.size}")
    @Column(name="nombre", nullable = true, length= 200)
    private String nombre;

    @Schema(description = "Almacen de la caja")
    @ManyToOne
    @JoinColumn(name = "almacen_id", nullable = false, foreignKey = @ForeignKey(name = "FK_almacen_caja"))
    private Almacen almacen;

    @Schema(description = "Id del Usuario que bloquea la caja")
    @Column(name="locked_by", nullable = true)
    private Long lockedBy;

    @Schema(description = "Último Balance")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="last_balanced", nullable = true)
    private LocalDateTime lastBalanced;

    @Schema(description = "Id del Usuario que hace el Último Balance")
    @Column(name="last_balanced_by", nullable = true)
    private Long lastBalancedBy;

    @Schema(description = "Último Seteo de la Caja")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="last_settled", nullable = true)
    private LocalDateTime lastSettled;

    @Schema(description = "Id del Usuario que hace el ultimo Seteo de la Caja")
    @Column(name="last_settled_by", nullable = true)
    private Long lastSettledBy;

    @Schema(description = "Último uso de la Caja")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="last_used", nullable = true)
    private LocalDateTime lastUsed;

    @Schema(description = "Id del Último Usuario que uso la caja")
    @Column(name="last_used_by", nullable = true)
    private Long lastUsedBy;

    @Schema(description = "Estado de la Caja")
    @Column(name="estado", nullable = true)
    private Integer estado;

    @Schema(description = "Id del Usuario que creó la Caja")
    @Column(name="is_created_by", nullable = true)
    private Long isCreatedBy;

    @Schema(description = "Indicar si la caja se encuentra balanceada")
    @Column(name="is_balanced", nullable = true)
    private Integer isBalanced;

    @Schema(description = "Último seteo de la caja por el sistema")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="last_system_settled", nullable = true)
    private LocalDateTime lastSystemSettled;

    @Schema(description = "Moneda de la Caja")
    //@NotNull( message = "{caja.currency_code.notnull}")
    //@Size(min = 1, max = 3, message = "{caja.currency_code.size}")
    @Column(name="currency_code", nullable = true, length= 3)
    private String currencyCode;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{producto.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado de la Caja")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de la Caja")
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


    @Transient
    private Integer countIdx;


    public Caja() {
    }

    public Integer getCountIdx() {
        return countIdx;
    }

    public void setCountIdx(Integer countIdx) {
        this.countIdx = countIdx;
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

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Long getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(Long lockedBy) {
        this.lockedBy = lockedBy;
    }

    public LocalDateTime getLastBalanced() {
        return lastBalanced;
    }

    public void setLastBalanced(LocalDateTime lastBalanced) {
        this.lastBalanced = lastBalanced;
    }

    public Long getLastBalancedBy() {
        return lastBalancedBy;
    }

    public void setLastBalancedBy(Long lastBalancedBy) {
        this.lastBalancedBy = lastBalancedBy;
    }

    public LocalDateTime getLastSettled() {
        return lastSettled;
    }

    public void setLastSettled(LocalDateTime lastSettled) {
        this.lastSettled = lastSettled;
    }

    public Long getLastSettledBy() {
        return lastSettledBy;
    }

    public void setLastSettledBy(Long lastSettledBy) {
        this.lastSettledBy = lastSettledBy;
    }

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }

    public Long getLastUsedBy() {
        return lastUsedBy;
    }

    public void setLastUsedBy(Long lastUsedBy) {
        this.lastUsedBy = lastUsedBy;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Long getIsCreatedBy() {
        return isCreatedBy;
    }

    public void setIsCreatedBy(Long isCreatedBy) {
        this.isCreatedBy = isCreatedBy;
    }

    public Integer getIsBalanced() {
        return isBalanced;
    }

    public void setIsBalanced(Integer isBalanced) {
        this.isBalanced = isBalanced;
    }

    public LocalDateTime getLastSystemSettled() {
        return lastSystemSettled;
    }

    public void setLastSystemSettled(LocalDateTime lastSystemSettled) {
        this.lastSystemSettled = lastSystemSettled;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

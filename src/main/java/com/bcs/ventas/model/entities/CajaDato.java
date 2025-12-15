package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Caja Datos Model")
@Entity
@Table(name = "caja_datos")
public class CajaDato implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Caja")
    @ManyToOne
    @JoinColumn(name = "caja_id", nullable = false, foreignKey = @ForeignKey(name = "FK_caja_datos_caja"))
    private Caja caja;

    @Schema(description = "Fecha de Data de Caja")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    @Schema(description = "Fecha y Hora de Inicio")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="fecha_inicio", nullable = true)
    private LocalDateTime fechaInicio;


    @Schema(description = "Fecha y Hora de Final ")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="fecha_final", nullable = true)
    private LocalDateTime fechaFinal;

    @Schema(description = "Monto de Inicio")
    @Column(name="monto_inicio", nullable = true)
    private BigDecimal montoInicio;

    @Schema(description = "Monto Final")
    @Column(name="monto_final", nullable = true)
    private BigDecimal montoFinal;

    @Schema(description = "Monto Final Calculado")
    @Column(name="monto_final_calc", nullable = true)
    private BigDecimal montoFinalCalc;

    @Schema(description = "Monto Final Diferencia")
    @Column(name="monto_final_dif", nullable = true)
    private BigDecimal montoFinalDif;

    @Schema(description = "monto Temporal")
    @Transient
    private BigDecimal montoTemporal;

    @Schema(description = "User ID que hace el Último edit de la tabla")
    @Column(name="last_user_id", nullable = true)
    private Long lastUserId;

    @Schema(description = "Conteo de Accesos a la Caja en la fecha aperturada")
    @Column(name="access_count", nullable = true)
    private Integer accessCount;

    @Schema(description = "Fecha de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="created_at", nullable = true)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de Update del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="updated_at", nullable = true)
    private LocalDateTime updatedAd;

    @Schema(description = "Estado de la Caja")
    @Column(name="estado", nullable = true)
    private Integer estado;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{producto.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Sustento de Inicio de Caja")
    @Column(name="sustento_ini", nullable = true)
    private String sustentoInicio;

    @Schema(description = "Sustento de Cierre de Caja")
    @Column(name="sustento_fin", nullable = true)
    private String sustentoCierre;

    public CajaDato() {
    }

    public BigDecimal getMontoFinalCalc() {
        return montoFinalCalc;
    }

    public void setMontoFinalCalc(BigDecimal montoFinalCalc) {
        this.montoFinalCalc = montoFinalCalc;
    }

    public BigDecimal getMontoFinalDif() {
        return montoFinalDif;
    }

    public void setMontoFinalDif(BigDecimal montoFinalDif) {
        this.montoFinalDif = montoFinalDif;
    }

    public BigDecimal getMontoTemporal() {
        return montoTemporal;
    }

    public void setMontoTemporal(BigDecimal montoTemporal) {
        this.montoTemporal = montoTemporal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public BigDecimal getMontoInicio() {
        return montoInicio;
    }

    public void setMontoInicio(BigDecimal montoInicio) {
        this.montoInicio = montoInicio;
    }

    public BigDecimal getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(BigDecimal montoFinal) {
        this.montoFinal = montoFinal;
    }

    public Long getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(Long lastUserId) {
        this.lastUserId = lastUserId;
    }

    public Integer getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getSustentoInicio() {
        return sustentoInicio;
    }

    public void setSustentoInicio(String sustentoInicio) {
        this.sustentoInicio = sustentoInicio;
    }

    public String getSustentoCierre() {
        return sustentoCierre;
    }

    public void setSustentoCierre(String sustentoCierre) {
        this.sustentoCierre = sustentoCierre;
    }
}

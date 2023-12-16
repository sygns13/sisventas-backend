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
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Schema(description = "Ingreso y Salidas de Caja Model")
@Entity
@Table(name = "ingreso_salida_cajas")
public class IngresoSalidaCaja implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Monto del Movimiento de Caja")
    @NotNull( message = "{ingreso_salida_cajas.monto.notnull}")
    @DecimalMin(value = "0.01", message = "{ingreso_salida_cajas.monto.min}")
    @DecimalMax(value = "999999999", message = "{ingreso_salida_cajas.monto.max}")
    @Column(name="monto", nullable = true)
    private BigDecimal monto;

    @Schema(description = "Concepto del Movimiento de Caja")
    @NotNull( message = "{ingreso_salida_cajas.concepto.notnull}")
    @Size(min = 1, max = 500, message = "{ingreso_salida_cajas.concepto.size}")
    @Column(name="concepto", nullable = true, length= 500)
    private String concepto;

    @Schema(description = "Comprobante del Movimiento de Caja")
    @Column(name="comprobante", nullable = true, length= 100)
    private String comprobante;

    @Schema(description = "Fecha del Movimiento de Caja")
    @NotNull( message = "{ingreso_salida_cajas.fecha.notnull}")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    @Schema(description = "Almacen donde se realizó el Movimiento de Caja")
    @ManyToOne
    @JoinColumn(name = "almacen_id", nullable = false, foreignKey = @ForeignKey(name = "FK_almacen_venta"))
    private Almacen almacen;

    @Schema(description = "Tipo de Movimiento de Caja")
    @NotNull( message = "{ingreso_salida_cajas.tipo.notnull}")
    @Column(name="tipo", nullable = true)
    private Integer tipo;

    @Schema(description = "Tipo de Comprovante de Movimiento de Caja")
    @NotNull( message = "{ingreso_salida_cajas.tipo_comprobante.notnull}")
    @Column(name="tipo_comprobante", nullable = true)
    private Integer tipoComprobante;

    @Schema(description = "Hora del Movimiento de Caja")
    @NotNull( message = "{ingreso_salida_cajas.hora.notnull}")
    @Column(name="hora", nullable = true)
    @JsonFormat(pattern="HH:mm")
    //@Temporal(TemporalType.TIME)
    //@DateTimeFormat(pattern="HH:mm:ss")
    private LocalTime hora;

    @Schema(description = "User que se realizó la Venta")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_user_venta"))
    private User user;

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
    public IngresoSalidaCaja() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(Integer tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

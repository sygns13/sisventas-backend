package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Schema(description = "Venta Model")
@Entity
@Table(name = "ventas")
public class Venta implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Fecha de registro de la Venta")
    //@NotNull( message = "{producto.fecha.notnull}")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    //@Column(name="cliente_id", nullable = true)
    //private Long clienteId;
    @Schema(description = "Cliente de la Venta")
    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "FK_cliente_venta"))
    private Cliente cliente;

    //@Column(name="comprobante_id", nullable = true)
    //private Long comprobanteId;
    @Schema(description = "Comprobante de la Venta")
    @OneToOne
    @JoinColumn(name = "comprobante_id", nullable = true, foreignKey = @ForeignKey(name = "FK_comprobante_venta"))
    private Comprobante comprobante;


    @Schema(description = "Monto Inafecto de la Venta")
    @Column(name="subtotal_inafecto", nullable = true)
    private Double subtotalInafecto;

    @Schema(description = "Monto Afecto de la Venta")
    @Column(name="subtotal_afecto", nullable = true)
    private Double subtotalAfecto;

    @Schema(description = "Monto IGV de la Venta")
    @Column(name="igv", nullable = true)
    private Double igv;

    @Schema(description = "Estado de la Venta")
    @Column(name="estado", nullable = true)
    private Integer estado;

    @Schema(description = "Estado de Pago de la Venta")
    @Column(name="pagado", nullable = true)
    private Integer pagado;

    @Schema(description = "Hora de la Venta")
    @Column(name="hora", nullable = true)
    //@Temporal(TemporalType.TIME)
    //@DateTimeFormat(pattern="HH:mm:ss")
    private LocalTime hora;

    @Schema(description = "Tipo de Venta")
    @Column(name="tipo", nullable = true)
    private Integer tipo;

    @Schema(description = "ID del Local")
    @NotNull( message = "{venta.almacen_id.notnull}")
    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

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

    public Venta() {
    }

    public Venta(Long id, LocalDate fecha, Cliente cliente, Comprobante comprobante, Double subtotalInafecto, Double subtotalAfecto, Double igv, Integer estado, Integer pagado, LocalTime hora, Integer tipo, Long almacenId, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.comprobante = comprobante;
        this.subtotalInafecto = subtotalInafecto;
        this.subtotalAfecto = subtotalAfecto;
        this.igv = igv;
        this.estado = estado;
        this.pagado = pagado;
        this.hora = hora;
        this.tipo = tipo;
        this.almacenId = almacenId;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public Venta(Long id, LocalDate fecha, Cliente cliente, Comprobante comprobante, Double subtotalInafecto, Double subtotalAfecto, Double igv, Integer estado, Integer pagado, LocalTime hora, Integer tipo, Long almacenId, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.comprobante = comprobante;
        this.subtotalInafecto = subtotalInafecto;
        this.subtotalAfecto = subtotalAfecto;
        this.igv = igv;
        this.estado = estado;
        this.pagado = pagado;
        this.hora = hora;
        this.tipo = tipo;
        this.almacenId = almacenId;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public Double getSubtotalInafecto() {
        return subtotalInafecto;
    }

    public void setSubtotalInafecto(Double subtotalInafecto) {
        this.subtotalInafecto = subtotalInafecto;
    }

    public Double getSubtotalAfecto() {
        return subtotalAfecto;
    }

    public void setSubtotalAfecto(Double subtotalAfecto) {
        this.subtotalAfecto = subtotalAfecto;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getPagado() {
        return pagado;
    }

    public void setPagado(Integer pagado) {
        this.pagado = pagado;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
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

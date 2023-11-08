package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Cobro de Ventas Model")
@Entity
@Table(name = "cobro_ventas")
public class CobroVenta implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Venta")
    @OneToOne
    @JoinColumn(name = "venta_id", nullable = true, foreignKey = @ForeignKey(name = "FK_cobro_venta_venta"))
    private Venta venta;

    @Schema(description = "Fecha de registro de la Venta")
    //@NotNull( message = "{producto.fecha.notnull}")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    @Schema(description = "Metodo de Pago")
    @OneToOne
    @JoinColumn(name = "metodos_pago_id", nullable = true, foreignKey = @ForeignKey(name = "FK_cobro_venta_metodos_pagos"))
    private MetodoPago metodoPago;

    @Schema(description = "Importe del Pago")
    @Column(name="importe", nullable = true)
    private BigDecimal importe;

    @Schema(description = "Tipo de Tarjeta")
    @Column(name="tipo_tarjeta", nullable = true, length= 100)
    private String tipoTarjeta;

    @Schema(description = "Sigla de Tipo de Tarjeta")
    @Column(name="sigla_tarjeta", nullable = true, length= 10)
    private String siglaTarjeta;

    @Schema(description = "Numero de Tarjeta")
    @Column(name="numero_tarjeta", nullable = true, length= 500)
    private String numeroTarjeta;

    @Schema(description = "Banco")
    @Column(name="banco", nullable = true, length= 250)
    private String banco;

    @Schema(description = "Numero de Cuenta")
    @Column(name="numero_cuenta", nullable = true, length= 200)
    private String numeroCuenta;

    @Schema(description = "Numero de Celular")
    @Column(name="numero_celular", nullable = true, length= 20)
    private String numeroCelular;

    @Schema(description = "Numero de Cheque")
    @Column(name="numero_cheque", nullable = true, length= 500)
    private String numeroCheque;

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

    public CobroVenta() {
    }

    public CobroVenta(Long id, Venta venta, LocalDate fecha, MetodoPago metodoPago, BigDecimal importe, String tipoTarjeta, String siglaTarjeta, String numeroTarjeta, String banco, String numeroCuenta, String numeroCelular, String numeroCheque, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.venta = venta;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.importe = importe;
        this.tipoTarjeta = tipoTarjeta;
        this.siglaTarjeta = siglaTarjeta;
        this.numeroTarjeta = numeroTarjeta;
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.numeroCelular = numeroCelular;
        this.numeroCheque = numeroCheque;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public CobroVenta(Long id, Venta venta, LocalDate fecha, MetodoPago metodoPago, BigDecimal importe, String tipoTarjeta, String siglaTarjeta, String numeroTarjeta, String banco, String numeroCuenta, String numeroCelular, String numeroCheque, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.venta = venta;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.importe = importe;
        this.tipoTarjeta = tipoTarjeta;
        this.siglaTarjeta = siglaTarjeta;
        this.numeroTarjeta = numeroTarjeta;
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.numeroCelular = numeroCelular;
        this.numeroCheque = numeroCheque;
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

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getSiglaTarjeta() {
        return siglaTarjeta;
    }

    public void setSiglaTarjeta(String siglaTarjeta) {
        this.siglaTarjeta = siglaTarjeta;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
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

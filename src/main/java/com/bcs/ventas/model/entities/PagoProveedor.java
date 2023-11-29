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

@Schema(description = "Pago a Proveedores Model")
@Entity
@Table(name = "pago_proveedors")
public class PagoProveedor implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Compra")
    @OneToOne
    @JoinColumn(name = "entrada_stock_id", nullable = true, foreignKey = @ForeignKey(name = "FK_pago_proveedors_entrada_stock"))
    private EntradaStock entradaStock;

    @Schema(description = "Fecha de registro de la Venta")
    //@NotNull( message = "{pago_proveedors.fecha.notnull}")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    @Schema(description = "Metodo de Pago")
    @OneToOne
    @JoinColumn(name = "metodos_pago_id", nullable = true, foreignKey = @ForeignKey(name = "FK_cobro_venta_metodos_pagos"))
    private MetodoPago metodoPago;

    @Schema(description = "Importe del Pago")
    @Column(name="monto_pago", nullable = true)
    private BigDecimal montoPago;

    @Schema(description = "Importe del Pago Real")
    @Column(name="monto_real", nullable = true)
    private BigDecimal montoReal;

    @Schema(description = "Tipo de Pago")
    @Column(name="tipo_pago", nullable = true)
    private Integer tipoPago;

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

    @Schema(description = "Codigo de Operacion")
    @Column(name="codigo_operacion", nullable = true, length= 200)
    private String codigoOperacion;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{pago_proveedors.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{pago_proveedors.empresa_id.notnull}")
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

    @Schema(description = "TipoComprobanteId")
    @Transient
    private Long tipoComprobanteId;

    public PagoProveedor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntradaStock getEntradaStock() {
        return entradaStock;
    }

    public void setEntradaStock(EntradaStock entradaStock) {
        this.entradaStock = entradaStock;
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

    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(BigDecimal montoPago) {
        this.montoPago = montoPago;
    }

    public BigDecimal getMontoReal() {
        return montoReal;
    }

    public void setMontoReal(BigDecimal montoReal) {
        this.montoReal = montoReal;
    }

    public Integer getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Integer tipoPago) {
        this.tipoPago = tipoPago;
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

    public String getCodigoOperacion() {
        return codigoOperacion;
    }

    public void setCodigoOperacion(String codigoOperacion) {
        this.codigoOperacion = codigoOperacion;
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

    public Long getTipoComprobanteId() {
        return tipoComprobanteId;
    }

    public void setTipoComprobanteId(Long tipoComprobanteId) {
        this.tipoComprobanteId = tipoComprobanteId;
    }
}

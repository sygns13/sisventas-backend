package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Column(name="venta_id", nullable = true)
    private Long ventaId;

    @Column(name="fecha", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fecha;

    @Column(name="tipo_pago", nullable = true)
    private Integer tipoPago;

    @Column(name="importe", nullable = true)
    private Double importe;

    @Column(name="tipo_tarjeta_id", nullable = true)
    private Long tipoTarjetaId;

    @Column(name="banco_id", nullable = true)
    private Long bancoId;

    @Column(name="cuenta_id", nullable = true)
    private Long cuentaId;

    @Column(name="numero_tarjeta", nullable = true, length= 500)
    private String numeroTarjeta;

    @Column(name="numero_cheque", nullable = true, length= 500)
    private String numeroCheque;

    @Column(name="user_id", nullable = true)
    private Long userId;

    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Column(name="activo", nullable = true)
    private Integer activo;

    @Column(name="borrado", nullable = true)
    private Integer borrado;

    @Column(name="created_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name="updated_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAd;

    public CobroVenta() {
    }

    public CobroVenta(Long id, Long ventaId, Date fecha, Integer tipoPago, Double importe, Long tipoTarjetaId, Long bancoId, Long cuentaId, String numeroTarjeta, String numeroCheque, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.ventaId = ventaId;
        this.fecha = fecha;
        this.tipoPago = tipoPago;
        this.importe = importe;
        this.tipoTarjetaId = tipoTarjetaId;
        this.bancoId = bancoId;
        this.cuentaId = cuentaId;
        this.numeroTarjeta = numeroTarjeta;
        this.numeroCheque = numeroCheque;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public CobroVenta(Long id, Long ventaId, Date fecha, Integer tipoPago, Double importe, Long tipoTarjetaId, Long bancoId, Long cuentaId, String numeroTarjeta, String numeroCheque, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.ventaId = ventaId;
        this.fecha = fecha;
        this.tipoPago = tipoPago;
        this.importe = importe;
        this.tipoTarjetaId = tipoTarjetaId;
        this.bancoId = bancoId;
        this.cuentaId = cuentaId;
        this.numeroTarjeta = numeroTarjeta;
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

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Integer tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Long getTipoTarjetaId() {
        return tipoTarjetaId;
    }

    public void setTipoTarjetaId(Long tipoTarjetaId) {
        this.tipoTarjetaId = tipoTarjetaId;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAd() {
        return updatedAd;
    }

    public void setUpdatedAd(Date updatedAd) {
        this.updatedAd = updatedAd;
    }
}

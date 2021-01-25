package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Column(name="fecha_pago", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fechaPago;

    @Column(name="monto_pago", nullable = true)
    private Double montoPago;

    @Column(name="monto_real", nullable = true)
    private Double montoReal;

    @Column(name="tipo_pago", nullable = true)
    private Integer tipoPago;

    @Column(name="banco_id_emisor", nullable = true)
    private Long bancoIdEmisor;

    @Column(name="banco_id_proveedor", nullable = true)
    private Long bancoIdProveedor;

    @Column(name="cuenta_proveedor_id", nullable = true)
    private Long cuentaProveedorId;

    @Column(name="numero_cheque", nullable = true, length= 45)
    private String numeroCheque;

    @Column(name="factura_proveedor_id", nullable = true)
    private Long facturaProveedorId;

    @Column(name="es_soles", nullable = true)
    private Integer esSoles;

    @Column(name="codigo_operacion", nullable = true, length= 45)
    private String codigoOperacion;

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

    public PagoProveedor() {
    }

    public PagoProveedor(Date fechaPago, Double montoPago, Double montoReal, Integer tipoPago, Long bancoIdEmisor, Long bancoIdProveedor, Long cuentaProveedorId, String numeroCheque, Long facturaProveedorId, Integer esSoles, String codigoOperacion, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.montoReal = montoReal;
        this.tipoPago = tipoPago;
        this.bancoIdEmisor = bancoIdEmisor;
        this.bancoIdProveedor = bancoIdProveedor;
        this.cuentaProveedorId = cuentaProveedorId;
        this.numeroCheque = numeroCheque;
        this.facturaProveedorId = facturaProveedorId;
        this.esSoles = esSoles;
        this.codigoOperacion = codigoOperacion;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public PagoProveedor(Date fechaPago, Double montoPago, Double montoReal, Integer tipoPago, Long bancoIdEmisor, Long bancoIdProveedor, Long cuentaProveedorId, String numeroCheque, Long facturaProveedorId, Integer esSoles, String codigoOperacion, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.montoReal = montoReal;
        this.tipoPago = tipoPago;
        this.bancoIdEmisor = bancoIdEmisor;
        this.bancoIdProveedor = bancoIdProveedor;
        this.cuentaProveedorId = cuentaProveedorId;
        this.numeroCheque = numeroCheque;
        this.facturaProveedorId = facturaProveedorId;
        this.esSoles = esSoles;
        this.codigoOperacion = codigoOperacion;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Double getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(Double montoPago) {
        this.montoPago = montoPago;
    }

    public Double getMontoReal() {
        return montoReal;
    }

    public void setMontoReal(Double montoReal) {
        this.montoReal = montoReal;
    }

    public Integer getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Integer tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Long getBancoIdEmisor() {
        return bancoIdEmisor;
    }

    public void setBancoIdEmisor(Long bancoIdEmisor) {
        this.bancoIdEmisor = bancoIdEmisor;
    }

    public Long getBancoIdProveedor() {
        return bancoIdProveedor;
    }

    public void setBancoIdProveedor(Long bancoIdProveedor) {
        this.bancoIdProveedor = bancoIdProveedor;
    }

    public Long getCuentaProveedorId() {
        return cuentaProveedorId;
    }

    public void setCuentaProveedorId(Long cuentaProveedorId) {
        this.cuentaProveedorId = cuentaProveedorId;
    }

    public String getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    public Long getFacturaProveedorId() {
        return facturaProveedorId;
    }

    public void setFacturaProveedorId(Long facturaProveedorId) {
        this.facturaProveedorId = facturaProveedorId;
    }

    public Integer getEsSoles() {
        return esSoles;
    }

    public void setEsSoles(Integer esSoles) {
        this.esSoles = esSoles;
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

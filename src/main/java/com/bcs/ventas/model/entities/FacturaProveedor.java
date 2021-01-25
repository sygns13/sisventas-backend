package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "factura_proveedors")
public class FacturaProveedor implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="proveedor_id", nullable = true)
    private Long proveedorId;

    @Column(name="serie", nullable = true, length= 45)
    private String serie;

    @Column(name="numero", nullable = true, length= 45)
    private String numero;

    @Column(name="fecha", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fecha;

    @Column(name="pagado", nullable = true)
    private Integer pagado;

    @Column(name="total", nullable = true)
    private Double total;

    @Column(name="observaciones", nullable = true, length= 1024)
    private String observaciones;

    @Column(name="tipo_comprobante_id", nullable = true)
    private Long tipoComprobanteId;

    @Column(name="total_soles", nullable = true)
    private Double totalSoles;

    @Column(name="en_soles", nullable = true)
    private Integer enSoles;

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

    public FacturaProveedor() {
    }

    public FacturaProveedor(Long id, Long proveedorId, String serie, String numero, Date fecha, Integer pagado, Double total, String observaciones, Long tipoComprobanteId, Double totalSoles, Integer enSoles, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.proveedorId = proveedorId;
        this.serie = serie;
        this.numero = numero;
        this.fecha = fecha;
        this.pagado = pagado;
        this.total = total;
        this.observaciones = observaciones;
        this.tipoComprobanteId = tipoComprobanteId;
        this.totalSoles = totalSoles;
        this.enSoles = enSoles;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public FacturaProveedor(Long id, Long proveedorId, String serie, String numero, Date fecha, Integer pagado, Double total, String observaciones, Long tipoComprobanteId, Double totalSoles, Integer enSoles, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.proveedorId = proveedorId;
        this.serie = serie;
        this.numero = numero;
        this.fecha = fecha;
        this.pagado = pagado;
        this.total = total;
        this.observaciones = observaciones;
        this.tipoComprobanteId = tipoComprobanteId;
        this.totalSoles = totalSoles;
        this.enSoles = enSoles;
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

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getPagado() {
        return pagado;
    }

    public void setPagado(Integer pagado) {
        this.pagado = pagado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getTipoComprobanteId() {
        return tipoComprobanteId;
    }

    public void setTipoComprobanteId(Long tipoComprobanteId) {
        this.tipoComprobanteId = tipoComprobanteId;
    }

    public Double getTotalSoles() {
        return totalSoles;
    }

    public void setTotalSoles(Double totalSoles) {
        this.totalSoles = totalSoles;
    }

    public Integer getEnSoles() {
        return enSoles;
    }

    public void setEnSoles(Integer enSoles) {
        this.enSoles = enSoles;
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

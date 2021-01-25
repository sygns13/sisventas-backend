package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "entrada_stocks")
public class EntradaStock implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="numero", nullable = true, length= 45)
    private String numero;

    @Column(name="fecha", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fecha;

    @Column(name="proveedor_id", nullable = true)
    private Long proveedorId;

    @Column(name="orden_compra_id", nullable = true)
    private Long ordenCompraId;

    @Column(name="facturado", nullable = true)
    private Integer facturado;

    @Column(name="actualizado", nullable = true)
    private Integer actualizado;

    @Column(name="factura_proveedor_id", nullable = true)
    private Long facturaProveedorId;

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

    public EntradaStock() {
    }

    public EntradaStock(Long id, String numero, Date fecha, Long proveedorId, Long ordenCompraId, Integer facturado, Integer actualizado, Long facturaProveedorId, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.numero = numero;
        this.fecha = fecha;
        this.proveedorId = proveedorId;
        this.ordenCompraId = ordenCompraId;
        this.facturado = facturado;
        this.actualizado = actualizado;
        this.facturaProveedorId = facturaProveedorId;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public EntradaStock(Long id, String numero, Date fecha, Long proveedorId, Long ordenCompraId, Integer facturado, Integer actualizado, Long facturaProveedorId, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.numero = numero;
        this.fecha = fecha;
        this.proveedorId = proveedorId;
        this.ordenCompraId = ordenCompraId;
        this.facturado = facturado;
        this.actualizado = actualizado;
        this.facturaProveedorId = facturaProveedorId;
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

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Long getOrdenCompraId() {
        return ordenCompraId;
    }

    public void setOrdenCompraId(Long ordenCompraId) {
        this.ordenCompraId = ordenCompraId;
    }

    public Integer getFacturado() {
        return facturado;
    }

    public void setFacturado(Integer facturado) {
        this.facturado = facturado;
    }

    public Integer getActualizado() {
        return actualizado;
    }

    public void setActualizado(Integer actualizado) {
        this.actualizado = actualizado;
    }

    public Long getFacturaProveedorId() {
        return facturaProveedorId;
    }

    public void setFacturaProveedorId(Long facturaProveedorId) {
        this.facturaProveedorId = facturaProveedorId;
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

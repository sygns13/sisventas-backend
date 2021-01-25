package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "guia_remisions")
public class GuiaRemision implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="fecha", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fecha;

    @Column(name="partida", nullable = true, length= 500)
    private String partida;

    @Column(name="llegada", nullable = true, length= 500)
    private String llegada;

    @Column(name="nombre_transportista", nullable = true, length= 500)
    private String nombreTransportista;

    @Column(name="ruc_transportista", nullable = true, length= 500)
    private String rucTransportista;

    @Column(name="domicilio", nullable = true, length= 500)
    private String domicilio;

    @Column(name="placa", nullable = true, length= 100)
    private String placa;

    @Column(name="numero_constancia", nullable = true, length= 100)
    private String numeroConstancia;

    @Column(name="numero_licencia", nullable = true, length= 100)
    private String numeroLicencia;

    @Column(name="venta_id", nullable = true)
    private Long ventaId;

    @Column(name="comprobante_id", nullable = true)
    private Long comprobanteId;

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

    public GuiaRemision() {
    }

    public GuiaRemision(Long id, Date fecha, String partida, String llegada, String nombreTransportista, String rucTransportista, String domicilio, String placa, String numeroConstancia, String numeroLicencia, Long ventaId, Long comprobanteId, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.fecha = fecha;
        this.partida = partida;
        this.llegada = llegada;
        this.nombreTransportista = nombreTransportista;
        this.rucTransportista = rucTransportista;
        this.domicilio = domicilio;
        this.placa = placa;
        this.numeroConstancia = numeroConstancia;
        this.numeroLicencia = numeroLicencia;
        this.ventaId = ventaId;
        this.comprobanteId = comprobanteId;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public GuiaRemision(Long id, Date fecha, String partida, String llegada, String nombreTransportista, String rucTransportista, String domicilio, String placa, String numeroConstancia, String numeroLicencia, Long ventaId, Long comprobanteId, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.fecha = fecha;
        this.partida = partida;
        this.llegada = llegada;
        this.nombreTransportista = nombreTransportista;
        this.rucTransportista = rucTransportista;
        this.domicilio = domicilio;
        this.placa = placa;
        this.numeroConstancia = numeroConstancia;
        this.numeroLicencia = numeroLicencia;
        this.ventaId = ventaId;
        this.comprobanteId = comprobanteId;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public String getLlegada() {
        return llegada;
    }

    public void setLlegada(String llegada) {
        this.llegada = llegada;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    public String getRucTransportista() {
        return rucTransportista;
    }

    public void setRucTransportista(String rucTransportista) {
        this.rucTransportista = rucTransportista;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getNumeroConstancia() {
        return numeroConstancia;
    }

    public void setNumeroConstancia(String numeroConstancia) {
        this.numeroConstancia = numeroConstancia;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Long getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(Long comprobanteId) {
        this.comprobanteId = comprobanteId;
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

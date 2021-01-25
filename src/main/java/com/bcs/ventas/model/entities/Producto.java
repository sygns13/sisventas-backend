package com.bcs.ventas.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombre", nullable = true, length= 150)
    private String nombre;

    @Column(name="tipo_producto_id", nullable = true)
    private Long tipoProductoId;

    @Column(name="marca_id", nullable = true)
    private Long marcaId;

    @Column(name="stock_minimo", nullable = true)
    private Double stockMinimo;

    @Column(name="precio_unidad", nullable = true)
    private Double precioUnidad;

    @Column(name="precio_compra", nullable = true)
    private Double precioCompra;

    @Column(name="fecha", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fecha;

    @Column(name="codigo_unidad", nullable = true, length= 200)
    private String codigoUnidad;

    @Column(name="codigo_producto", nullable = true, length= 200)
    private String codigoProducto;

    @Column(name="presentacion_id", nullable = true)
    private Long presentacionId;

    @Column(name="composicion", nullable = true, length= 500)
    private String composicion;

    @Column(name="prioridad", nullable = true, length= 100)
    private String prioridad;

    @Column(name="ubicacion", nullable = true, length= 100)
    private String ubicacion;

    @Column(name="activo_lotes", nullable = true)
    private Integer activoLotes;

    @Column(name="afecto_isc", nullable = true)
    private Integer afectoIsc;

    @Column(name="tipo_tasa_isc", nullable = true)
    private Integer tipoTasaIsc;

    @Column(name="tasa_isc", nullable = true)
    private Integer tasaIsc;

    @Column(name="afecto_igv", nullable = true)
    private Integer afectoIgv;

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

    public Producto() {
    }

    public Producto(Long id, String nombre, Long tipoProductoId, Long marcaId, Double stockMinimo, Double precioUnidad, Double precioCompra, Date fecha, String codigoUnidad, String codigoProducto, Long presentacionId, String composicion, String prioridad, String ubicacion, Integer activoLotes, Integer afectoIsc, Integer tipoTasaIsc, Integer tasaIsc, Integer afectoIgv, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.nombre = nombre;
        this.tipoProductoId = tipoProductoId;
        this.marcaId = marcaId;
        this.stockMinimo = stockMinimo;
        this.precioUnidad = precioUnidad;
        this.precioCompra = precioCompra;
        this.fecha = fecha;
        this.codigoUnidad = codigoUnidad;
        this.codigoProducto = codigoProducto;
        this.presentacionId = presentacionId;
        this.composicion = composicion;
        this.prioridad = prioridad;
        this.ubicacion = ubicacion;
        this.activoLotes = activoLotes;
        this.afectoIsc = afectoIsc;
        this.tipoTasaIsc = tipoTasaIsc;
        this.tasaIsc = tasaIsc;
        this.afectoIgv = afectoIgv;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public Producto(Long id, String nombre, Long tipoProductoId, Long marcaId, Double stockMinimo, Double precioUnidad, Double precioCompra, Date fecha, String codigoUnidad, String codigoProducto, Long presentacionId, String composicion, String prioridad, String ubicacion, Integer activoLotes, Integer afectoIsc, Integer tipoTasaIsc, Integer tasaIsc, Integer afectoIgv, Long userId, Long empresaId, Integer activo, Integer borrado, Date createdAt, Date updatedAd) {
        this.id = id;
        this.nombre = nombre;
        this.tipoProductoId = tipoProductoId;
        this.marcaId = marcaId;
        this.stockMinimo = stockMinimo;
        this.precioUnidad = precioUnidad;
        this.precioCompra = precioCompra;
        this.fecha = fecha;
        this.codigoUnidad = codigoUnidad;
        this.codigoProducto = codigoProducto;
        this.presentacionId = presentacionId;
        this.composicion = composicion;
        this.prioridad = prioridad;
        this.ubicacion = ubicacion;
        this.activoLotes = activoLotes;
        this.afectoIsc = afectoIsc;
        this.tipoTasaIsc = tipoTasaIsc;
        this.tasaIsc = tasaIsc;
        this.afectoIgv = afectoIgv;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getTipoProductoId() {
        return tipoProductoId;
    }

    public void setTipoProductoId(Long tipoProductoId) {
        this.tipoProductoId = tipoProductoId;
    }

    public Long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Long marcaId) {
        this.marcaId = marcaId;
    }

    public Double getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Double getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(Double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public Long getPresentacionId() {
        return presentacionId;
    }

    public void setPresentacionId(Long presentacionId) {
        this.presentacionId = presentacionId;
    }

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getActivoLotes() {
        return activoLotes;
    }

    public void setActivoLotes(Integer activoLotes) {
        this.activoLotes = activoLotes;
    }

    public Integer getAfectoIsc() {
        return afectoIsc;
    }

    public void setAfectoIsc(Integer afectoIsc) {
        this.afectoIsc = afectoIsc;
    }

    public Integer getTipoTasaIsc() {
        return tipoTasaIsc;
    }

    public void setTipoTasaIsc(Integer tipoTasaIsc) {
        this.tipoTasaIsc = tipoTasaIsc;
    }

    public Integer getTasaIsc() {
        return tasaIsc;
    }

    public void setTasaIsc(Integer tasaIsc) {
        this.tasaIsc = tasaIsc;
    }

    public Integer getAfectoIgv() {
        return afectoIgv;
    }

    public void setAfectoIgv(Integer afectoIgv) {
        this.afectoIgv = afectoIgv;
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

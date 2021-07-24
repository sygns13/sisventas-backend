package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "Servicio Model")
@Entity
@Table(name = "servicios")
public class Servicio {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del Servicio")
    @NotNull( message = "{servicio.nombre.notnull}")
    @Size(min = 1, max = 2000, message = "{servicio.nombre.size}")
    @Column(name="nombre", nullable = true, length= 2000)
    private String nombre;

    @Schema(description = "Descripción del Servicio")
    @Column(name="descripcion", nullable = true)
    private String descripcion;

    @Schema(description = "Precio de Venta de Servicio")
    @NotNull( message = "{servicio.precio_unidad.notnull}")
    @DecimalMin(value = "0.01", message = "{servicio.precio_unidad.min}")
    @DecimalMax(value = "999999999", message = "{servicio.precio_unidad.max}")
    @Column(name="precio_unidad", nullable = true)
    private Double precioUnidad;

    @Schema(description = "Código del Servicio")
    @NotNull( message = "{servicio.codigo.notnull}")
    @Size(min = 1, max = 200, message = "{servicio.codigo.size}")
    @Column(name="codigo", nullable = true, length= 200)
    private String codigo;

    @Schema(description = "Indicador de ISC del Servicio")
    @NotNull( message = "{servicio.afecto_isc.notnull}")
    @Column(name="afecto_isc", nullable = true)
    private Integer afectoIsc;

    @Schema(description = "Tipo de Tasa de ISC del Servicio")
    //@NotNull( message = "{servicio.tipo_tasa_isc.notnull}")
    @Column(name="tipo_tasa_isc", nullable = true)
    private Integer tipoTasaIsc;

    @Schema(description = "Tasa de ISC del Servicio")
    //@NotNull( message = "{servicio.tasa_isc.notnull}")
    @Column(name="tasa_isc", nullable = true)
    private Double tasaIsc;

    @Schema(description = "Indicador de IGV del Servicio")
    @NotNull( message = "{servicio.afecto_igv.notnull}")
    @Column(name="afecto_igv", nullable = true)
    private Integer afectoIgv;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{servicio.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{servicio.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado de Servicio")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de Servicio")
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

    public Servicio() {
    }

    public Servicio(Long id, String nombre, String descripcion, Double precioUnidad, String codigo, Integer afectoIsc, Integer tipoTasaIsc, Double tasaIsc, Integer afectoIgv, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnidad = precioUnidad;
        this.codigo = codigo;
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

    public Servicio(Long id, String nombre, String descripcion, Double precioUnidad, String codigo, Integer afectoIsc, Integer tipoTasaIsc, Double tasaIsc, Integer afectoIgv, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnidad = precioUnidad;
        this.codigo = codigo;
        this.afectoIsc = afectoIsc;
        this.tipoTasaIsc = tipoTasaIsc;
        this.tasaIsc = tasaIsc;
        this.afectoIgv = afectoIgv;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(Double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public Double getTasaIsc() {
        return tasaIsc;
    }

    public void setTasaIsc(Double tasaIsc) {
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

package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Schema(description = "Almacen Model")
@Entity
@Table(name = "almacens")
public class Almacen implements Serializable {

    private static final long serialVersionUID = 1L;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombres del Almacén")
    @NotNull( message = "{almacen.nombre.notnull}")
    @Size(min = 1, max = 500, message = "{almacen.nombre.size}")
    @Column(name="nombre", nullable = true, length= 500)
    private String nombre;

    @Schema(description = "Dirección del Almacén")
    @Size(max = 500, message = "{almacen.direccion.size}")
    @Column(name="direccion", nullable = true, length= 500)
    private String direccion;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{almacen.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{almacen.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "Estado de Almacen")
    //@NotNull( message = "{almacen.activo.notnull}")
    //@Min(value = 0, message = "{almacen.activo.min}")
    //@Max(value = 2, message = "{almacen.activo.max}")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de Almacen")
    //@NotNull( message = "{almacen.borrado.notnull}")
    //@Min(value = 0, message = "{almacen.borrado.min}")
    //@Max(value = 2, message = "{almacen.borrado.max}")
    @Column(name="borrado", nullable = true)
    private Integer borrado;

    @Schema(description = "Distrito de Ubicación del Almacen")
    @NotNull( message = "{almacen.distrito_id.notnull}")
    @Column(name="distrito_id", nullable = true)
    private Long distritoId;

    @Schema(description = "Código del Almacen")
    @NotNull( message = "{almacen.codigo.notnull}")
    @Size(min = 1, max = 200, message = "{almacen.codigo.size}")
    @Column(name="codigo", nullable = true, length= 200)
    private String codigo;

   /* @Column(name="created_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;*/
    @Schema(description = "Fecha de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="created_at", nullable = true)
    private LocalDateTime createdAt;


    /*@Column(name="updated_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAd;*/
    @Schema(description = "Fecha de Update del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="updated_at", nullable = true)
    private LocalDateTime updatedAd;

    @Schema(description = "País del Almacén")
    @Transient
    private Pais pais;

    @Schema(description = "Departamento del Almacén")
    @Transient
    private Departamento departamento;

    @Schema(description = "Provincia del Almacén")
    @Transient
    private  Provincia provincia;

    @Schema(description = "Distrito del Almacén")
    @Transient
    private Distrito distrito;

    @Schema(description = "Productos en Stock en Almacen")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private Map<String, Object> productosStocks;

    public Almacen() {
    }

    public Almacen(Long id, String nombre, String direccion, Long empresaId, Long userId, Integer activo, Integer borrado, Long distritoId, String codigo, Pais pais, Departamento departamento, Provincia provincia, Distrito distrito) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.empresaId = empresaId;
        this.userId = userId;
        this.activo = activo;
        this.borrado = borrado;
        this.distritoId = distritoId;
        this.codigo = codigo;
        this.pais = pais;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
    }

    public Almacen(Long id, String nombre, String direccion, Long empresaId, Long userId, Integer activo, Integer borrado, Long distritoId, String codigo, LocalDateTime createdAt, LocalDateTime updatedAd , Pais pais, Departamento departamento, Provincia provincia, Distrito distrito) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.empresaId = empresaId;
        this.userId = userId;
        this.activo = activo;
        this.borrado = borrado;
        this.distritoId = distritoId;
        this.codigo = codigo;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
        this.pais = pais;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(Long distritoId) {
        this.distritoId = distritoId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public Map<String, Object> getProductosStocks() {
        return productosStocks;
    }

    public void setProductosStocks(Map<String, Object> productosStocks) {
        this.productosStocks = productosStocks;
    }
}

package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "Tipo Comprobante Model")
@Entity
@Table(name = "init_comprobantes")
public class InitComprobante implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Numero Serie del Comprobante")
    @NotNull( message = "{init_comprobante.num_serie.notnull}")
    @Min(value = 1, message = "{init_comprobante.num_serie.size}")
    @Max(value = 99, message = "{init_comprobante.num_serie.size}")
    @Column(name="num_serie", nullable = true)
    private Integer numSerie;

    @Schema(description = "Letra de Serie del Comprobante")
    @NotNull( message = "{init_comprobante.letra_serie.notnull}")
    @Size(min = 1, max = 1, message = "{init_comprobante.letra_serie.size}")
    @Column(name="letra_serie", nullable = true, length= 1)
    private String letraSerie;

    @Schema(description = "Numero inicial del Comprobante")
    @NotNull( message = "{init_comprobante.numero.notnull}")
    @Min(value = 1, message = "{init_comprobante.numero.size}")
    @Max(value = 99999999, message = "{init_comprobante.numero.size}")
    @Column(name="numero", nullable = true)
    private Integer numero;

    @Schema(description = "Numero actual del Comprobante")
    //@NotNull( message = "{init_comprobante.numero_actual.notnull}")
    //@Size(min = 1, max = 99999999, message = "{init_comprobante.numero_actual.size}")
    @Column(name="numero_actual", nullable = true)
    private Integer numeroActual;

    @Schema(description = "cantidad de caracteres del Numero del Comprobante")
    //@NotNull( message = "{init_comprobante.cantidad_digitos.notnull}")
    @Column(name="cantidad_digitos", nullable = true)
    private Integer cantidadDigitos;

    @Schema(description = "Tipo de Comprobante")
    @ManyToOne
    @JoinColumn(name = "tipo_comprobante_id", nullable = false, foreignKey = @ForeignKey(name = "FK_init_comprobante_comprobante"))
    private TipoComprobante tipoComprobante;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{producto.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{producto.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "Fecha de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="created_at", nullable = true)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de Update del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="updated_at", nullable = true)
    private LocalDateTime updatedAd;

    @Schema(description = "Estado de Producto")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de Producto")
    @Column(name="borrado", nullable = true)
    private Integer borrado;


    @Schema(description = "ID del Local")
    @NotNull( message = "{init_comprobante.almacen_id.notnull}")
    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    @Schema(description = "Almacén")
    @Transient
    private Almacen almacen;

    @Transient
    private String letraSerieStr;

    @Transient
    private String numSerieStr;

    @Transient
    private String numeroStr;

    @Transient
    private String numeroActualStr;

    public InitComprobante() {
    }

    public InitComprobante(Long id, Integer numSerie, String letraSerie, Integer numero, Integer numeroActual, Integer cantidadDigitos, TipoComprobante tipoComprobante, Long empresaId, Long userId, Integer activo, Integer borrado, Long almacenId, Almacen almacen, String letraSerieStr, String numSerieStr, String numeroStr, String numeroActualStr) {
        this.id = id;
        this.numSerie = numSerie;
        this.letraSerie = letraSerie;
        this.numero = numero;
        this.numeroActual = numeroActual;
        this.cantidadDigitos = cantidadDigitos;
        this.tipoComprobante = tipoComprobante;
        this.empresaId = empresaId;
        this.userId = userId;
        this.activo = activo;
        this.borrado = borrado;
        this.almacenId = almacenId;
        this.almacen = almacen;
        this.letraSerieStr = letraSerieStr;
        this.numSerieStr = numSerieStr;
        this.numeroStr = numeroStr;
        this.numeroActualStr = numeroActualStr;
    }

    public InitComprobante(Long id, Integer numSerie, String letraSerie, Integer numero, Integer numeroActual, Integer cantidadDigitos, TipoComprobante tipoComprobante, Long empresaId, Long userId, LocalDateTime createdAt, LocalDateTime updatedAd, Integer activo, Integer borrado, Long almacenId, Almacen almacen, String letraSerieStr, String numSerieStr, String numeroStr, String numeroActualStr) {
        this.id = id;
        this.numSerie = numSerie;
        this.letraSerie = letraSerie;
        this.numero = numero;
        this.numeroActual = numeroActual;
        this.cantidadDigitos = cantidadDigitos;
        this.tipoComprobante = tipoComprobante;
        this.empresaId = empresaId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
        this.activo = activo;
        this.borrado = borrado;
        this.almacenId = almacenId;
        this.almacen = almacen;
        this.letraSerieStr = letraSerieStr;
        this.numSerieStr = numSerieStr;
        this.numeroStr = numeroStr;
        this.numeroActualStr = numeroActualStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(Integer numSerie) {
        this.numSerie = numSerie;
    }

    public String getLetraSerie() {
        return letraSerie;
    }

    public void setLetraSerie(String letraSerie) {
        this.letraSerie = letraSerie;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCantidadDigitos() {
        return cantidadDigitos;
    }

    public void setCantidadDigitos(Integer cantidadDigitos) {
        this.cantidadDigitos = cantidadDigitos;
    }

    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
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

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Integer getNumeroActual() {
        return numeroActual;
    }

    public void setNumeroActual(Integer numeroActual) {
        this.numeroActual = numeroActual;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public String getLetraSerieStr() {
        return letraSerieStr;
    }

    public void setLetraSerieStr(String letraSerieStr) {
        this.letraSerieStr = letraSerieStr;
    }

    public String getNumSerieStr() {
        return numSerieStr;
    }

    public void setNumSerieStr(String numSerieStr) {
        this.numSerieStr = numSerieStr;
    }

    public String getNumeroStr() {
        return numeroStr;
    }

    public void setNumeroStr(String numeroStr) {
        this.numeroStr = numeroStr;
    }

    public String getNumeroActualStr() {
        return numeroActualStr;
    }

    public void setNumeroActualStr(String numeroActualStr) {
        this.numeroActualStr = numeroActualStr;
    }
}

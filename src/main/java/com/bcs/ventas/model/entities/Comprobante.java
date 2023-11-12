package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Comprobante Model")
@Entity
@Table(name = "comprobantes")
public class Comprobante implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Serie del Comprobante")
    @NotNull( message = "{comprobante.serie.notnull}")
    //@Size(min = 4, max = 4, message = "{comprobante.serie.size}")
    @Column(name="serie", nullable = true, length= 46)
    private String serie;

    @Schema(description = "Numero del Comprobante")
    @NotNull( message = "{comprobante.numero.notnull}")
    @Size(min = 1, max = 8, message = "{comprobante.numero.size}")
    @Column(name="numero", nullable = true, length= 45)
    private String numero;

    @Schema(description = "cantidad de caracteres del Numero del Comprobante")
    @NotNull( message = "{comprobante.cantidad_digitos.notnull}")
    @Column(name="cantidad_digitos", nullable = true)
    private Integer cantidadDigitos;

    //@Column(name="tipo_comprobante_id", nullable = true)
    //private Long tipoComprobanteId;
    @Schema(description = "ID Inicio de Comprobante")
    @ManyToOne
    @JoinColumn(name = "init_comprobante_id", nullable = false, foreignKey = @ForeignKey(name = "FK_init_comprobante_comprobante"))
    private InitComprobante initComprobante;

    @Schema(description = "Estado del Comprobante")
    @Column(name="estado", nullable = true, length= 45)
    private String estado;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{producto.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID del Local")
    @NotNull( message = "{comprobante.almacen_id.notnull}")
    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{producto.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado de Producto")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de Producto")
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

    public Comprobante() {
    }

    public Comprobante(Long id, String serie, String numero, Integer cantidadDigitos, InitComprobante initComprobante, String estado, Long userId, Long almacenId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.serie = serie;
        this.numero = numero;
        this.cantidadDigitos = cantidadDigitos;
        this.initComprobante = initComprobante;
        this.estado = estado;
        this.userId = userId;
        this.almacenId = almacenId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public Comprobante(Long id, String serie, String numero, Integer cantidadDigitos, InitComprobante initComprobante, String estado, Long userId, Long almacenId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.serie = serie;
        this.numero = numero;
        this.cantidadDigitos = cantidadDigitos;
        this.initComprobante = initComprobante;
        this.estado = estado;
        this.userId = userId;
        this.almacenId = almacenId;
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

    public Integer getCantidadDigitos() {
        return cantidadDigitos;
    }

    public void setCantidadDigitos(Integer cantidadDigitos) {
        this.cantidadDigitos = cantidadDigitos;
    }

    public InitComprobante getInitComprobante() {
        return initComprobante;
    }

    public void setInitComprobante(InitComprobante initComprobante) {
        this.initComprobante = initComprobante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
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

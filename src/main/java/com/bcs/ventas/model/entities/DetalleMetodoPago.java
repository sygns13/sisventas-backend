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

@Schema(description = "Detalle de Metodo de Pago Model")
@Entity
@Table(name = "detalle_metodo_pagos")
public class DetalleMetodoPago implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID del Tipo de Tarjeta")
    //@NotNull( message = "{detalle_metodo_pagos.tipo_tarjeta_id.notnull}")
    @Column(name="tipo_tarjeta_id", nullable = true)
    private Long tipoTarjetaId;

    @Schema(description = "ID del Banco")
    //@NotNull( message = "{detalle_metodo_pagos.tipo_tarjeta_id.notnull}")
    @Column(name="banco_id", nullable = true)
    private Long bancoId;

    @Schema(description = "Numero de Cuenta")
    //@NotNull( message = "{detalle_metodo_pagos.numero_cuenta.notnull}")
    //@Size(min = 1, max = 200, message = "{metodos_pagos.nombre.size}")
    @Column(name="numero_cuenta", nullable = true, length = 200)
    private String numeroCuenta;

    @Schema(description = "ID de Tipo del Metodo de Pago")
    //@NotNull( message = "{detalle_metodo_pagos.numeroCelular.notnull}")
    //@Size(min = 1, max = 20, message = "{metodos_pagos.numeroCelular.size}")
    @Column(name="numero_celular", nullable = true, length = 20)
    private String numeroCelular;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{detalle_metodo_pagos.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

    @Schema(description = "ID Empresa Padre")
    //@NotNull( message = "{detalle_metodo_pagos.empresa_id.notnull}")
    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Schema(description = "Estado de la Unidad")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico de la Unidad")
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


    @Schema(description = "Banco")
    @Transient
    private Banco banco;

    @Schema(description = "Tipo Tarjeta")
    @Transient
    private TipoTarjeta tipoTarjeta;

    @Schema(description = "Metodo de Pago")
    @ManyToOne
    @JoinColumn(name = "metodos_pago_id", nullable = false, foreignKey = @ForeignKey(name = "FK_metodos_pagos"))
    private MetodoPago metodoPago;

    public DetalleMetodoPago() {
    }

    public DetalleMetodoPago(Long id, Long tipoTarjetaId, Long bancoId, String numeroCuenta, String numeroCelular, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.tipoTarjetaId = tipoTarjetaId;
        this.bancoId = bancoId;
        this.numeroCuenta = numeroCuenta;
        this.numeroCelular = numeroCelular;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public DetalleMetodoPago(Long id, Long tipoTarjetaId, Long bancoId, String numeroCuenta, String numeroCelular, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.tipoTarjetaId = tipoTarjetaId;
        this.bancoId = bancoId;
        this.numeroCuenta = numeroCuenta;
        this.numeroCelular = numeroCelular;
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

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
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

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public TipoTarjeta getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(TipoTarjeta tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
}

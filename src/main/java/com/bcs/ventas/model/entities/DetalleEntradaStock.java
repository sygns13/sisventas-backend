package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Detalle Compras Model")
@Entity
@Table(name = "detalle_entrada_stocks")
public class DetalleEntradaStock implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Compra")
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "entrada_stock_id", nullable = false, foreignKey = @ForeignKey(name = "FK_entrada_stock_id_detalle"))
    private EntradaStock entradaStock;

    @Transient
    private Long entradaStockIdReference;

    //@Column(name="producto_id", nullable = true)
    //private Long productoId;
    @Schema(description = "Producto de la Venta")
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false, foreignKey = @ForeignKey(name = "FK_producto_detalle_entrada_stocks"))
    private Producto producto;

    @Schema(description = "Cantidad de Producto vendido")
    @NotNull( message = "{detalle_entrada_stocks.cantidad.notnull}")
    @DecimalMin(value = "0.00", message = "{detalle_entrada_stocks.cantidad.min}")
    @DecimalMax(value = "999999999", message = "{detalle_entrada_stocks.cantidad.max}")
    @Column(name="cantidad", nullable = true)
    private Double cantidad;

    @Schema(description = "Costo de Venta del Producto")
    @NotNull( message = "{detalle_entrada_stocks.costo.notnull}")
    @DecimalMin(value = "0.01", message = "{detalle_entrada_stocks.costo.min}")
    @DecimalMax(value = "999999999", message = "{detalle_entrada_stocks.costo.max}")
    @Column(name="costo", nullable = true)
    private BigDecimal costo;

    @Schema(description = "ID del Local")
    @NotNull( message = "{detalle_entrada_stocks.almacen_id.notnull}")
    @Column(name="almacen_id", nullable = true)
    private Long almacenId;

    @Schema(description = "Cantidad Real de Unidad de Producto comprado")
    @NotNull( message = "{detalle_entrada_stocks.cantreal.notnull}")
    @DecimalMin(value = "0.00", message = "{detalle_entrada_stocks.cantreal.min}")
    @DecimalMax(value = "999999999", message = "{detalle_entrada_stocks.cantreal.max}")
    @Column(name="cantreal", nullable = true)
    private Double cantreal;

    @Schema(description = "Descripción de Unidad de Producto comprado")
    @Column(name="unidad", nullable = true, length= 500)
    private String unidad;

    @Schema(description = "Precio de Venta del Producto")
    @NotNull( message = "{detalle_entrada_stocks.costo_total.notnull}")
    @DecimalMin(value = "0.01", message = "{costo_total.costo_total.min}")
    @DecimalMax(value = "999999999", message = "{detalle_entrada_stocks.costo_total.max}")
    @Column(name="costo_total", nullable = true)
    private BigDecimal costoTotal;

    @Schema(description = "Lote del Producto de la Venta")
    @ManyToOne
    @JoinColumn(name = "lote_id", nullable = true, foreignKey = @ForeignKey(name = "FK_lote_detalle_entrada_stocks"))
    private Lote lote;

    @Schema(description = "ID User Padre")
    //@NotNull( message = "{producto.user_id.notnull}")
    @Column(name="user_id", nullable = true)
    private Long userId;

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

    public DetalleEntradaStock() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntradaStock getEntradaStock() {
        return entradaStock;
    }

    public void setEntradaStock(EntradaStock entradaStock) {
        this.entradaStock = entradaStock;
    }

    public Long getEntradaStockIdReference() {
        return entradaStockIdReference;
    }

    public void setEntradaStockIdReference(Long entradaStockIdReference) {
        this.entradaStockIdReference = entradaStockIdReference;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Double getCantreal() {
        return cantreal;
    }

    public void setCantreal(Double cantreal) {
        this.cantreal = cantreal;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
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

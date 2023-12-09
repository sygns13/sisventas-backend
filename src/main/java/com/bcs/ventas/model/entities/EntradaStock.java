package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Schema(description = "Compra Model")
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

    @Schema(description = "Numero de Compra por Sucursal")
    @Column(name="numero", nullable = true)
    private String numero;

    @Schema(description = "Fecha de registro de la Compra")
    //@NotNull( message = "{entrada_stocks.fecha.notnull}")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    @Schema(description = "Hora de la Compra")
    @Column(name="hora", nullable = true)
    @JsonFormat(pattern="HH:mm:ss")
    //@Temporal(TemporalType.TIME)
    //@DateTimeFormat(pattern="HH:mm:ss")
    private LocalTime hora;

    //@Column(name="proveedor_id", nullable = true)
    //private Long proveedorId;

    @Schema(description = "Proveedor de la Compra")
    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = true, foreignKey = @ForeignKey(name = "FK_proveedor_entrada_stocks"))
    private Proveedor proveedor;

    @Schema(description = "Orden de Compra ID")
    @Column(name="orden_compra_id", nullable = true)
    private Long ordenCompraId;

    @Schema(description = "Orden de Compra ID")
    @Column(name="facturado", nullable = true)
    private Integer facturado;

    @Schema(description = "Actualizado Stock")
    @Column(name="actualizado", nullable = true)
    private Integer actualizado;

    @Schema(description = "Estado de la Compra")
    @Column(name="estado", nullable = true)
    private Integer estado;

    @Schema(description = "Monto Total de la Compra")
    @Column(name="total_monto", nullable = true)
    private BigDecimal totalMonto;

    //@Column(name="factura_proveedor_id", nullable = true)
    //private Long facturaProveedorId;

    @Schema(description = "Comprobante de la Compra")
    @OneToOne
    @JoinColumn(name = "factura_proveedor_id", nullable = true, foreignKey = @ForeignKey(name = "FK_factura_proveedor_id_entrada_stocks"))
    private FacturaProveedor facturaProveedor;

    @Schema(description = "Almacen donde se realizó la Compra")
    @ManyToOne
    @JoinColumn(name = "almacen_id", nullable = false, foreignKey = @ForeignKey(name = "FK_almacen_venta"))
    private Almacen almacen;

    @Schema(description = "User que se realizó la Compra")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_user_venta"))
    private User user;

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

    @Schema(description = "Detalles de Compra")
    @OneToMany(mappedBy = "entradaStock", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<DetalleEntradaStock> detalleEntradaStock;

    @Schema(description = "Detalle de Estado")
    @Transient
    private String estadoStr;

    @Schema(description = "importe Total Decimales")
    @Transient
    private BigDecimal importeTotal;

    @Schema(description = "importe Pagado Decimales")
    @Transient
    private BigDecimal montoPagado;

    @Schema(description = "importe Por Pagar Decimales")
    @Transient
    private BigDecimal montoPorPagar;

    public EntradaStock() {
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public FacturaProveedor getFacturaProveedor() {
        return facturaProveedor;
    }

    public void setFacturaProveedor(FacturaProveedor facturaProveedor) {
        this.facturaProveedor = facturaProveedor;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<DetalleEntradaStock> getDetalleEntradaStock() {
        return detalleEntradaStock;
    }

    public void setDetalleEntradaStock(List<DetalleEntradaStock> detalleEntradaStock) {
        this.detalleEntradaStock = detalleEntradaStock;
    }

    public String getEstadoStr() {
        return estadoStr;
    }

    public void setEstadoStr(String estadoStr) {
        this.estadoStr = estadoStr;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public BigDecimal getMontoPorPagar() {
        return montoPorPagar;
    }

    public void setMontoPorPagar(BigDecimal montoPorPagar) {
        this.montoPorPagar = montoPorPagar;
    }

    public BigDecimal getTotalMonto() {
        return totalMonto;
    }

    public void setTotalMonto(BigDecimal totalMonto) {
        this.totalMonto = totalMonto;
    }
}

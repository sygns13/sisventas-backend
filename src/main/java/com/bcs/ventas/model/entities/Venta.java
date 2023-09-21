package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Schema(description = "Venta Model")
@Entity
@Table(name = "ventas")
public class Venta implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Fecha de registro de la Venta")
    //@NotNull( message = "{producto.fecha.notnull}")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    //@Column(name="cliente_id", nullable = true)
    //private Long clienteId;
    @Schema(description = "Cliente de la Venta")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = true, foreignKey = @ForeignKey(name = "FK_cliente_venta"))
    private Cliente cliente;

    //@Column(name="comprobante_id", nullable = true)
    //private Long comprobanteId;
    @Schema(description = "Comprobante de la Venta")
    @OneToOne
    @JoinColumn(name = "comprobante_id", nullable = true, foreignKey = @ForeignKey(name = "FK_comprobante_venta"))
    private Comprobante comprobante;


    @Schema(description = "Monto Inafecto de la Venta")
    @Column(name="subtotal_inafecto", nullable = true)
    private BigDecimal subtotalInafecto;

    @Schema(description = "Monto Afecto de la Venta")
    @Column(name="subtotal_afecto", nullable = true)
    private BigDecimal subtotalAfecto;

    @Schema(description = "Monto Exonerado de la Venta")
    @Column(name="subtotal_exonerado", nullable = true)
    private BigDecimal subtotalExonerado;

    @Schema(description = "Monto Total de la Venta")
    @Column(name="total_monto", nullable = true)
    private BigDecimal totalMonto;

    @Schema(description = "Monto Afecto de la Venta al ISC")
    @Column(name="total_afecto_isc", nullable = true)
    private BigDecimal totalAfectoIsc;

    @Schema(description = "Monto IGV de la Venta")
    @Column(name="igv", nullable = true)
    private BigDecimal igv;

    @Schema(description = "Monto ISC de la Venta")
    @Column(name="isc", nullable = true)
    private BigDecimal isc;

    @Schema(description = "Estado de la Venta")
    @Column(name="estado", nullable = true)
    private Integer estado;

    @Schema(description = "Estado de Pago de la Venta")
    @Column(name="pagado", nullable = true)
    private Integer pagado;

    @Schema(description = "Hora de la Venta")
    @Column(name="hora", nullable = true)
    @JsonFormat(pattern="HH:mm:ss")
    //@Temporal(TemporalType.TIME)
    //@DateTimeFormat(pattern="HH:mm:ss")
    private LocalTime hora;

    @Schema(description = "Tipo de Venta")
    @Column(name="tipo", nullable = true)
    private Integer tipo;

    @Schema(description = "Almacen donde se realizó la Venta")
    @ManyToOne
    @JoinColumn(name = "almacen_id", nullable = false, foreignKey = @ForeignKey(name = "FK_almacen_venta"))
    private Almacen almacen;

    @Schema(description = "User que se realizó la Venta")
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

    @Schema(description = "Numero de Venta por Sucursal")
    @Column(name="numero_venta", nullable = true)
    private String numeroVenta;

    @Schema(description = "Cantidad de Bolsas ICBPER")
    @Column(name="cantidad_icbper", nullable = true)
    private Integer cantidadIcbper;

    @Schema(description = "Monto activo del ICBPER")
    @Column(name="monto_icbper", nullable = true)
    private BigDecimal montoIcbper;

    @Schema(description = "Detalles de Venta")
    @OneToMany(mappedBy = "venta", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<DetalleVenta> detalleVentas;

    public Venta() {
    }

    public Venta(Long id, LocalDate fecha, Cliente cliente, Comprobante comprobante, BigDecimal subtotalInafecto, BigDecimal subtotalAfecto, BigDecimal subtotalExonerado, BigDecimal totalMonto, BigDecimal totalAfectoIsc, BigDecimal igv, BigDecimal isc, Integer estado, Integer pagado, LocalTime hora, Integer tipo, Almacen almacen, User user, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd, String numeroVenta, Integer cantidadIcbper, BigDecimal montoIcbper) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.comprobante = comprobante;
        this.subtotalInafecto = subtotalInafecto;
        this.subtotalAfecto = subtotalAfecto;
        this.subtotalExonerado = subtotalExonerado;
        this.totalMonto = totalMonto;
        this.totalAfectoIsc = totalAfectoIsc;
        this.igv = igv;
        this.isc = isc;
        this.estado = estado;
        this.pagado = pagado;
        this.hora = hora;
        this.tipo = tipo;
        this.almacen = almacen;
        this.user = user;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
        this.numeroVenta = numeroVenta;
        this.cantidadIcbper = cantidadIcbper;
        this.montoIcbper = montoIcbper;
    }

    public Venta(Long id, LocalDate fecha, Cliente cliente, Comprobante comprobante, BigDecimal subtotalInafecto, BigDecimal subtotalAfecto, BigDecimal subtotalExonerado, BigDecimal totalMonto, BigDecimal totalAfectoIsc, BigDecimal igv, BigDecimal isc, Integer estado, Integer pagado, LocalTime hora, Integer tipo, Almacen almacen, User user, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd, String numeroVenta, Integer cantidadIcbper, BigDecimal montoIcbper, List<DetalleVenta> detalleVentas) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.comprobante = comprobante;
        this.subtotalInafecto = subtotalInafecto;
        this.subtotalAfecto = subtotalAfecto;
        this.subtotalExonerado = subtotalExonerado;
        this.totalMonto = totalMonto;
        this.totalAfectoIsc = totalAfectoIsc;
        this.igv = igv;
        this.isc = isc;
        this.estado = estado;
        this.pagado = pagado;
        this.hora = hora;
        this.tipo = tipo;
        this.almacen = almacen;
        this.user = user;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
        this.numeroVenta = numeroVenta;
        this.cantidadIcbper = cantidadIcbper;
        this.montoIcbper = montoIcbper;
        this.detalleVentas = detalleVentas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public BigDecimal getSubtotalInafecto() {
        return subtotalInafecto;
    }

    public void setSubtotalInafecto(BigDecimal subtotalInafecto) {
        this.subtotalInafecto = subtotalInafecto;
    }

    public BigDecimal getSubtotalAfecto() {
        return subtotalAfecto;
    }

    public void setSubtotalAfecto(BigDecimal subtotalAfecto) {
        this.subtotalAfecto = subtotalAfecto;
    }

    public BigDecimal getSubtotalExonerado() {
        return subtotalExonerado;
    }

    public void setSubtotalExonerado(BigDecimal subtotalExonerado) {
        this.subtotalExonerado = subtotalExonerado;
    }

    public BigDecimal getTotalMonto() {
        return totalMonto;
    }

    public void setTotalMonto(BigDecimal totalMonto) {
        this.totalMonto = totalMonto;
    }

    public BigDecimal getTotalAfectoIsc() {
        return totalAfectoIsc;
    }

    public void setTotalAfectoIsc(BigDecimal totalAfectoIsc) {
        this.totalAfectoIsc = totalAfectoIsc;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getIsc() {
        return isc;
    }

    public void setIsc(BigDecimal isc) {
        this.isc = isc;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getPagado() {
        return pagado;
    }

    public void setPagado(Integer pagado) {
        this.pagado = pagado;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
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

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public Integer getCantidadIcbper() {
        return cantidadIcbper;
    }

    public void setCantidadIcbper(Integer cantidadIcbper) {
        this.cantidadIcbper = cantidadIcbper;
    }

    public BigDecimal getMontoIcbper() {
        return montoIcbper;
    }

    public void setMontoIcbper(BigDecimal montoIcbper) {
        this.montoIcbper = montoIcbper;
    }

    public List<DetalleVenta> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<DetalleVenta> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }
}

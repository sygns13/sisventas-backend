package com.bcs.ventas.utils.beans;

import java.time.LocalDate;
import java.time.LocalTime;

public class FiltroEntradaStock {

    //Filtro Almacen
    private Long almacenId;

    //Filtros EntradaStocks
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private Integer facturado;
    private Integer actualizado;
    private Integer estado;
    private Integer ordenCompraId;
    private String numeroEntradaStock;

    //Filtros Proveedores
    private Long idProveedor;
    private String nombreProveedor;
    private String documentoProveedor;
    private Long idTipoDocumentoProveedor;

    //Filtros FacturaProveedor
    private Long idFacturaProveedor;
    private String serieFacturaProveedor;
    private String numeroFacturaProveedor;
    private Long idTipoComprobante;

    //Filtros User
    private Long idUser;
    private String nameUser;
    private String emailUser;
    private Long idTipoUser;
    private String buscarDatosUser;

    //Buscar General
    private String buscarDatos;

    private Integer estadoPago;

    //Filtro para Detalles
    private Long idProducto;

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
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

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
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

    public Integer getOrdenCompraId() {
        return ordenCompraId;
    }

    public void setOrdenCompraId(Integer ordenCompraId) {
        this.ordenCompraId = ordenCompraId;
    }

    public String getNumeroEntradaStock() {
        return numeroEntradaStock;
    }

    public void setNumeroEntradaStock(String numeroEntradaStock) {
        this.numeroEntradaStock = numeroEntradaStock;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getDocumentoProveedor() {
        return documentoProveedor;
    }

    public void setDocumentoProveedor(String documentoProveedor) {
        this.documentoProveedor = documentoProveedor;
    }

    public Long getIdTipoDocumentoProveedor() {
        return idTipoDocumentoProveedor;
    }

    public void setIdTipoDocumentoProveedor(Long idTipoDocumentoProveedor) {
        this.idTipoDocumentoProveedor = idTipoDocumentoProveedor;
    }

    public Long getIdFacturaProveedor() {
        return idFacturaProveedor;
    }

    public void setIdFacturaProveedor(Long idFacturaProveedor) {
        this.idFacturaProveedor = idFacturaProveedor;
    }

    public String getSerieFacturaProveedor() {
        return serieFacturaProveedor;
    }

    public void setSerieFacturaProveedor(String serieFacturaProveedor) {
        this.serieFacturaProveedor = serieFacturaProveedor;
    }

    public String getNumeroFacturaProveedor() {
        return numeroFacturaProveedor;
    }

    public void setNumeroFacturaProveedor(String numeroFacturaProveedor) {
        this.numeroFacturaProveedor = numeroFacturaProveedor;
    }

    public Long getIdTipoComprobante() {
        return idTipoComprobante;
    }

    public void setIdTipoComprobante(Long idTipoComprobante) {
        this.idTipoComprobante = idTipoComprobante;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public Long getIdTipoUser() {
        return idTipoUser;
    }

    public void setIdTipoUser(Long idTipoUser) {
        this.idTipoUser = idTipoUser;
    }

    public String getBuscarDatosUser() {
        return buscarDatosUser;
    }

    public void setBuscarDatosUser(String buscarDatosUser) {
        this.buscarDatosUser = buscarDatosUser;
    }

    public String getBuscarDatos() {
        return buscarDatos;
    }

    public void setBuscarDatos(String buscarDatos) {
        this.buscarDatos = buscarDatos;
    }

    public Integer getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(Integer estadoPago) {
        this.estadoPago = estadoPago;
    }
}

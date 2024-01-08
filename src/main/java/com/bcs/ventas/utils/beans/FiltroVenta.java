package com.bcs.ventas.utils.beans;

import java.time.LocalDate;
import java.time.LocalTime;

public class FiltroVenta {

    //Filtro Almacen
    private Long almacenId;

    //Filtros Ventas
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private Integer estadoVenta;
    private Integer pagado;
    private Integer tipoVenta;
    private String numeroVenta;

    //Filtros Clientes
    private Long idCliente;
    private String nombreCliente;
    private String documentoCliente;
    private Long idTipoDocumentoCliente;

    //Filtros Comprobante
    private Long idComprobante;
    private String serieComprobante;
    private String numeroComprobante;
    private Long idTipoComprobante;

    //Filtros User
    private Long idUser;
    private String nameUser;
    private String emailUser;
    private Long idTipoUser;
    private String buscarDatosUser;

    //Buscar General
    private String buscarDatos;

    //Filtro para Detalles
    private Long idProducto;

    public FiltroVenta() {
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public FiltroVenta(Long almacenId, Long id, LocalDate fecha, LocalTime hora, LocalDate fechaInicio, LocalDate fechaFinal, Integer estadoVenta, Integer pagado, Integer tipoVenta, String numeroVenta, Long idCliente, String nombreCliente, String documentoCliente, Long idTipoDocumentoCliente, Long idComprobante, String serieComprobante, String numeroComprobante, Long idTipoComprobante, Long idUser, String nameUser, String emailUser, Long idTipoUser, String buscarDatosUser, String buscarDatos) {
        this.almacenId = almacenId;
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.estadoVenta = estadoVenta;
        this.pagado = pagado;
        this.tipoVenta = tipoVenta;
        this.numeroVenta = numeroVenta;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.documentoCliente = documentoCliente;
        this.idTipoDocumentoCliente = idTipoDocumentoCliente;
        this.idComprobante = idComprobante;
        this.serieComprobante = serieComprobante;
        this.numeroComprobante = numeroComprobante;
        this.idTipoComprobante = idTipoComprobante;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.emailUser = emailUser;
        this.idTipoUser = idTipoUser;
        this.buscarDatosUser = buscarDatosUser;
        this.buscarDatos = buscarDatos;
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

    public Integer getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(Integer estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public Integer getPagado() {
        return pagado;
    }

    public void setPagado(Integer pagado) {
        this.pagado = pagado;
    }

    public Integer getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(Integer tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
    }

    public Long getIdTipoDocumentoCliente() {
        return idTipoDocumentoCliente;
    }

    public void setIdTipoDocumentoCliente(Long idTipoDocumentoCliente) {
        this.idTipoDocumentoCliente = idTipoDocumentoCliente;
    }

    public Long getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(Long idComprobante) {
        this.idComprobante = idComprobante;
    }

    public String getSerieComprobante() {
        return serieComprobante;
    }

    public void setSerieComprobante(String serieComprobante) {
        this.serieComprobante = serieComprobante;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
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

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public String getBuscarDatos() {
        return buscarDatos;
    }

    public void setBuscarDatos(String buscarDatos) {
        this.buscarDatos = buscarDatos;
    }


}

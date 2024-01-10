package com.bcs.ventas.utils.beans;

import java.time.LocalDate;

public class FiltroGeneral {

    private Long almacenId;

    private Long unidadId;

    private String palabraClave;

    private Integer page;

    private Integer size;

    private Integer tipo;

     private LocalDate fechaInicio;

     private LocalDate fechaFinal;

    private LocalDate fecha;

    private Integer tipoComprobanteOtros;

    public FiltroGeneral() {
    }

    public Integer getTipoComprobanteOtros() {
        return tipoComprobanteOtros;
    }

    public void setTipoComprobanteOtros(Integer tipoComprobanteOtros) {
        this.tipoComprobanteOtros = tipoComprobanteOtros;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getPalabraClave() {
        return palabraClave;
    }

    public void setPalabraClave(String palabraClave) {
        this.palabraClave = palabraClave;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
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

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }
}

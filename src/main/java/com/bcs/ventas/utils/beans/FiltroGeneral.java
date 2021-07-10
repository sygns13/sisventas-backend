package com.bcs.ventas.utils.beans;

import java.time.LocalDate;

public class FiltroGeneral {

    private Long almacenId;

    private String palabraClave;

    private Integer page;

    private Integer size;

    private Integer tipo;

     private LocalDate fechaInicio;

     private LocalDate fechaFinal;

    public FiltroGeneral() {
    }

    public FiltroGeneral(Long almacenId, String palabraClave, Integer page, Integer size, Integer tipo, LocalDate fechaInicio, LocalDate fechaFinal) {
        this.almacenId = almacenId;
        this.palabraClave = palabraClave;
        this.page = page;
        this.size = size;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
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
}

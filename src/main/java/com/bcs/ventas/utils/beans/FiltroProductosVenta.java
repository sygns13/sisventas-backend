package com.bcs.ventas.utils.beans;

public class FiltroProductosVenta {

    private Long almacenId;

    private Long unidadId;

    private String palabraClave;

    private Integer page;

    private Integer size;

    public FiltroProductosVenta() {
    }

    public FiltroProductosVenta(Long almacenId, Long unidadId, String palabraClave, Integer page, Integer size) {
        this.almacenId = almacenId;
        this.unidadId = unidadId;
        this.palabraClave = palabraClave;
        this.page = page;
        this.size = size;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
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
}

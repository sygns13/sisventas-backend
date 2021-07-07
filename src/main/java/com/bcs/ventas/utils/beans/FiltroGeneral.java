package com.bcs.ventas.utils.beans;

public class FiltroGeneral {

    private Long almacenId;

    private String palabraClave;

    private Integer page;

    private Integer size;

    public FiltroGeneral() {
    }

    public FiltroGeneral(Long almacenId, String palabraClave, Integer page, Integer size) {
        this.almacenId = almacenId;
        this.palabraClave = palabraClave;
        this.page = page;
        this.size = size;
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
}

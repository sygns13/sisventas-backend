package com.bcs.ventas.utils.beans;

public class FiltroInventario {

    private Long almacenId;

    private Long tipoProductoId;

    private Long marcaId;

    private Long presentacionId;

    private String prioridad;

    private String codigo;

    private String nombre;

    private String composicion;

    private String ubicacion;

    private Integer page;

    private Integer size;

    public FiltroInventario() {
    }

    public FiltroInventario(Long almacenId, Long tipoProductoId, Long marcaId, Long presentacionId, String prioridad, String codigo, String nombre, String composicion, String ubicacion, Integer page, Integer size) {
        this.almacenId = almacenId;
        this.tipoProductoId = tipoProductoId;
        this.marcaId = marcaId;
        this.presentacionId = presentacionId;
        this.prioridad = prioridad;
        this.codigo = codigo;
        this.nombre = nombre;
        this.composicion = composicion;
        this.ubicacion = ubicacion;
        this.page = page;
        this.size = size;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Long getTipoProductoId() {
        return tipoProductoId;
    }

    public void setTipoProductoId(Long tipoProductoId) {
        this.tipoProductoId = tipoProductoId;
    }

    public Long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Long marcaId) {
        this.marcaId = marcaId;
    }

    public Long getPresentacionId() {
        return presentacionId;
    }

    public void setPresentacionId(Long presentacionId) {
        this.presentacionId = presentacionId;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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

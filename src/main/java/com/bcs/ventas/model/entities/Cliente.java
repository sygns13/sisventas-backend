package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Cliente Model")
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del Cliente")
    @NotNull( message = "{cliente.nombre.notnull}")
    @Size(max = 500, message = "{cliente.nombre.size}")
    @Column(name="nombre", nullable = true, length= 500)
    private String nombre;

    /*
    @Schema(description = "Tipo Documento del CLiente")
    @NotNull( message = "{cliente.tipoDocumentoId.notnull}")
    @Column(name="tipo_documento_id", nullable = true)
    private Long tipoDocumentoId;*/

    @Schema(description = "Tipo Documento del CLiente")
    @NotNull( message = "{cliente.tipoDocumentoId.notnull}")
    @ManyToOne
    @JoinColumn(name = "tipo_documento_id", nullable = false, foreignKey = @ForeignKey(name = "FK_tipo_documento"))
    private TipoDocumento tipoDocumento;

    @Schema(description = "Documento del Cliente")
    @NotNull( message = "{cliente.documento.notnull}")
    @Size(min = 8, max = 45, message = "{cliente.documento.size}")
    @Column(name="documento", nullable = true, length= 45)
    private String documento;

    @Schema(description = "Dirección del Cliente")
    @Size(max = 500, message = "{cliente.direccion.size}")
    @Column(name="direccion", nullable = true, length= 500)
    private String direccion;

    @Schema(description = "Teléfono del Cliente")
    @Size(max = 25, message = "{cliente.telefono.size}")
    @Column(name="telefono", nullable = true, length= 25)
    private String telefono;

    @Schema(description = "Correo 1 del Cliente")
    @Size(max = 1000, message = "{cliente.correo1.size}")
    @Column(name="correo1", nullable = true, length= 1000)
    private String correo1;

    @Schema(description = "Correo 2 del Cliente")
    @Size(max = 1000, message = "{cliente.correo2.size}")
    @Column(name="correo2", nullable = true, length= 1000)
    private String correo2;

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

    public Cliente() {
    }

    public Cliente(Long id, String nombre, TipoDocumento tipoDocumento, String documento, String direccion, String telefono, String correo1, String correo2, Long userId, Long empresaId, Integer activo, Integer borrado) {
        this.id = id;
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo1 = correo1;
        this.correo2 = correo2;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
    }

    public Cliente(Long id, String nombre, TipoDocumento tipoDocumento, String documento, String direccion, String telefono, String correo1, String correo2, Long userId, Long empresaId, Integer activo, Integer borrado, LocalDateTime createdAt, LocalDateTime updatedAd) {
        this.id = id;
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo1 = correo1;
        this.correo2 = correo2;
        this.userId = userId;
        this.empresaId = empresaId;
        this.activo = activo;
        this.borrado = borrado;
        this.createdAt = createdAt;
        this.updatedAd = updatedAd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo1() {
        return correo1;
    }

    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    public String getCorreo2() {
        return correo2;
    }

    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
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

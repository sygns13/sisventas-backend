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

@Schema(description = "Proveedor Model")
@Entity
@Table(name = "proveedors")
public class Proveedor implements Serializable {

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @Column(name="tipo_documento_id", nullable = true)
    private Long tipoDocumentoId;
     */

    @Schema(description = "Tipo Documento del Proveedor")
    @NotNull( message = "{proveedor.tipoDocumentoId.notnull}")
    @ManyToOne
    @JoinColumn(name = "tipo_documento_id", nullable = false, foreignKey = @ForeignKey(name = "FK_tipo_documento"))
    private TipoDocumento tipoDocumento;

    @Schema(description = "Documento del Proveedor")
    @NotNull( message = "{proveedor.documento.notnull}")
    @Size(min = 8, max = 45, message = "{proveedor.documento.size}")
    @Column(name="documento", nullable = true, length= 45)
    private String documento;

    @Schema(description = "Nombre del Proveedor")
    @NotNull( message = "{proveedor.nombre.notnull}")
    @Size(max = 500, message = "{proveedor.nombre.size}")
    @Column(name="nombre", nullable = true, length= 500)
    private String nombre;

    @Schema(description = "Dirección del Proveedor")
    @Size(max = 500, message = "{proveedor.direccion.size}")
    @Column(name="direccion", nullable = true, length= 500)
    private String direccion;

    @Schema(description = "Teléfono del Proveedor")
    @Size(max = 25, message = "{proveedor.telefono.size}")
    @Column(name="telefono", nullable = true, length= 25)
    private String telefono;

    @Schema(description = "Anexo del Proveedor")
    @Size(max = 25, message = "{proveedor.anexo.size}")
    @Column(name="anexo", nullable = true, length= 45)
    private String anexo;

    @Schema(description = "Celular del Proveedor")
    @Size(max = 25, message = "{proveedor.celular.size}")
    @Column(name="celular", nullable = true, length= 45)
    private String celular;

    @Schema(description = "Nombre del Responsable del Proveedor")
    @Size(max = 500, message = "{proveedor.responsable.size}")
    @Column(name="responsable", nullable = true, length= 500)
    private String responsable;

    @Schema(description = "ID User")
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

    public Proveedor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
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

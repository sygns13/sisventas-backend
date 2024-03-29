package com.bcs.ventas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Cabecera Comprobante Model")
@Entity
@Table(name = "cabecera_comprobantes")
public class CabeceraComprobante implements Serializable {

    private static final long serialVersionUID = 1L;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="id_externo", nullable = true, length= 50)
    private String idExterno;

    @Column(name="empr_razonsocial", nullable = true, length= 200)
    private String empRazonsocial;

    @Column(name="empr_ubigeo", nullable = true, length= 6)
    private String empUbigeo;

    @Column(name="empr_nombrecomercial", nullable = true, length= 200)
    private String empNombrecomercial;

    @Column(name="empr_direccion", nullable = true, length= 200)
    private String empDireccion;

    @Column(name="empr_provincia", nullable = true, length= 200)
    private String empProvincia;

    @Column(name="empr_departamento", nullable = true, length= 200)
    private String empDepartamento;

    @Column(name="empr_distrito", nullable = true, length= 200)
    private String empDistrito;

    @Column(name="empr_pais", nullable = true, length= 200)
    private String empPais;

    @Column(name="empr_nroruc", nullable = true, length= 11)
    private String empNroruc;

    @Column(name="empr_tipodoc", nullable = true, length= 45)
    private String empTipodoc;

    @Column(name="clie_numero", nullable = true, length= 45)
    private String cliNumero;

    @Column(name="clie_tipodoc", nullable = true, length= 45)
    private String cliTipodoc;

    @Column(name="clie_tipo_doc_label", nullable = true, length= 50)
    private String cliTipodocLabel;

    @Column(name="clie_nombre", nullable = true, length= 200)
    private String cliNombre;

    /*
    @Column(name="docu_fecha", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date docFecha;*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="docu_fecha", nullable = true)
    private LocalDate docFecha;

    @Column(name="docu_tipodocumento", nullable = true, length= 45)
    private String docTipodocumento;

    @Column(name="docu_numero", nullable = true, length= 45)
    private String docNumero;

    @Column(name="docu_moneda", nullable = true, length= 45)
    private String docMoneda;

    @Column(name="docu_gravada", nullable = true, length= 45)
    private String docGravada;

    @Column(name="docu_inafecta", nullable = true, length= 45)
    private String docInafecta;

    @Column(name="docu_exonerada", nullable = true, length= 45)
    private String docExonerada;

    @Column(name="docu_gratuita", nullable = true, length= 45)
    private String docGratuita;

    @Column(name="docu_descuento", nullable = true, length= 45)
    private String docDescuento;

    @Column(name="docu_subtotal", nullable = true, length= 45)
    private String docSubtotal;

    @Column(name="docu_total", nullable = true, length= 45)
    private String docTotal;

    @Column(name="docu_igv", nullable = true, length= 45)
    private String docIgv;

    @Column(name="tasa_igv", nullable = true, length= 45)
    private String tasaIgv;

    @Column(name="docu_isc", nullable = true, length= 45)
    private String docIsc;

    @Column(name="tasa_isc", nullable = true, length= 45)
    private String tasaIsc;

    @Column(name="docu_otrostributos", nullable = true, length= 45)
    private String docOtrostributos;

    @Column(name="tasa_otrostributos", nullable = true, length= 45)
    private String tasaOtrostributos;

    @Column(name="docu_otroscargos", nullable = true, length= 45)
    private String docOtroscargos;

    @Column(name="docu_percepcion", nullable = true, length= 45)
    private String docPercepcion;

    @Column(name="hashcode", nullable = true, length= 100)
    private String hashcode;

    @Column(name="cdr", nullable = true, length= 200)
    private String cdr;

    @Column(name="cdr_nota", nullable = true, length= 500)
    private String cdrNota;

    @Column(name="cdr_observacion", nullable = true, length= 2000)
    private String cdrObservacion;

    @Column(name="docu_enviaws", nullable = true, length= 45)
    private String docEnviaws;

    @Column(name="docu_proce_status", nullable = true, length= 45)
    private String docProceStatus;

    @Column(name="docu_proce_fecha", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date docProceFecha;

    @Column(name="docu_link_pdf", nullable = true, length= 200)
    private String docLinkPdf;

    @Column(name="docu_link_cdr", nullable = true, length= 200)
    private String docLinkCdr;

    @Column(name="docu_link_xml", nullable = true, length= 200)
    private String docLinkXml;

    @Column(name="clie_correo_cpe1", nullable = true, length= 100)
    private String cliCorreoCpe1;

    @Column(name="clie_correo_cpe2", nullable = true, length= 100)
    private String cliCorreoCpe2;

    @Column(name="venta_id", nullable = true)
    private Long ventaId;

    @Column(name="empresa_id", nullable = true)
    private Long empresaId;

    @Column(name="user_id", nullable = true)
    private Long userId;

    @Column(name="activo", nullable = true)
    private Integer activo;

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

    @Column(name="activo_cpe", nullable = true)
    private Integer activoCpe;

    @Column(name="importe_letras", nullable = true, length= 500)
    private String importeLetras;

    @Column(name="icbper", nullable = true, length= 45)
    private String icbper;

    public CabeceraComprobante() {
    }

    public String getCliTipodocLabel() {
        return cliTipodocLabel;
    }

    public void setCliTipodocLabel(String cliTipodocLabel) {
        this.cliTipodocLabel = cliTipodocLabel;
    }

    public String getIcbper() {
        return icbper;
    }

    public void setIcbper(String icbper) {
        this.icbper = icbper;
    }

    public String getImporteLetras() {
        return importeLetras;
    }

    public void setImporteLetras(String importeLetras) {
        this.importeLetras = importeLetras;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(String idExterno) {
        this.idExterno = idExterno;
    }

    public String getEmpRazonsocial() {
        return empRazonsocial;
    }

    public void setEmpRazonsocial(String empRazonsocial) {
        this.empRazonsocial = empRazonsocial;
    }

    public String getEmpUbigeo() {
        return empUbigeo;
    }

    public void setEmpUbigeo(String empUbigeo) {
        this.empUbigeo = empUbigeo;
    }

    public String getEmpNombrecomercial() {
        return empNombrecomercial;
    }

    public void setEmpNombrecomercial(String empNombrecomercial) {
        this.empNombrecomercial = empNombrecomercial;
    }

    public String getEmpDireccion() {
        return empDireccion;
    }

    public void setEmpDireccion(String empDireccion) {
        this.empDireccion = empDireccion;
    }

    public String getEmpProvincia() {
        return empProvincia;
    }

    public void setEmpProvincia(String empProvincia) {
        this.empProvincia = empProvincia;
    }

    public String getEmpDepartamento() {
        return empDepartamento;
    }

    public void setEmpDepartamento(String empDepartamento) {
        this.empDepartamento = empDepartamento;
    }

    public String getEmpDistrito() {
        return empDistrito;
    }

    public void setEmpDistrito(String empDistrito) {
        this.empDistrito = empDistrito;
    }

    public String getEmpPais() {
        return empPais;
    }

    public void setEmpPais(String empPais) {
        this.empPais = empPais;
    }

    public String getEmpNroruc() {
        return empNroruc;
    }

    public void setEmpNroruc(String empNroruc) {
        this.empNroruc = empNroruc;
    }

    public String getEmpTipodoc() {
        return empTipodoc;
    }

    public void setEmpTipodoc(String empTipodoc) {
        this.empTipodoc = empTipodoc;
    }

    public String getCliNumero() {
        return cliNumero;
    }

    public void setCliNumero(String cliNumero) {
        this.cliNumero = cliNumero;
    }

    public String getCliTipodoc() {
        return cliTipodoc;
    }

    public void setCliTipodoc(String cliTipodoc) {
        this.cliTipodoc = cliTipodoc;
    }

    public String getCliNombre() {
        return cliNombre;
    }

    public void setCliNombre(String cliNombre) {
        this.cliNombre = cliNombre;
    }

    public LocalDate getDocFecha() {
        return docFecha;
    }

    public void setDocFecha(LocalDate docFecha) {
        this.docFecha = docFecha;
    }

    public String getDocTipodocumento() {
        return docTipodocumento;
    }

    public void setDocTipodocumento(String docTipodocumento) {
        this.docTipodocumento = docTipodocumento;
    }

    public String getDocNumero() {
        return docNumero;
    }

    public void setDocNumero(String docNumero) {
        this.docNumero = docNumero;
    }

    public String getDocMoneda() {
        return docMoneda;
    }

    public void setDocMoneda(String docMoneda) {
        this.docMoneda = docMoneda;
    }

    public String getDocGravada() {
        return docGravada;
    }

    public void setDocGravada(String docGravada) {
        this.docGravada = docGravada;
    }

    public String getDocInafecta() {
        return docInafecta;
    }

    public void setDocInafecta(String docInafecta) {
        this.docInafecta = docInafecta;
    }

    public String getDocExonerada() {
        return docExonerada;
    }

    public void setDocExonerada(String docExonerada) {
        this.docExonerada = docExonerada;
    }

    public String getDocGratuita() {
        return docGratuita;
    }

    public void setDocGratuita(String docGratuita) {
        this.docGratuita = docGratuita;
    }

    public String getDocDescuento() {
        return docDescuento;
    }

    public void setDocDescuento(String docDescuento) {
        this.docDescuento = docDescuento;
    }

    public String getDocSubtotal() {
        return docSubtotal;
    }

    public void setDocSubtotal(String docSubtotal) {
        this.docSubtotal = docSubtotal;
    }

    public String getDocTotal() {
        return docTotal;
    }

    public void setDocTotal(String docTotal) {
        this.docTotal = docTotal;
    }

    public String getDocIgv() {
        return docIgv;
    }

    public void setDocIgv(String docIgv) {
        this.docIgv = docIgv;
    }

    public String getTasaIgv() {
        return tasaIgv;
    }

    public void setTasaIgv(String tasaIgv) {
        this.tasaIgv = tasaIgv;
    }

    public String getDocIsc() {
        return docIsc;
    }

    public void setDocIsc(String docIsc) {
        this.docIsc = docIsc;
    }

    public String getTasaIsc() {
        return tasaIsc;
    }

    public void setTasaIsc(String tasaIsc) {
        this.tasaIsc = tasaIsc;
    }

    public String getDocOtrostributos() {
        return docOtrostributos;
    }

    public void setDocOtrostributos(String docOtrostributos) {
        this.docOtrostributos = docOtrostributos;
    }

    public String getTasaOtrostributos() {
        return tasaOtrostributos;
    }

    public void setTasaOtrostributos(String tasaOtrostributos) {
        this.tasaOtrostributos = tasaOtrostributos;
    }

    public String getDocOtroscargos() {
        return docOtroscargos;
    }

    public void setDocOtroscargos(String docOtroscargos) {
        this.docOtroscargos = docOtroscargos;
    }

    public String getDocPercepcion() {
        return docPercepcion;
    }

    public void setDocPercepcion(String docPercepcion) {
        this.docPercepcion = docPercepcion;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getCdr() {
        return cdr;
    }

    public void setCdr(String cdr) {
        this.cdr = cdr;
    }

    public String getCdrNota() {
        return cdrNota;
    }

    public void setCdrNota(String cdrNota) {
        this.cdrNota = cdrNota;
    }

    public String getCdrObservacion() {
        return cdrObservacion;
    }

    public void setCdrObservacion(String cdrObservacion) {
        this.cdrObservacion = cdrObservacion;
    }

    public String getDocEnviaws() {
        return docEnviaws;
    }

    public void setDocEnviaws(String docEnviaws) {
        this.docEnviaws = docEnviaws;
    }

    public String getDocProceStatus() {
        return docProceStatus;
    }

    public void setDocProceStatus(String docProceStatus) {
        this.docProceStatus = docProceStatus;
    }

    public Date getDocProceFecha() {
        return docProceFecha;
    }

    public void setDocProceFecha(Date docProceFecha) {
        this.docProceFecha = docProceFecha;
    }

    public String getDocLinkPdf() {
        return docLinkPdf;
    }

    public void setDocLinkPdf(String docLinkPdf) {
        this.docLinkPdf = docLinkPdf;
    }

    public String getDocLinkCdr() {
        return docLinkCdr;
    }

    public void setDocLinkCdr(String docLinkCdr) {
        this.docLinkCdr = docLinkCdr;
    }

    public String getDocLinkXml() {
        return docLinkXml;
    }

    public void setDocLinkXml(String docLinkXml) {
        this.docLinkXml = docLinkXml;
    }

    public String getCliCorreoCpe1() {
        return cliCorreoCpe1;
    }

    public void setCliCorreoCpe1(String cliCorreoCpe1) {
        this.cliCorreoCpe1 = cliCorreoCpe1;
    }

    public String getCliCorreoCpe2() {
        return cliCorreoCpe2;
    }

    public void setCliCorreoCpe2(String cliCorreoCpe2) {
        this.cliCorreoCpe2 = cliCorreoCpe2;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getActivoCpe() {
        return activoCpe;
    }

    public void setActivoCpe(Integer activoCpe) {
        this.activoCpe = activoCpe;
    }
}

package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.ProveedorDAO;
import com.bcs.ventas.dao.mappers.ProveedorMapper;
import com.bcs.ventas.model.entities.Proveedor;
import com.bcs.ventas.service.jasper.ProveedorJasperService;
import com.bcs.ventas.service.reportes.ProveedorReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.reportbeans.ProveedorReport;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProveedorReportServiceImpl implements ProveedorReportService {

    @Autowired
    private ProveedorJasperService proveedorJasperService;

    @Autowired
    private ProveedorDAO proveedorDAO;

    @Autowired
    private ProveedorMapper proveedorMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public byte[] exportPdf() throws JRException, FileNotFoundException {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Proveedor> proveedors = proveedorMapper.listByParameterMap(params);

        List<ProveedorReport> proveedorReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        proveedors.forEach(proveedor -> {
            ProveedorReport proveedorReport = new ProveedorReport();
            proveedorReport.setNro(nro.get());
            proveedorReport.setProveedor(proveedor.getNombre());
            proveedorReport.setTipoDocumento(proveedor.getTipoDocumento().getTipo());
            proveedorReport.setDocumento(proveedor.getDocumento());
            proveedorReport.setDireccion(proveedor.getDireccion());

            if(proveedor.getTelefono() != null && !proveedor.getTelefono().isEmpty() &&
                    proveedor.getAnexo() != null && !proveedor.getAnexo().isEmpty()){
                proveedorReport.setTelefonoAnexo(proveedor.getTelefono() + " - " + proveedor.getAnexo());
            } else if(proveedor.getTelefono() != null && !proveedor.getTelefono().isEmpty()){
                proveedorReport.setTelefonoAnexo(proveedor.getTelefono());
            } else if(proveedor.getAnexo() != null && !proveedor.getAnexo().isEmpty()){
                proveedorReport.setTelefonoAnexo(proveedor.getAnexo());
            }
            proveedorReport.setCelular(proveedor.getCelular());
            proveedorReport.setResponsable(proveedor.getResponsable());

            proveedorReports.add(proveedorReport);

            nro.set(nro.get() + 1L);
        });


        return proveedorJasperService.exportToPdf(proveedorReports,  "data","reportProveedores");
    }

    @Override
    public byte[] exportXls() throws JRException, FileNotFoundException {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Proveedor> proveedors = proveedorMapper.listByParameterMap(params);

        List<ProveedorReport> proveedorReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        proveedors.forEach(proveedor -> {
            ProveedorReport proveedorReport = new ProveedorReport();
            proveedorReport.setNro(nro.get());
            proveedorReport.setProveedor(proveedor.getNombre());
            proveedorReport.setTipoDocumento(proveedor.getTipoDocumento().getTipo());
            proveedorReport.setDocumento(proveedor.getDocumento());
            proveedorReport.setDireccion(proveedor.getDireccion());

            if(proveedor.getTelefono() != null && !proveedor.getTelefono().isEmpty() &&
                    proveedor.getAnexo() != null && !proveedor.getAnexo().isEmpty()){
                proveedorReport.setTelefonoAnexo(proveedor.getTelefono() + " - " + proveedor.getAnexo());
            } else if(proveedor.getTelefono() != null && !proveedor.getTelefono().isEmpty()){
                proveedorReport.setTelefonoAnexo(proveedor.getTelefono());
            } else if(proveedor.getAnexo() != null && !proveedor.getAnexo().isEmpty()){
                proveedorReport.setTelefonoAnexo(proveedor.getAnexo());
            }
            proveedorReport.setCelular(proveedor.getCelular());
            proveedorReport.setResponsable(proveedor.getResponsable());

            proveedorReports.add(proveedorReport);

            nro.set(nro.get() + 1L);
        });

        return proveedorJasperService.exportToXls(proveedorReports, "data", "reportProveedores", "hoja1");
    }
}

package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.MarcaDAO;
import com.bcs.ventas.dao.mappers.MarcaMapper;
import com.bcs.ventas.model.entities.Marca;
import com.bcs.ventas.service.jasper.MarcaJasperService;
import com.bcs.ventas.service.reportes.MarcaReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.reportbeans.MarcaReport;
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
public class MarcaReportServiceImpl implements MarcaReportService {

    @Autowired
    private MarcaJasperService marcaJasperService;

    @Autowired
    private MarcaDAO marcaDAO;

    @Autowired
    private MarcaMapper marcaMapper;

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Override
    public byte[] exportPdf() throws JRException, FileNotFoundException {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        //params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Marca> marcas = marcaMapper.listByParameterMap(params);

        List<MarcaReport> marcaReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        marcas.forEach(marca -> {
            MarcaReport marcaReport = new MarcaReport();
            marcaReport.setNro(nro.get());
            marcaReport.setMarca(marca.getNombre());

            if(marca.getActivo().compareTo(Constantes.REGISTRO_ACTIVO)==0)
                marcaReport.setEstado("Activo");
            else if(marca.getActivo().compareTo(Constantes.REGISTRO_INACTIVO)==0)
                marcaReport.setEstado("Inactivo");
            else
                marcaReport.setEstado("Desconocido");


            marcaReports.add(marcaReport);

            nro.set(nro.get() + 1L);
        });


        return marcaJasperService.exportToPdf(marcaReports,  "data","reportMarcas");
    }

    @Override
    public byte[] exportXls() throws JRException, FileNotFoundException {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO",Constantes.REGISTRO_BORRADO);
        //params.put("ACTIVO",Constantes.REGISTRO_ACTIVO);
        params.put("EMPRESA_ID",EmpresaId);

        List<Marca> marcas = marcaMapper.listByParameterMap(params);

        List<MarcaReport> marcaReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        marcas.forEach(marca -> {
            MarcaReport marcaReport = new MarcaReport();
            marcaReport.setNro(nro.get());
            marcaReport.setMarca(marca.getNombre());

            if(marca.getActivo().compareTo(Constantes.REGISTRO_ACTIVO)==0)
                marcaReport.setEstado("Activo");
            else if(marca.getActivo().compareTo(Constantes.REGISTRO_INACTIVO)==0)
                marcaReport.setEstado("Inactivo");
            else
                marcaReport.setEstado("Desconocido");

            marcaReports.add(marcaReport);

            nro.set(nro.get() + 1L);
        });

        return marcaJasperService.exportToXls(marcaReports, "data", "reportMarcas", "hoja1");
    }
}

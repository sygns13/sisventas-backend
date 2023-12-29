package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.TipoProductoDAO;
import com.bcs.ventas.dao.mappers.TipoProductoMapper;
import com.bcs.ventas.model.entities.TipoProducto;
import com.bcs.ventas.service.jasper.TipoProductoJasperService;
import com.bcs.ventas.service.reportes.TipoProductoReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.reportbeans.TipoProductoReport;
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
public class TipoProductoReportServiceImpl implements TipoProductoReportService {

    @Autowired
    private TipoProductoJasperService tipoProductoJasperService;

    @Autowired
    private TipoProductoDAO tipoProductoDAO;

    @Autowired
    private TipoProductoMapper tipoProductoMapper;

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

        List<TipoProducto> tipoProductos = tipoProductoMapper.listByParameterMap(params);

        List<TipoProductoReport> tipoProductoReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        tipoProductos.forEach(tipoProducto -> {
            TipoProductoReport tipoProductoReport = new TipoProductoReport();
            tipoProductoReport.setNro(nro.get());
            tipoProductoReport.setTipoProducto(tipoProducto.getTipo());

            if(tipoProducto.getActivo().compareTo(Constantes.REGISTRO_ACTIVO)==0)
                tipoProductoReport.setEstado("Activo");
            else if(tipoProducto.getActivo().compareTo(Constantes.REGISTRO_INACTIVO)==0)
                tipoProductoReport.setEstado("Inactivo");
            else
                tipoProductoReport.setEstado("Desconocido");


            tipoProductoReports.add(tipoProductoReport);

            nro.set(nro.get() + 1L);
        });


        return tipoProductoJasperService.exportToPdf(tipoProductoReports,  "data","reportTipoProductos");
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

        List<TipoProducto> tipoProductos = tipoProductoMapper.listByParameterMap(params);

        List<TipoProductoReport> tipoProductoReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        tipoProductos.forEach(tipoProducto -> {
            TipoProductoReport tipoProductoReport = new TipoProductoReport();
            tipoProductoReport.setNro(nro.get());
            tipoProductoReport.setTipoProducto(tipoProducto.getTipo());

            if(tipoProducto.getActivo().compareTo(Constantes.REGISTRO_ACTIVO)==0)
                tipoProductoReport.setEstado("Activo");
            else if(tipoProducto.getActivo().compareTo(Constantes.REGISTRO_INACTIVO)==0)
                tipoProductoReport.setEstado("Inactivo");
            else
                tipoProductoReport.setEstado("Desconocido");

            tipoProductoReports.add(tipoProductoReport);

            nro.set(nro.get() + 1L);
        });

        return tipoProductoJasperService.exportToXls(tipoProductoReports, "data", "reportTipoProductos", "hoja1");
    }
}

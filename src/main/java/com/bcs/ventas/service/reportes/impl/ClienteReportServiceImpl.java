package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.ClienteDAO;
import com.bcs.ventas.dao.mappers.ClienteMapper;
import com.bcs.ventas.model.entities.Cliente;
import com.bcs.ventas.service.jasper.ClienteJasperService;
import com.bcs.ventas.service.reportes.ClienteReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import com.bcs.ventas.utils.reportbeans.ClienteReport;
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
public class ClienteReportServiceImpl implements ClienteReportService {

    @Autowired
    private ClienteJasperService clienteJasperService;

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private ClienteMapper clienteMapper;

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

        List<Cliente> clientes = clienteMapper.listByParameterMap(params);

        List<ClienteReport> clienteReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        clientes.forEach(cliente -> {
            ClienteReport clienteReport = new ClienteReport();
            clienteReport.setNro(nro.get());
            clienteReport.setCliente(cliente.getNombre());
            clienteReport.setTipoDocumento(cliente.getTipoDocumento().getTipo());
            clienteReport.setDocumento(cliente.getDocumento());
            clienteReport.setDireccion(cliente.getDireccion());
            clienteReport.setTelefono(cliente.getTelefono());
            clienteReport.setCorreo1(cliente.getCorreo1());
            clienteReport.setCorreo2(cliente.getCorreo2());

            clienteReports.add(clienteReport);

            nro.set(nro.get() + 1L);
        });


        return clienteJasperService.exportToPdf(clienteReports,  "data","reportClientes");
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

        List<Cliente> clientes = clienteMapper.listByParameterMap(params);

        List<ClienteReport> clienteReports = new ArrayList<>();
        AtomicReference<Long> nro = new AtomicReference<>(1L);

        clientes.forEach(cliente -> {
            ClienteReport clienteReport = new ClienteReport();
            clienteReport.setNro(nro.get());
            clienteReport.setCliente(cliente.getNombre());
            clienteReport.setTipoDocumento(cliente.getTipoDocumento().getTipo());
            clienteReport.setDocumento(cliente.getDocumento());
            clienteReport.setDireccion(cliente.getDireccion());
            clienteReport.setTelefono(cliente.getTelefono());
            clienteReport.setCorreo1(cliente.getCorreo1());
            clienteReport.setCorreo2(cliente.getCorreo2());

            clienteReports.add(clienteReport);

            nro.set(nro.get() + 1L);
        });

        return clienteJasperService.exportToXls(clienteReports, "data", "reportClientes", "hoja1");
    }
}

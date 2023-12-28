package com.bcs.ventas.service.jasper.impl;


import com.bcs.ventas.service.jasper.ClienteJasperService;
import com.bcs.ventas.utils.reportbeans.ClienteReport;
import org.springframework.stereotype.Service;

@Service
public class ClienteJasperServiceImpl extends ReportGeneratorServiceImpl<ClienteReport> implements ClienteJasperService {
}

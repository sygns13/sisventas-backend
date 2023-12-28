package com.bcs.ventas.service.reportes;

import com.bcs.ventas.model.entities.Cliente;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;

public interface ClienteReportService {

    byte[] exportPdf() throws JRException, FileNotFoundException;
    byte[] exportXls() throws JRException, FileNotFoundException;
}

package com.bcs.ventas.service.reportes;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface TipoProductoReportService {

    byte[] exportPdf() throws JRException, FileNotFoundException;
    byte[] exportXls() throws JRException, FileNotFoundException;
}

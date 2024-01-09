package com.bcs.ventas.service.jasper;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.Map;

public interface ResumenCajaJasperService{

    byte[] exportToPdf(String nameData, String nameReport, Map<String, Object> parametros) throws JRException, FileNotFoundException;

    byte[] exportToXls(String nameData, String nameReport, String sheet, Map<String, Object> parametros) throws JRException, FileNotFoundException;
}
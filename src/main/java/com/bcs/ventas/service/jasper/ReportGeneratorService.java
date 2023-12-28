package com.bcs.ventas.service.jasper;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;

public interface ReportGeneratorService<T> {

    byte[] exportToPdf(List<T> list, String nameData, String nameReport) throws JRException, FileNotFoundException;

    byte[] exportToXls(List<T> list, String nameData, String nameReport, String sheet) throws JRException, FileNotFoundException;
}

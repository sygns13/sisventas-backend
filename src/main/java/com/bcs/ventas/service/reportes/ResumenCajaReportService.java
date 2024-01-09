package com.bcs.ventas.service.reportes;

import com.bcs.ventas.utils.beans.FiltroGeneral;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface ResumenCajaReportService {

    byte[] exportPdf(FiltroGeneral filtroGeneral) throws Exception;
    byte[] exportXls(FiltroGeneral filtroGeneral) throws Exception;
}

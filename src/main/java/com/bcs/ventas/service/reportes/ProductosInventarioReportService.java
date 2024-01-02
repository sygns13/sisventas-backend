package com.bcs.ventas.service.reportes;

import com.bcs.ventas.utils.beans.FiltroInventario;

import java.util.Map;

public interface ProductosInventarioReportService {

    byte[] exportPdf(FiltroInventario filtros) throws Exception;
    byte[] exportXls(FiltroInventario filtros) throws Exception;
}

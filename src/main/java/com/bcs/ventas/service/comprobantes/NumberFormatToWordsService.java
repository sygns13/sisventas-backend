package com.bcs.ventas.service.comprobantes;

public interface NumberFormatToWordsService {

    String Convert(String numero, String etiquetaEnteroSingular,String etiquetaEnteroPlural, String etiquetaFlotanteSingular,String etiquetaFlotantePlural, String etiquetaConector, boolean mayusculas);
}

package com.bcs.ventas.utils;

import java.util.ArrayList;
import java.util.List;

public class Constantes {

    public static final Integer REGISTRO_ACTIVO = 1;
    public static final Integer REGISTRO_INACTIVO = 0;

    public static final Integer REGISTRO_BORRADO = 1;
    public static final Integer REGISTRO_NO_BORRADO = 0;
    public static final Integer REGISTRO_NO_BORRADO_2_DEFAULT = 2;

    public static final String SUPER_ADMINISTRADOR = "1";

    public static final String ALMACEN_GENERAL = "0";
    public static final String CANTIDAD_UNIDAD = "1";

    public static final Integer CANTIDAD_ZERO = 0;

    //Intreso Retiro Lotes
    public static final String TIPO_ENTRADA_PRODUCTOS = "1";
    public static final String TIPO_RETIRO_PRODUCTOS = "0";
    public static final String MOTIVO_INGRESO_CREACION_LOTE = "Ingreso de Productos por Creación de Lote en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_SALIDA_ELIMINACION_LOTE = "Salida de Productos por Eliminación de Lote en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_INGRESO_MODIFICACION_LOTE = "Ingreso de Productos por Modificación de Lote en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_SALIDA_MODIFICACION_LOTE = "Salida de Productos por Modificación de Lote en el Módulo de Gestión de Stocks";

    //Intreso Retiro Stocks
    public static final String ID_INGRESO_SALIDA_STOCK_NO_LOTE = "0";
    public static final String MOTIVO_INGRESO_CREACION_STOCK = "Ingreso de Productos por Definición de Nuevo Stock en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_INGRESO_MODIFICACION_STOCK = "Ingreso de Productos por Modificación de Stock en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_SALIDA_MODIFICACION_STOCK = "Salida de Productos por Modificación de Stock en el Módulo de Gestión de Stocks";

}

package com.bcs.ventas.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public  class Constantes {
    public static final String VOID = "";


    public static final Integer REGISTRO_ACTIVO = 1;

    public static final Integer REGISTRO_ACTIVO_2 = 2;
    public static final Integer REGISTRO_INACTIVO = 0;

    public static final Integer REGISTRO_BORRADO = 1;
    public static final Integer REGISTRO_NO_BORRADO = 0;
    public static final Integer REGISTRO_NO_BORRADO_2_DEFAULT = 2;

    public static final String SUPER_ADMINISTRADOR = "1";

    public static final Long ALMACEN_GENERAL = 0L;
    public static final String CANTIDAD_UNIDAD = "1";

    public static final Integer CANTIDAD_ZERO = 0;
    public static final Integer CANTIDAD_MINIMA_PAGE = 5;
    public static final Integer CANTIDAD_UNIDAD_INTEGER = 1;

    public static final Double CANTIDAD_ZERO_DOUBLE = 0.0;

    public static final Double CANTIDAD_UNIDAD_DOUBLE = 1.0;

    public static final Double CANTIDAD_CIEN_DOUBLE = 100.0;

    public static final Long CANTIDAD_ZERO_LONG = 0L;

    public static final Long CANTIDAD_UNIDAD_LONG = 1L;

    public static final BigDecimal CANTIDAD_ZERO_BIG_DECIMAL = new BigDecimal("0");

    //Intreso Retiro Lotes
    public static final Integer TIPO_ENTRADA_PRODUCTOS = 1;
    public static final Integer TIPO_RETIRO_PRODUCTOS = 0;
    public static final String MOTIVO_INGRESO_CREACION_LOTE = "Ingreso de Productos por Creación de Lote en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_SALIDA_ELIMINACION_LOTE = "Salida de Productos por Eliminación de Lote en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_INGRESO_MODIFICACION_LOTE = "Ingreso de Productos por Modificación de Lote en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_SALIDA_MODIFICACION_LOTE = "Salida de Productos por Modificación de Lote en el Módulo de Gestión de Stocks";

    //Intreso Retiro Stocks
    public static final Long ID_INGRESO_SALIDA_STOCK_NO_LOTE = 0L;
    public static final String MOTIVO_INGRESO_CREACION_STOCK = "Ingreso de Productos por Definición de Nuevo Stock en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_INGRESO_MODIFICACION_STOCK = "Ingreso de Productos por Modificación de Stock en el Módulo de Gestión de Stocks";
    public static final String MOTIVO_SALIDA_MODIFICACION_STOCK = "Salida de Productos por Modificación de Stock en el Módulo de Gestión de Stocks";

    //Estados Ventas
    public static final Integer VENTA_ESTADO_ANULADO = 0;
    public static final Integer VENTA_ESTADO_INICIADO = 1;
    public static final Integer VENTA_ESTADO_VENTA_NO_COBRADA = 2;
    public static final Integer VENTA_ESTADO_VENTA_COBRADA_PARCIAL = 3;
    public static final Integer VENTA_ESTADO_VENTA_COBRADA_TOTAL = 4;

    public enum VentaStatus {
        VENTA_ESTADO_ANULADO(0),
        VENTA_ESTADO_INICIADO(1),
        VENTA_ESTADO_VENTA_NO_COBRADA(2),
        VENTA_ESTADO_VENTA_COBRADA_PARCIAL(3),
        VENTA_ESTADO_VENTA_COBRADA_TOTAL(4);

        private int value;
        VentaStatus(int i) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static final String VENTA_ESTADO_ANULADO_STR = "VENTA ANULADA";
    public static final String VENTA_ESTADO_INICIADO_STR = "VENTA INICIADA";
    public static final String VENTA_ESTADO_VENTA_NO_COBRADA_STR = "VENTA FACTURADA NO PAGADA";
    public static final String VENTA_ESTADO_VENTA_COBRADA_PARCIAL_STR = "VENTA FACTURADA PAGADA PARCIALMENTE";
    public static final String VENTA_ESTADO_VENTA_COBRADA_TOTAL_STR = "VENTA FACTURADA PAGADA";

    public static final Integer VENTA_NO_PAGADO = 0;
    public static final Integer VENTA_SI_PAGADO = 1;

    public static final Integer VENTA_TIPO_BIENES = 1;
    public static final Integer VENTA_TIPO_SERVICIOS = 2;


    //Afectos IGV
    public static final Integer GRABADO_IGV = 1;
    public static final Integer INAFECTO_IGV = 0;
    public static final Integer EXONERADO_IGV = 2;


    public static final Double PERCENTIL_IGV = 1.18;

    //Afectos ISC
    public static final Integer TIPO_ISC_PERCENTIL = 1;
    public static final Integer TIPO_ISC_FIJO = 1;

    //Tipos de Descuento
    public static final Integer TIPO_DESCUENTO_PORCENTIL = 1;
    public static final Integer TIPO_DESCUENTO_FIJO = 2;

    //ICBPER PERU 2023
    public static final BigDecimal CANTIDAD_ICBPER_2023 = new BigDecimal("0.5");

    //Metodos de pago
    public static final String ID_TIPO_METODO_PAGO_CASH = "CA";
    public static final String ID_TIPO_METODO_PAGO_CREDIT_CARD = "CC";
    public static final String ID_TIPO_METODO_PAGO_WIRE_TRANSFER = "WT";
    public static final String ID_TIPO_METODO_PAGO_E_WALLET = "EW";
    public static final String ID_TIPO_METODO_PAGO_CHEQUE = "CH";

    //Estados de Comprobantes
    public static final String COMPROBANTE_ESTADO_ANULADO = "0";
    public static final String COMPROBANTE_ESTADO_CREADO = "1";
    public static final String COMPROBANTE_ESTADO_FACTURADO = "2";

}

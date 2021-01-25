package com.bcs.ventas.exception;

public class ModeloNotFoundException extends RuntimeException{

    public ModeloNotFoundException(String mensaje) {
        super(mensaje);
    }
}

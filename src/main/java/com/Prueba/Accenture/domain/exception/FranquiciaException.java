package com.Prueba.Accenture.domain.exception;

// Regla de negocio violada -> 400
public class FranquiciaException extends RuntimeException {
    public FranquiciaException(String mensaje) {
        super(mensaje);
    }
}

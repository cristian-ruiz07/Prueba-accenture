package com.Prueba.Accenture.domain.exception;

// Recurso que no existe -> 404
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

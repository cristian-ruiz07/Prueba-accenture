package com.Prueba.Accenture.domain.model;

import com.Prueba.Accenture.domain.exception.FranquiciaException;

/**
 * Regla compartida para la descripcion opcional de franquicias, sucursales y productos.
 * Vacia o en blanco se guarda como null; no puede superar MAX_LONGITUD caracteres.
 */
final class Descripcion {

    static final int MAX_LONGITUD = 255;

    private Descripcion() {
    }

    static String normalizar(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            return null;
        }
        String limpia = descripcion.trim();
        if (limpia.length() > MAX_LONGITUD) {
            throw new FranquiciaException(
                    "La descripcion no puede superar " + MAX_LONGITUD + " caracteres");
        }
        return limpia;
    }
}

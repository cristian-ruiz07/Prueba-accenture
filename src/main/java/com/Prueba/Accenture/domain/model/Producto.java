package com.Prueba.Accenture.domain.model;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Producto {

    private final String id;
    private final String nombre;
    private final int stock;

    public static Producto crear(String nombre, int stock) {
        validarNombre(nombre);
        validarStock(stock);
        return Producto.builder()
                .id(UUID.randomUUID().toString())
                .nombre(nombre.trim())
                .stock(stock)
                .build();
    }

    public Producto actualizarStock(int nuevoStock) {
        validarStock(nuevoStock);
        return toBuilder().stock(nuevoStock).build();
    }

    public Producto actualizarNombre(String nuevoNombre) {
        validarNombre(nuevoNombre);
        return toBuilder().nombre(nuevoNombre.trim()).build();
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new FranquiciaException("El nombre del producto es obligatorio");
        }
    }

    private static void validarStock(int stock) {
        if (stock < 0) {
            throw new FranquiciaException("El stock no puede ser negativo");
        }
    }
}

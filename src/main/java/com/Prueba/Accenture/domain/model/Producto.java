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
    private final String descripcion;   // opcional
    private final int stock;

    public static Producto crear(String nombre, String descripcion, int stock) {
        validarNombre(nombre);
        validarStock(stock);
        return Producto.builder()
                .id(UUID.randomUUID().toString())
                .nombre(nombre.trim())
                .descripcion(Descripcion.normalizar(descripcion))
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

    public Producto actualizarDescripcion(String nuevaDescripcion) {
        return toBuilder().descripcion(Descripcion.normalizar(nuevaDescripcion)).build();
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

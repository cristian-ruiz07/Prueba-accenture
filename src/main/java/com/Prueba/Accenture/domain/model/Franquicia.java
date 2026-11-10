package com.Prueba.Accenture.domain.model;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Franquicia {

    private final String id;
    private final String nombre;
    @Builder.Default
    private final List<Sucursal> sucursales = List.of();

    public static Franquicia crear(String nombre) {
        validarNombre(nombre);
        return Franquicia.builder()
                .id(UUID.randomUUID().toString())
                .nombre(nombre.trim())
                .build();
    }

    public Franquicia actualizarNombre(String nuevoNombre) {
        validarNombre(nuevoNombre);
        return toBuilder().nombre(nuevoNombre.trim()).build();
    }

    public Franquicia agregarSucursal(Sucursal sucursal) {
        boolean existe = sucursales.stream()
                .anyMatch(s -> s.getNombre().equalsIgnoreCase(sucursal.getNombre()));
        if (existe) {
            throw new FranquiciaException("Ya existe una sucursal con el nombre " + sucursal.getNombre());
        }
        List<Sucursal> nuevas = new ArrayList<>(sucursales);
        nuevas.add(sucursal);
        return toBuilder().sucursales(List.copyOf(nuevas)).build();
    }

    public Sucursal buscarSucursal(String sucursalId) {
        return sucursales.stream()
                .filter(s -> s.getId().equals(sucursalId))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada: " + sucursalId));
    }

    // Reemplaza una sucursal ya modificada (por ejemplo, despues de agregarle un producto)
    public Franquicia actualizarSucursal(Sucursal actualizada) {
        buscarSucursal(actualizada.getId());
        List<Sucursal> nuevas = sucursales.stream()
                .map(s -> s.getId().equals(actualizada.getId()) ? actualizada : s)
                .toList();
        return toBuilder().sucursales(nuevas).build();
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new FranquiciaException("El nombre de la franquicia es obligatorio");
        }
    }
}

package com.Prueba.Accenture.domain.model;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Sucursal {

    private final String id;
    private final String nombre;
    private final String descripcion;   // opcional
    @Builder.Default
    private final List<Producto> productos = List.of();

    public static Sucursal crear(String nombre, String descripcion) {
        validarNombre(nombre);
        return Sucursal.builder()
                .id(UUID.randomUUID().toString())
                .nombre(nombre.trim())
                .descripcion(Descripcion.normalizar(descripcion))
                .build();
    }

    public Sucursal actualizarNombre(String nuevoNombre) {
        validarNombre(nuevoNombre);
        return toBuilder().nombre(nuevoNombre.trim()).build();
    }

    public Sucursal actualizarDescripcion(String nuevaDescripcion) {
        return toBuilder().descripcion(Descripcion.normalizar(nuevaDescripcion)).build();
    }

    public Sucursal agregarProducto(Producto producto) {
        boolean existe = productos.stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase(producto.getNombre()));
        if (existe) {
            throw new FranquiciaException("Ya existe un producto con el nombre " + producto.getNombre());
        }
        List<Producto> nuevos = new ArrayList<>(productos);
        nuevos.add(producto);
        return toBuilder().productos(List.copyOf(nuevos)).build();
    }

    public Producto buscarProducto(String productoId) {
        return productos.stream()
                .filter(p -> p.getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + productoId));
    }

    public Sucursal eliminarProducto(String productoId) {
        buscarProducto(productoId);
        List<Producto> restantes = productos.stream()
                .filter(p -> !p.getId().equals(productoId))
                .toList();
        return toBuilder().productos(restantes).build();
    }

    public Sucursal actualizarProducto(Producto actualizado) {
        buscarProducto(actualizado.getId());
        List<Producto> nuevos = productos.stream()
                .map(p -> p.getId().equals(actualizado.getId()) ? actualizado : p)
                .toList();
        return toBuilder().productos(nuevos).build();
    }

    // Base del criterio 7: vacio si la sucursal no tiene productos
    public Optional<Producto> productoConMasStock() {
        return productos.stream()
                .max(Comparator.comparingInt(Producto::getStock));
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new FranquiciaException("El nombre de la sucursal es obligatorio");
        }
    }
}

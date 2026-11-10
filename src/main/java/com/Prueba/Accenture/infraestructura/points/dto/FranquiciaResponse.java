package com.Prueba.Accenture.infraestructura.points.dto;

import com.Prueba.Accenture.domain.model.Franquicia;

import java.util.List;

public record FranquiciaResponse(String id, String nombre, List<SucursalResponse> sucursales) {

    public record SucursalResponse(String id, String nombre, List<ProductoResponse> productos) {
    }

    public record ProductoResponse(String id, String nombre, int stock) {
    }

    public static FranquiciaResponse de(Franquicia f) {
        return new FranquiciaResponse(f.getId(), f.getNombre(),
                f.getSucursales().stream()
                        .map(s -> new SucursalResponse(s.getId(), s.getNombre(),
                                s.getProductos().stream()
                                        .map(p -> new ProductoResponse(p.getId(), p.getNombre(), p.getStock()))
                                        .toList()))
                        .toList());
    }
}

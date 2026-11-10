package com.Prueba.Accenture.infraestructura.points.dto;

import com.Prueba.Accenture.domain.model.Franquicia;
import com.Prueba.Accenture.domain.model.Producto;
import com.Prueba.Accenture.domain.model.Sucursal;

import java.util.List;

public record FranquiciaResponse(String id, String nombre, String descripcion, List<SucursalResponse> sucursales) {

    public static FranquiciaResponse de(Franquicia f) {
        return new FranquiciaResponse(f.getId(), f.getNombre(), f.getDescripcion(),
                f.getSucursales().stream().map(SucursalResponse::de).toList());
    }

    public record SucursalResponse(String id, String nombre, String descripcion, List<ProductoResponse> productos) {

        public static SucursalResponse de(Sucursal s) {
            return new SucursalResponse(s.getId(), s.getNombre(), s.getDescripcion(),
                    s.getProductos().stream().map(ProductoResponse::de).toList());
        }
    }

    public record ProductoResponse(String id, String nombre, String descripcion, int stock) {

        public static ProductoResponse de(Producto p) {
            return new ProductoResponse(p.getId(), p.getNombre(), p.getDescripcion(), p.getStock());
        }
    }
}

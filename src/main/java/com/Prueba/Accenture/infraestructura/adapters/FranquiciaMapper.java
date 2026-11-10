package com.Prueba.Accenture.infraestructura.adapters;

import com.Prueba.Accenture.domain.model.Franquicia;
import com.Prueba.Accenture.domain.model.Producto;
import com.Prueba.Accenture.domain.model.Sucursal;

import java.util.List;

public final class FranquiciaMapper {

    private FranquiciaMapper() {
    }

    public static FranquiciaDocument aDocumento(Franquicia f) {
        List<SucursalDocument> sucursales = f.getSucursales().stream()
                .map(s -> new SucursalDocument(s.getId(), s.getNombre(),
                        s.getProductos().stream()
                                .map(p -> new ProductoDocument(p.getId(), p.getNombre(), p.getStock()))
                                .toList()))
                .toList();
        return new FranquiciaDocument(f.getId(), f.getNombre(), sucursales);
    }

    public static Franquicia aDominio(FranquiciaDocument d) {
        List<Sucursal> sucursales = listaSegura(d.getSucursales()).stream()
                .map(s -> Sucursal.builder()
                        .id(s.getId())
                        .nombre(s.getNombre())
                        .productos(listaSegura(s.getProductos()).stream()
                                .map(p -> Producto.builder()
                                        .id(p.getId())
                                        .nombre(p.getNombre())
                                        .stock(p.getStock())
                                        .build())
                                .toList())
                        .build())
                .toList();
        return Franquicia.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .sucursales(sucursales)
                .build();
    }

    // Mongo puede devolver null si el campo no existe en el documento
    private static <T> List<T> listaSegura(List<T> lista) {
        return lista == null ? List.of() : lista;
    }
}

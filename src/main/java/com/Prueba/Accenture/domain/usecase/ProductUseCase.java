package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import com.Prueba.Accenture.domain.model.Producto;
import com.Prueba.Accenture.domain.model.Sucursal;
import com.Prueba.Accenture.domain.model.TopStockProduct;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@RequiredArgsConstructor
public class ProductUseCase {

    private final FranquiciaRepositorio repositorio;

    public Mono<Franquicia> agregar(String franquiciaId, String sucursalId,
                                    String nombre, String descripcion, int stock) {
        return modificarSucursal(franquiciaId, sucursalId,
                sucursal -> sucursal.agregarProducto(Producto.crear(nombre, descripcion, stock)));
    }

    public Mono<Franquicia> eliminar(String franquiciaId, String sucursalId, String productoId) {
        return modificarSucursal(franquiciaId, sucursalId,
                sucursal -> sucursal.eliminarProducto(productoId));
    }

    public Mono<Franquicia> actualizarStock(String franquiciaId, String sucursalId,
                                            String productoId, int nuevoStock) {
        return modificarSucursal(franquiciaId, sucursalId,
                sucursal -> sucursal.actualizarProducto(
                        sucursal.buscarProducto(productoId).actualizarStock(nuevoStock)));
    }

    public Mono<Franquicia> actualizarNombre(String franquiciaId, String sucursalId,
                                             String productoId, String nuevoNombre) {
        return modificarSucursal(franquiciaId, sucursalId,
                sucursal -> sucursal.actualizarProducto(
                        sucursal.buscarProducto(productoId).actualizarNombre(nuevoNombre)));
    }

    public Flux<TopStockProduct> productosConMasStock(String franquiciaId) {
        return buscarFranquicia(franquiciaId)
                .flatMapMany(franquicia -> Flux.fromIterable(franquicia.getSucursales()))
                .concatMap(sucursal -> Mono.justOrEmpty(sucursal.productoConMasStock())
                        .map(producto -> TopStockProduct.de(sucursal, producto)));
    }

    private Mono<Franquicia> modificarSucursal(String franquiciaId, String sucursalId,
                                               UnaryOperator<Sucursal> cambio) {
        return buscarFranquicia(franquiciaId)
                .map(franquicia -> franquicia.actualizarSucursal(
                        cambio.apply(franquicia.buscarSucursal(sucursalId))))
                .flatMap(repositorio::guardar);
    }

    private Mono<Franquicia> buscarFranquicia(String franquiciaId) {
        return repositorio.buscarPorId(franquiciaId)
                .switchIfEmpty(Mono.error(
                        new RecursoNoEncontradoException("Franquicia no encontrada: " + franquiciaId)));
    }

}

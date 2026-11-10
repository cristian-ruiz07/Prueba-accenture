package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import com.Prueba.Accenture.domain.model.Sucursal;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SucursalUseCase {

    private final FranquiciaRepositorio repositorio;

    public Mono<Franquicia> agregar(String franquiciaId, String nombre, String descripcion) {
        return buscarFranquicia(franquiciaId)
                .map(franquicia -> franquicia.agregarSucursal(Sucursal.crear(nombre, descripcion)))
                .flatMap(repositorio::guardar);
    }

    public Mono<Franquicia> actualizarNombre(String franquiciaId, String sucursalId, String nuevoNombre) {
        return buscarFranquicia(franquiciaId)
                .map(franquicia -> {
                    Sucursal actualizada = franquicia.buscarSucursal(sucursalId)
                            .actualizarNombre(nuevoNombre);
                    return franquicia.actualizarSucursal(actualizada);
                })
                .flatMap(repositorio::guardar);
    }

    private Mono<Franquicia> buscarFranquicia(String franquiciaId) {
        return repositorio.buscarPorId(franquiciaId)
                .switchIfEmpty(Mono.error(
                        new RecursoNoEncontradoException("Franquicia no encontrada: " + franquiciaId)));
    }

    public Flux<Sucursal> listar(String franquiciaId) {
        return buscarFranquicia(franquiciaId)                        // Mono<Franquicia>
                .flatMapMany(f -> Flux.fromIterable(f.getSucursales())); // Flux<Sucursal>
    }
}

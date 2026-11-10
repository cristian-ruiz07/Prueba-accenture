package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranquiciaUseCase {

    private final FranquiciaRepositorio repositorio;

    public Mono<Franquicia> crear(String nombre, String descripcion) {
        return Mono.fromCallable(() -> Franquicia.crear(nombre, descripcion))
                .flatMap(repositorio::guardar);
    }

    public Mono<Franquicia> obtener(String franquiciaId) {
        return buscarFranquicia(franquiciaId);
    }

    public Mono<Franquicia> actualizarNombre(String franquiciaId, String nuevoNombre) {
        return buscarFranquicia(franquiciaId)
                .map(franquicia -> franquicia.actualizarNombre(nuevoNombre))
                .flatMap(repositorio::guardar);
    }

    private Mono<Franquicia> buscarFranquicia(String franquiciaId) {
        return repositorio.buscarPorId(franquiciaId)
                .switchIfEmpty(Mono.error(
                        new RecursoNoEncontradoException("Franquicia no encontrada: " + franquiciaId)));
    }

    public Flux<Franquicia> listar(){
        return repositorio.buscarTodos();
    }

}

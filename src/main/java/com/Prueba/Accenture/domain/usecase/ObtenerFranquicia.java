package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ObtenerFranquicia {

    private final FranquiciaRepositorio repositorio;

    public Mono<Franquicia> ejecutar(String id) {
        return repositorio.buscarPorId(id)
                .switchIfEmpty(Mono.error(
                        new RecursoNoEncontradoException("Franquicia no encontrada: " + id)));
    }
}

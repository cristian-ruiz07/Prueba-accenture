package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CrearFranquicia {

    private final FranquiciaRepositorio repositorio;

    public Mono<Franquicia> ejecutar(String nombre, String descripcion) {
        return Mono.fromCallable(() -> Franquicia.crear(nombre, descripcion))
                .flatMap(repositorio::guardar);
    }
}

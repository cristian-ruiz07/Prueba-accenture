package com.Prueba.Accenture.domain.interfaces;

import com.Prueba.Accenture.domain.model.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaRepositorio {

    Mono<Franquicia> guardar(Franquicia franquicia);

    Mono<Franquicia> buscarPorId(String id);
}

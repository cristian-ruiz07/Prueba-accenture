package com.Prueba.Accenture.infraestructura.adapters;

import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FranquiciaRepositorioAdapter implements FranquiciaRepositorio {

    private final FranquiciaMongo mongo;

    @Override
    public Mono<Franquicia> guardar(Franquicia franquicia) {
        return mongo.save(FranquiciaMapper.aDocumento(franquicia))
                .map(FranquiciaMapper::aDominio);
    }

    @Override
    public Mono<Franquicia> buscarPorId(String id) {
        return mongo.findById(id)
                .map(FranquiciaMapper::aDominio);
    }

    @Override
    public Flux<Franquicia> buscarTodos() {
        return mongo.findAll().map(FranquiciaMapper::aDominio);
    }

}

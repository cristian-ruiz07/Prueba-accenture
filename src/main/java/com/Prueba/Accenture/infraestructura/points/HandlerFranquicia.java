package com.Prueba.Accenture.infraestructura.points;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.usecase.CrearFranquicia;
import com.Prueba.Accenture.domain.usecase.ObtenerFranquicia;
import com.Prueba.Accenture.infraestructura.points.dto.CrearFranquiciaRequest;
import com.Prueba.Accenture.infraestructura.points.dto.FranquiciaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class HandlerFranquicia {

    private final CrearFranquicia crearFranquicia;
    private final ObtenerFranquicia obtenerFranquicia;

    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(CrearFranquiciaRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException("El cuerpo de la peticion es obligatorio")))
                .flatMap(body -> crearFranquicia.ejecutar(body.nombre()))
                .flatMap(f -> ServerResponse
                        .created(URI.create("/api/franquicias/" + f.getId()))
                        .bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> obtener(ServerRequest request) {
        return obtenerFranquicia.ejecutar(request.pathVariable("id"))
                .flatMap(f -> ServerResponse.ok().bodyValue(FranquiciaResponse.de(f)));
    }
}

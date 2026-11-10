package com.Prueba.Accenture.infraestructura.points;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.usecase.FranquiciaUseCase;
import com.Prueba.Accenture.infraestructura.points.dto.ActualizarNombreRequest;
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

    private static final String CUERPO_OBLIGATORIO = "El cuerpo de la peticion es obligatorio";

    private final FranquiciaUseCase franquiciaUseCase;

    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(CrearFranquiciaRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException(CUERPO_OBLIGATORIO)))
                .flatMap(body -> franquiciaUseCase.crear(body.nombre(), body.descripcion()))
                .flatMap(f -> ServerResponse
                        .created(URI.create("/api/franquicias/" + f.getId()))
                        .bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> obtener(ServerRequest request) {
        return franquiciaUseCase.obtener(request.pathVariable("id"))
                .flatMap(f -> ServerResponse.ok().bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> actualizarNombre(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(ActualizarNombreRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException(CUERPO_OBLIGATORIO)))
                .flatMap(body -> franquiciaUseCase.actualizarNombre(id, body.nombre()))
                .flatMap(f -> ServerResponse.ok().bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> listar (ServerRequest request){
        return ServerResponse.ok().body(franquiciaUseCase.listar()
                .map(FranquiciaResponse::de),FranquiciaResponse.class);
    }
}

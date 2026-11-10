package com.Prueba.Accenture.infraestructura.points;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.usecase.ActualizarNombreFranquicia;
import com.Prueba.Accenture.domain.usecase.CrearFranquicia;
import com.Prueba.Accenture.domain.usecase.ObtenerFranquicia;
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

    private final CrearFranquicia crearFranquicia;
    private final ObtenerFranquicia obtenerFranquicia;
    private final ActualizarNombreFranquicia actualizarNombreFranquicia;

    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(CrearFranquiciaRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException(CUERPO_OBLIGATORIO)))
                .flatMap(body -> crearFranquicia.ejecutar(body.nombre(), body.descripcion()))
                .flatMap(f -> ServerResponse
                        .created(URI.create("/api/franquicias/" + f.getId()))
                        .bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> obtener(ServerRequest request) {
        return obtenerFranquicia.ejecutar(request.pathVariable("id"))
                .flatMap(f -> ServerResponse.ok().bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> actualizarNombre(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(ActualizarNombreRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException(CUERPO_OBLIGATORIO)))
                .flatMap(body -> actualizarNombreFranquicia.ejecutar(id, body.nombre()))
                .flatMap(f -> ServerResponse.ok().bodyValue(FranquiciaResponse.de(f)));
    }
}

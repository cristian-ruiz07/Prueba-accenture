package com.Prueba.Accenture.infraestructura.points;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class ManejoErrores implements HandlerFilterFunction<ServerResponse,ServerResponse> {

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        return next.handle(request)
                .onErrorResume(RecursoNoEncontradoException.class,
                        e -> responder(HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorResume(FranquiciaException.class,
                        e -> responder(HttpStatus.BAD_REQUEST, e.getMessage()))
                .onErrorResume(ResponseStatusException.class,   // JSON mal formado, tipo de contenido inválido, etc.
                        e -> responder(e.getStatusCode(), e.getReason() != null ? e.getReason() : "Peticion invalida"))
                .onErrorResume(e -> {
                    log.error("Error inesperado en {} {}", request.method(), request.path(), e);
                    return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
                });
    }

    private Mono<ServerResponse> responder(HttpStatusCode status, String mensaje) {
        return ServerResponse.status(status).bodyValue(Map.of("error", mensaje));
    }
}

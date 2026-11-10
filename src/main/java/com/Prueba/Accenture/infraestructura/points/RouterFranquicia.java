package com.Prueba.Accenture.infraestructura.points;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

@Configuration
public class RouterFranquicia {

    @Bean
    public RouterFunction<ServerResponse> rutasFranquicia(HandlerFranquicia handler) {
        return RouterFunctions.route()
                .POST("/api/franquicias", handler::crear)
                .GET("/api/franquicias/{id}", handler::obtener)
                .onError(RecursoNoEncontradoException.class, (e, req) ->
                        ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(Map.of("error", e.getMessage())))
                .onError(FranquiciaException.class, (e, req) ->
                        ServerResponse.badRequest().bodyValue(Map.of("error", e.getMessage())))
                .build();
    }
}

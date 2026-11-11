package com.Prueba.Accenture.infraestructura.points.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

@Configuration
public class RouterInicio {

    @Bean
    public RouterFunction<ServerResponse> rutaInicio() {
        return RouterFunctions.route()
                .GET("/", request -> ServerResponse.ok().bodyValue(Map.of(
                        "api", "API de Franquicias",
                        "estado", "activa",
                        "documentacion", "https://github.com/cristian-ruiz07/Prueba-accenture",
                        "endpoints", "/api/franquicias")))
                .build();
    }
}

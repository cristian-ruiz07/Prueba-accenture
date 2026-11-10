package com.Prueba.Accenture.infraestructura.points.routers;

import com.Prueba.Accenture.infraestructura.points.ManejoErrores;
import com.Prueba.Accenture.infraestructura.points.handler.HandlerFranquicia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterFranquicia {

    private static final String BASE = "/api/franquicias";

    @Bean
    public RouterFunction<ServerResponse> rutasFranquicia(HandlerFranquicia handler,
                                                          ManejoErrores manejoErrores) {
        return RouterFunctions.route()
                .POST(BASE, handler::crear)
                .GET(BASE, handler::listar)
                .GET(BASE + "/{id}", handler::obtener)
                .PATCH(BASE + "/{id}/nombre", handler::actualizarNombre)
                .filter(manejoErrores)
                .build();
    }
}

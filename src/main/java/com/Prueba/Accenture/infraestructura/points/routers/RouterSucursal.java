package com.Prueba.Accenture.infraestructura.points.routers;

import com.Prueba.Accenture.infraestructura.points.handler.HandlerSucursal;
import com.Prueba.Accenture.infraestructura.points.ManejoErrores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterSucursal {

    private static final String BASE = "/api/franquicias/{franquiciaId}/sucursales";

    @Bean
    public RouterFunction<ServerResponse> rutasSucursal(HandlerSucursal handler,
                                                        ManejoErrores manejadorErrores) {
        return RouterFunctions.route()
                .POST(BASE, handler::agregar)
                .GET(BASE, handler::listar)
                .PATCH(BASE + "/{sucursalId}/nombre", handler::actualizarNombre)
                .filter(manejadorErrores)
                .build();
    }

}

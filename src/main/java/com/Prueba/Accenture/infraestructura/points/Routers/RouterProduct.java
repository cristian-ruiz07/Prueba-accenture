package com.Prueba.Accenture.infraestructura.points.Routers;

import com.Prueba.Accenture.infraestructura.points.ManejoErrores;
import com.Prueba.Accenture.infraestructura.points.handler.HandlerProduct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterProduct {
    private static final String BASE = "/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos";
    private static final String PRODUCTO = BASE + "/{productoId}";

    @Bean
    public RouterFunction<ServerResponse> rutasProducto(HandlerProduct handler,
                                                        ManejoErrores manejoErrores) {
        return RouterFunctions.route()
                .POST(BASE, handler::agregar)
                .DELETE(PRODUCTO, handler::eliminar)
                .PATCH(PRODUCTO + "/stock", handler::actualizarStock)
                .PATCH(PRODUCTO + "/nombre", handler::actualizarNombre)
                // Criterio 7
                .GET("/api/franquicias/{franquiciaId}/productos/top-stock", handler::productosConMasStock)
                .filter(manejoErrores)
                .build();
    }
}

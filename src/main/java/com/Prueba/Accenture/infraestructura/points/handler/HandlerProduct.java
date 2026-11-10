package com.Prueba.Accenture.infraestructura.points.handler;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.model.Franquicia;
import com.Prueba.Accenture.domain.usecase.ProductUseCase;
import com.Prueba.Accenture.infraestructura.points.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerProduct {
    private static final String CUERPO_OBLIGATORIO = "El cuerpo de la peticion es obligatorio";
    private static final String STOCK_OBLIGATORIO = "El stock es obligatorio";

    private final ProductUseCase productUseCase;

    public Mono<ServerResponse> agregar(ServerRequest request){
        String franquiciaId = request.pathVariable("franquiciaId");
        String sucursalId = request.pathVariable("sucursalId");
        return request.bodyToMono(CrearProductRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException(CUERPO_OBLIGATORIO)))
                .flatMap(body -> body.stock() == null
                        ? Mono.<Franquicia>error(new FranquiciaException(STOCK_OBLIGATORIO))
                        : productUseCase.agregar(franquiciaId, sucursalId,
                        body.nombre(), body.descripcion(), body.stock()))
                .flatMap(f -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> eliminar(ServerRequest request) {
        return productUseCase.eliminar(
                        request.pathVariable("franquiciaId"),
                        request.pathVariable("sucursalId"),
                        request.pathVariable("productoId"))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> actualizarStock(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        String sucursalId = request.pathVariable("sucursalId");
        String productoId = request.pathVariable("productoId");
        return request.bodyToMono(ActualizarStockRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException(CUERPO_OBLIGATORIO)))
                .flatMap(body -> body.stock() == null
                        ? Mono.<Franquicia>error(new FranquiciaException(STOCK_OBLIGATORIO))
                        : productUseCase.actualizarStock(franquiciaId, sucursalId, productoId, body.stock()))
                .flatMap(f -> ServerResponse.ok().bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> actualizarNombre(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        String sucursalId = request.pathVariable("sucursalId");
        String productoId = request.pathVariable("productoId");
        return request.bodyToMono(ActualizarNombreRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException(CUERPO_OBLIGATORIO)))
                .flatMap(body -> productUseCase.actualizarNombre(franquiciaId, sucursalId, productoId, body.nombre()))
                .flatMap(f -> ServerResponse.ok().bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> productosConMasStock(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        return productUseCase.productosConMasStock(franquiciaId)
                .map(ProductTopStockResponse::de)
                .collectList()
                .flatMap(lista -> ServerResponse.ok().bodyValue(lista));
    }
}

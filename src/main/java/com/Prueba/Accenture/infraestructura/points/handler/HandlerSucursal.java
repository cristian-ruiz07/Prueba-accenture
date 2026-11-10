package com.Prueba.Accenture.infraestructura.points.handler;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.usecase.SucursalUseCase;
import com.Prueba.Accenture.infraestructura.points.dto.ActualizarNombreRequest;
import com.Prueba.Accenture.infraestructura.points.dto.CrearSucursalRequest;
import com.Prueba.Accenture.infraestructura.points.dto.FranquiciaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerSucursal {

    private static final String CUERPO_OBLIGATORIO = "El cuerpo de la peticion es obligatorio";

    private final SucursalUseCase sucursalUseCase;

    public Mono<ServerResponse> agregar(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        return request.bodyToMono(CrearSucursalRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException(CUERPO_OBLIGATORIO)))
                .flatMap(body -> sucursalUseCase.agregar(franquiciaId, body.nombre(), body.descripcion()))
                .flatMap(f -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> actualizarNombre(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        String sucursalId = request.pathVariable("sucursalId");
        return request.bodyToMono(ActualizarNombreRequest.class)
                .switchIfEmpty(Mono.error(new FranquiciaException(CUERPO_OBLIGATORIO)))
                .flatMap(body -> sucursalUseCase.actualizarNombre(franquiciaId, sucursalId, body.nombre()))
                .flatMap(f -> ServerResponse.ok().bodyValue(FranquiciaResponse.de(f)));
    }

    public Mono<ServerResponse> listar(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        return ServerResponse.ok()
                .body(sucursalUseCase.listar(franquiciaId).map(FranquiciaResponse.SucursalResponse::de), FranquiciaResponse.SucursalResponse.class);
    }

}

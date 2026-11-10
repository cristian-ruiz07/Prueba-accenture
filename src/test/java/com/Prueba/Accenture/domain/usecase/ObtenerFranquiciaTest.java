package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerFranquiciaTest {

    @Mock
    private FranquiciaRepositorio repositorio;

    @InjectMocks
    private ObtenerFranquicia useCase;

    @Test
    void debeRetornarLaFranquiciaSiExiste() {
        Franquicia franquicia = Franquicia.crear("Frisby");
        when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));

        StepVerifier.create(useCase.ejecutar(franquicia.getId()))
                .expectNext(franquicia)
                .verifyComplete();
    }

    @Test
    void debeFallarSiLaFranquiciaNoExiste() {
        when(repositorio.buscarPorId("no-existe")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.ejecutar("no-existe"))
                .expectError(RecursoNoEncontradoException.class)
                .verify();
    }
}

package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActualizarNombreFranquiciaTest {

    @Mock
    private FranquiciaRepositorio repositorio;

    @InjectMocks
    private ActualizarNombreFranquicia useCase;

    @Test
    void debeActualizarElNombreSinPerderLosDemasDatos() {
        Franquicia original = Franquicia.crear("Frisby", "Pollo apanado");
        when(repositorio.buscarPorId(original.getId())).thenReturn(Mono.just(original));
        when(repositorio.guardar(any(Franquicia.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.ejecutar(original.getId(), "Frisby Colombia"))
                .assertNext(f -> {
                    assertEquals(original.getId(), f.getId());
                    assertEquals("Frisby Colombia", f.getNombre());
                    assertEquals("Pollo apanado", f.getDescripcion());
                })
                .verifyComplete();
    }

    @Test
    void debeFallarSiLaFranquiciaNoExiste() {
        when(repositorio.buscarPorId("no-existe")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.ejecutar("no-existe", "Nuevo nombre"))
                .expectError(RecursoNoEncontradoException.class)
                .verify();

        verify(repositorio, never()).guardar(any());
    }

    @Test
    void debeFallarSiElNuevoNombreEstaVacio() {
        Franquicia original = Franquicia.crear("Frisby", null);
        when(repositorio.buscarPorId(original.getId())).thenReturn(Mono.just(original));

        StepVerifier.create(useCase.ejecutar(original.getId(), " "))
                .expectError(FranquiciaException.class)
                .verify();

        verify(repositorio, never()).guardar(any());
    }
}

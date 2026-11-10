package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearFranquiciaTest {

    @Mock
    private FranquiciaRepositorio repositorio;

    @InjectMocks
    private CrearFranquicia useCase;

    @Test
    void debeCrearFranquiciaConNombreValido() {
        // El mock devuelve lo mismo que recibe, como haria la base de datos
        when(repositorio.guardar(any(Franquicia.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.ejecutar("Frisby"))
                .assertNext(f -> {
                    assertNotNull(f.getId());
                    assertEquals("Frisby", f.getNombre());
                    assertTrue(f.getSucursales().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void debeFallarSiElNombreEstaVacio() {
        StepVerifier.create(useCase.ejecutar("   "))
                .expectError(FranquiciaException.class)
                .verify();

        verify(repositorio, never()).guardar(any());
    }
}

package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranquiciaUseCaseTest {

    @Mock
    private FranquiciaRepositorio repositorio;

    private FranquiciaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FranquiciaUseCase(repositorio);
    }

    private void guardar() {
        when(repositorio.guardar(any(Franquicia.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
    }

    @Nested
    class Crear {

        @Test
        void debeCrearFranquiciaConNombreYDescripcion() {
            guardar();

            StepVerifier.create(useCase.crear("Frisby", "Pollo apanado"))
                    .assertNext(f -> {
                        assertNotNull(f.getId());
                        assertEquals("Frisby", f.getNombre());
                        assertEquals("Pollo apanado", f.getDescripcion());
                        assertTrue(f.getSucursales().isEmpty());
                    })
                    .verifyComplete();
        }

        @Test
        void debeGuardarDescripcionVaciaComoNull() {
            guardar();

            StepVerifier.create(useCase.crear("Frisby", "   "))
                    .assertNext(f -> assertNull(f.getDescripcion()))
                    .verifyComplete();
        }

        @Test
        void debeFallarSiElNombreEstaVacio() {
            StepVerifier.create(useCase.crear("   ", null))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }

        @Test
        void debeFallarSiLaDescripcionEsDemasiadoLarga() {
            StepVerifier.create(useCase.crear("Frisby", "a".repeat(256)))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }
    }

    @Nested
    class Obtener {

        @Test
        void debeRetornarLaFranquiciaSiExiste() {
            Franquicia franquicia = Franquicia.crear("Frisby", null);
            when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));

            StepVerifier.create(useCase.obtener(franquicia.getId()))
                    .expectNext(franquicia)
                    .verifyComplete();
        }

        @Test
        void debeFallarSiLaFranquiciaNoExiste() {
            when(repositorio.buscarPorId("no-existe")).thenReturn(Mono.empty());

            StepVerifier.create(useCase.obtener("no-existe"))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();
        }
    }

    @Nested
    class ActualizarNombre {

        @Test
        void debeActualizarElNombreSinPerderLosDemasDatos() {
            Franquicia original = Franquicia.crear("Frisby", "Pollo apanado");
            when(repositorio.buscarPorId(original.getId())).thenReturn(Mono.just(original));
            guardar();

            StepVerifier.create(useCase.actualizarNombre(original.getId(), "Frisby Colombia"))
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

            StepVerifier.create(useCase.actualizarNombre("no-existe", "Nuevo nombre"))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }

        @Test
        void debeFallarSiElNuevoNombreEstaVacio() {
            Franquicia original = Franquicia.crear("Frisby", null);
            when(repositorio.buscarPorId(original.getId())).thenReturn(Mono.just(original));

            StepVerifier.create(useCase.actualizarNombre(original.getId(), " "))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }
    }

    @Nested
    class Listar {

        @Test
        void debeRetornarTodasLasFranquicias() {
            Franquicia frisby = Franquicia.crear("Frisby", null);
            Franquicia kokoriko = Franquicia.crear("Kokoriko", null);
            when(repositorio.buscarTodos()).thenReturn(Flux.just(frisby, kokoriko));

            StepVerifier.create(useCase.listar())
                    .expectNext(frisby)
                    .expectNext(kokoriko)
                    .verifyComplete();
        }

        @Test
        void debeRetornarVacioSiNoHayFranquicias() {
            when(repositorio.buscarTodos()).thenReturn(Flux.empty());

            StepVerifier.create(useCase.listar())
                    .verifyComplete();
        }
    }

}

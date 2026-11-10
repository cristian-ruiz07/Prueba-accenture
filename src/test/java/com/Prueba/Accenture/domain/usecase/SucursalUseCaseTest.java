package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import com.Prueba.Accenture.domain.model.Sucursal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class SucursalUseCaseTest {

    @Mock
    private FranquiciaRepositorio repositorio;

    private SucursalUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SucursalUseCase(repositorio);
    }

    private void guardarDevuelveLoRecibido() {
        when(repositorio.guardar(any(Franquicia.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
    }

    @Nested
    class Agregar {

        @Test
        void debeAgregarLaSucursalALaFranquicia() {
            Franquicia franquicia = Franquicia.crear("Frisby", null);
            when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));
            guardarDevuelveLoRecibido();

            StepVerifier.create(useCase.agregar(franquicia.getId(), "Sucursal Centro", "Calle 10"))
                    .assertNext(f -> {
                        assertEquals(1, f.getSucursales().size());
                        Sucursal s = f.getSucursales().get(0);
                        assertNotNull(s.getId());
                        assertEquals("Sucursal Centro", s.getNombre());
                        assertEquals("Calle 10", s.getDescripcion());
                        assertTrue(s.getProductos().isEmpty());
                    })
                    .verifyComplete();
        }

        @Test
        void debeFallarSiLaFranquiciaNoExiste() {
            when(repositorio.buscarPorId("no-existe")).thenReturn(Mono.empty());

            StepVerifier.create(useCase.agregar("no-existe", "Sucursal Centro", null))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }

        @Test
        void debeFallarSiYaExisteUnaSucursalConElMismoNombre() {
            Franquicia franquicia = Franquicia.crear("Frisby", null)
                    .agregarSucursal(Sucursal.crear("Sucursal Centro", null));
            when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));

            // En minusculas: la comparacion ignora mayusculas
            StepVerifier.create(useCase.agregar(franquicia.getId(), "sucursal centro", null))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }

        @Test
        void debeFallarSiElNombreEstaVacio() {
            Franquicia franquicia = Franquicia.crear("Frisby", null);
            when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));

            StepVerifier.create(useCase.agregar(franquicia.getId(), "  ", null))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }
    }

    @Nested
    class ActualizarNombre {

        @Test
        void debeActualizarElNombreSinPerderLosDemasDatos() {
            Sucursal sucursal = Sucursal.crear("Sucursal Centro", "Calle 10");
            Franquicia franquicia = Franquicia.crear("Frisby", null).agregarSucursal(sucursal);
            when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));
            guardarDevuelveLoRecibido();

            StepVerifier.create(useCase.actualizarNombre(franquicia.getId(), sucursal.getId(), "Sucursal Norte"))
                    .assertNext(f -> {
                        Sucursal actualizada = f.buscarSucursal(sucursal.getId());
                        assertEquals("Sucursal Norte", actualizada.getNombre());
                        assertEquals("Calle 10", actualizada.getDescripcion());
                    })
                    .verifyComplete();
        }

        @Test
        void debeFallarSiLaSucursalNoExiste() {
            Franquicia franquicia = Franquicia.crear("Frisby", null);
            when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));

            StepVerifier.create(useCase.actualizarNombre(franquicia.getId(), "no-existe", "Sucursal Norte"))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }

        @Test
        void debeFallarSiElNuevoNombreEstaVacio() {
            Sucursal sucursal = Sucursal.crear("Sucursal Centro", null);
            Franquicia franquicia = Franquicia.crear("Frisby", null).agregarSucursal(sucursal);
            when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));

            StepVerifier.create(useCase.actualizarNombre(franquicia.getId(), sucursal.getId(), ""))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }
    }

    @Nested
    class Listar {

        @Test
        void debeRetornarLasSucursalesDeLaFranquicia() {
            Sucursal centro = Sucursal.crear("Centro", null);
            Sucursal norte = Sucursal.crear("Norte", null);
            Franquicia franquicia = Franquicia.crear("Frisby", null)
                    .agregarSucursal(centro)
                    .agregarSucursal(norte);
            when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));

            StepVerifier.create(useCase.listar(franquicia.getId()))
                    .expectNext(centro)
                    .expectNext(norte)
                    .verifyComplete();
        }

        @Test
        void debeRetornarVacioSiNoTieneSucursales() {
            Franquicia franquicia = Franquicia.crear("Frisby", null);
            when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));

            StepVerifier.create(useCase.listar(franquicia.getId()))
                    .verifyComplete();
        }

        @Test
        void debeFallarSiLaFranquiciaNoExiste() {
            when(repositorio.buscarPorId("no-existe")).thenReturn(Mono.empty());

            StepVerifier.create(useCase.listar("no-existe"))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();
        }
    }
}

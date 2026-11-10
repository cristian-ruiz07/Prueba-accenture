package com.Prueba.Accenture.domain.usecase;

import com.Prueba.Accenture.domain.exception.FranquiciaException;
import com.Prueba.Accenture.domain.exception.RecursoNoEncontradoException;
import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.model.Franquicia;
import com.Prueba.Accenture.domain.model.Producto;
import com.Prueba.Accenture.domain.model.Sucursal;
import com.Prueba.Accenture.domain.model.TopStockProduct;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private FranquiciaRepositorio repositorio;

    private ProductUseCase useCase;

    // Datos de prueba: franquicia con una sucursal que tiene un producto
    private Producto papas;
    private Sucursal centro;
    private Franquicia franquicia;

    @BeforeEach
    void setUp() {
        useCase = new ProductUseCase(repositorio);
        papas = Producto.crear("Papas", "Papas a la francesa", 10);
        centro = Sucursal.crear("Centro", null).agregarProducto(papas);
        franquicia = Franquicia.crear("Frisby", null).agregarSucursal(centro);
    }

    private void franquiciaExiste() {
        when(repositorio.buscarPorId(franquicia.getId())).thenReturn(Mono.just(franquicia));
    }

    private void guardarDevuelveLoRecibido() {
        when(repositorio.guardar(any(Franquicia.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
    }

    @Nested
    class Agregar {

        @Test
        void debeAgregarElProductoALaSucursal() {
            franquiciaExiste();
            guardarDevuelveLoRecibido();

            StepVerifier.create(useCase.agregar(franquicia.getId(), centro.getId(), "Gaseosa", "350 ml", 25))
                    .assertNext(f -> {
                        Sucursal sucursal = f.buscarSucursal(centro.getId());
                        assertEquals(2, sucursal.getProductos().size());
                        Producto nuevo = sucursal.getProductos().get(1);
                        assertNotNull(nuevo.getId());
                        assertEquals("Gaseosa", nuevo.getNombre());
                        assertEquals("350 ml", nuevo.getDescripcion());
                        assertEquals(25, nuevo.getStock());
                    })
                    .verifyComplete();
        }

        @Test
        void debeFallarSiElStockEsNegativo() {
            franquiciaExiste();

            StepVerifier.create(useCase.agregar(franquicia.getId(), centro.getId(), "Gaseosa", null, -1))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }

        @Test
        void debeFallarSiYaExisteUnProductoConElMismoNombre() {
            franquiciaExiste();

            StepVerifier.create(useCase.agregar(franquicia.getId(), centro.getId(), "papas", null, 5))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }

        @Test
        void debeFallarSiLaSucursalNoExiste() {
            franquiciaExiste();

            StepVerifier.create(useCase.agregar(franquicia.getId(), "no-existe", "Gaseosa", null, 5))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }

        @Test
        void debeFallarSiLaFranquiciaNoExiste() {
            when(repositorio.buscarPorId("no-existe")).thenReturn(Mono.empty());

            StepVerifier.create(useCase.agregar("no-existe", centro.getId(), "Gaseosa", null, 5))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }
    }

    @Nested
    class Eliminar {

        @Test
        void debeEliminarElProductoDeLaSucursal() {
            franquiciaExiste();
            guardarDevuelveLoRecibido();

            StepVerifier.create(useCase.eliminar(franquicia.getId(), centro.getId(), papas.getId()))
                    .assertNext(f -> assertTrue(f.buscarSucursal(centro.getId()).getProductos().isEmpty()))
                    .verifyComplete();
        }

        @Test
        void debeFallarSiElProductoNoExiste() {
            franquiciaExiste();

            StepVerifier.create(useCase.eliminar(franquicia.getId(), centro.getId(), "no-existe"))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }
    }

    @Nested
    class ActualizarStock {

        @Test
        void debeActualizarElStockSinPerderLosDemasDatos() {
            franquiciaExiste();
            guardarDevuelveLoRecibido();

            StepVerifier.create(useCase.actualizarStock(franquicia.getId(), centro.getId(), papas.getId(), 50))
                    .assertNext(f -> {
                        Producto actualizado = f.buscarSucursal(centro.getId()).buscarProducto(papas.getId());
                        assertEquals(50, actualizado.getStock());
                        assertEquals("Papas", actualizado.getNombre());
                        assertEquals("Papas a la francesa", actualizado.getDescripcion());
                    })
                    .verifyComplete();
        }

        @Test
        void debePermitirStockEnCero() {
            franquiciaExiste();
            guardarDevuelveLoRecibido();

            StepVerifier.create(useCase.actualizarStock(franquicia.getId(), centro.getId(), papas.getId(), 0))
                    .assertNext(f -> assertEquals(0,
                            f.buscarSucursal(centro.getId()).buscarProducto(papas.getId()).getStock()))
                    .verifyComplete();
        }

        @Test
        void debeFallarSiElStockEsNegativo() {
            franquiciaExiste();

            StepVerifier.create(useCase.actualizarStock(franquicia.getId(), centro.getId(), papas.getId(), -5))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }

        @Test
        void debeFallarSiElProductoNoExiste() {
            franquiciaExiste();

            StepVerifier.create(useCase.actualizarStock(franquicia.getId(), centro.getId(), "no-existe", 5))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }
    }

    @Nested
    class ActualizarNombre {

        @Test
        void debeActualizarElNombreDelProducto() {
            franquiciaExiste();
            guardarDevuelveLoRecibido();

            StepVerifier.create(useCase.actualizarNombre(franquicia.getId(), centro.getId(), papas.getId(), "Papas grandes"))
                    .assertNext(f -> {
                        Producto actualizado = f.buscarSucursal(centro.getId()).buscarProducto(papas.getId());
                        assertEquals("Papas grandes", actualizado.getNombre());
                        assertEquals(10, actualizado.getStock());
                    })
                    .verifyComplete();
        }

        @Test
        void debeFallarSiElNuevoNombreEstaVacio() {
            franquiciaExiste();

            StepVerifier.create(useCase.actualizarNombre(franquicia.getId(), centro.getId(), papas.getId(), " "))
                    .expectError(FranquiciaException.class)
                    .verify();

            verify(repositorio, never()).guardar(any());
        }
    }

    @Nested
    class ProductosConMasStock {

        @Test
        void debeRetornarElProductoConMasStockDeCadaSucursal() {
            Producto gaseosa = Producto.crear("Gaseosa", null, 30);
            Producto hamburguesa = Producto.crear("Hamburguesa", null, 5);
            Producto helado = Producto.crear("Helado", null, 12);
            Sucursal norte = Sucursal.crear("Norte", null)
                    .agregarProducto(hamburguesa)
                    .agregarProducto(helado);
            Franquicia conDosSucursales = franquicia
                    .actualizarSucursal(centro.agregarProducto(gaseosa))   // Centro: Papas 10, Gaseosa 30
                    .agregarSucursal(norte);                               // Norte: Hamburguesa 5, Helado 12
            when(repositorio.buscarPorId(conDosSucursales.getId())).thenReturn(Mono.just(conDosSucursales));

            StepVerifier.create(useCase.productosConMasStock(conDosSucursales.getId()))
                    .expectNext(TopStockProduct.de(centro, gaseosa))
                    .expectNext(TopStockProduct.de(norte, helado))
                    .verifyComplete();
        }

        @Test
        void debeOmitirLasSucursalesSinProductos() {
            Franquicia conSucursalVacia = franquicia.agregarSucursal(Sucursal.crear("Vacia", null));
            when(repositorio.buscarPorId(conSucursalVacia.getId())).thenReturn(Mono.just(conSucursalVacia));

            StepVerifier.create(useCase.productosConMasStock(conSucursalVacia.getId()))
                    .expectNext(TopStockProduct.de(centro, papas))
                    .verifyComplete();
        }

        @Test
        void debeRetornarVacioSiLaFranquiciaNoTieneSucursales() {
            Franquicia sinSucursales = Franquicia.crear("Kokoriko", null);
            when(repositorio.buscarPorId(sinSucursales.getId())).thenReturn(Mono.just(sinSucursales));

            StepVerifier.create(useCase.productosConMasStock(sinSucursales.getId()))
                    .verifyComplete();
        }

        @Test
        void debeFallarSiLaFranquiciaNoExiste() {
            when(repositorio.buscarPorId("no-existe")).thenReturn(Mono.empty());

            StepVerifier.create(useCase.productosConMasStock("no-existe"))
                    .expectError(RecursoNoEncontradoException.class)
                    .verify();
        }
    }

}

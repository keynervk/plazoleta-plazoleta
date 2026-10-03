package com.sistemas.plazoleta.domain.usecase;

import com.sistemas.plazoleta.domain.exception.PlatoNoEncontradoException;
import com.sistemas.plazoleta.domain.exception.ValidacionException;
import com.sistemas.plazoleta.domain.model.Plato;
import com.sistemas.plazoleta.domain.spi.IPlatoPersistencePort;
import com.sistemas.plazoleta.domain.spi.IRestaurantePersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatoUseCaseTest {

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @InjectMocks
    private PlatoUseCase platoUseCase;

    private Plato platoValido() {
        Plato plato = new Plato();
        plato.setNombre("Hamburguesa clasica");
        plato.setPrecio(15000);
        plato.setDescripcion("Carne, queso y vegetales");
        plato.setUrlImagen("https://img.com/hamburguesa.png");
        plato.setCategoria("Comida rapida");
        plato.setIdRestaurante(1L);
        return plato;
    }

    private Plato platoExistente() {
        Plato plato = platoValido();
        plato.setId(10L);
        plato.setActivo(true);
        return plato;
    }

    // ---------- crearPlato ----------

    @Test
    void crearPlato_conDatosValidos_quedaActivoYSeGuarda() {
        when(restaurantePersistencePort.existePorId(1L)).thenReturn(true);

        platoUseCase.crearPlato(platoValido());

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).guardarPlato(captor.capture());
        assertTrue(captor.getValue().isActivo());
    }

    @Test
    void crearPlato_conPrecioCero_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setPrecio(0);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_conPrecioNegativo_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setPrecio(-100);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_sinNombre_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setNombre(" ");

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_sinDescripcion_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setDescripcion(null);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_sinIdRestaurante_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setIdRestaurante(null);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_siElRestauranteNoExiste_lanzaValidacionException() {
        when(restaurantePersistencePort.existePorId(1L)).thenReturn(false);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(platoValido()));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_conPlatoNulo_lanzaValidacionException() {
        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(null));
    }

    // ---------- modificarPlato ----------

    @Test
    void modificarPlato_conPrecioYDescripcion_actualizaAmbos() {
        Plato existente = platoExistente();
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(existente);

        platoUseCase.modificarPlato(10L, 20000, "Nueva descripcion");

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).actualizarPlato(captor.capture());
        assertEquals(20000, captor.getValue().getPrecio());
        assertEquals("Nueva descripcion", captor.getValue().getDescripcion());
    }

    @Test
    void modificarPlato_soloConPrecio_noTocaLaDescripcion() {
        Plato existente = platoExistente();
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(existente);

        platoUseCase.modificarPlato(10L, 18000, null);

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).actualizarPlato(captor.capture());
        assertEquals(18000, captor.getValue().getPrecio());
        assertEquals("Carne, queso y vegetales", captor.getValue().getDescripcion());
    }

    @Test
    void modificarPlato_soloConDescripcion_noTocaElPrecio() {
        Plato existente = platoExistente();
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(existente);

        platoUseCase.modificarPlato(10L, null, "Solo cambia la descripcion");

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).actualizarPlato(captor.capture());
        assertEquals(15000, captor.getValue().getPrecio());
        assertEquals("Solo cambia la descripcion", captor.getValue().getDescripcion());
    }

    @Test
    void modificarPlato_siElPlatoNoExiste_lanzaPlatoNoEncontradoException() {
        when(platoPersistencePort.buscarPorId(99L)).thenReturn(null);

        assertThrows(PlatoNoEncontradoException.class,
                () -> platoUseCase.modificarPlato(99L, 20000, "Algo"));
        verify(platoPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void modificarPlato_sinPrecioNiDescripcion_lanzaValidacionException() {
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(platoExistente());

        assertThrows(ValidacionException.class,
                () -> platoUseCase.modificarPlato(10L, null, null));
        verify(platoPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void modificarPlato_conPrecioCero_lanzaValidacionException() {
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(platoExistente());

        assertThrows(ValidacionException.class,
                () -> platoUseCase.modificarPlato(10L, 0, "Algo"));
        verify(platoPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void modificarPlato_sinIdPlato_lanzaValidacionException() {
        assertThrows(ValidacionException.class,
                () -> platoUseCase.modificarPlato(null, 20000, "Algo"));
    }
}
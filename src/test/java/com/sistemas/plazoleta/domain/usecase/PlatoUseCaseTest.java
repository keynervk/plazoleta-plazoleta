package com.sistemas.plazoleta.domain.usecase;

import com.sistemas.plazoleta.domain.exception.NoAutorizadoException;
import com.sistemas.plazoleta.domain.exception.PlatoNoEncontradoException;
import com.sistemas.plazoleta.domain.exception.ValidacionException;
import com.sistemas.plazoleta.domain.model.Plato;
import com.sistemas.plazoleta.domain.model.Restaurante;
import com.sistemas.plazoleta.domain.spi.IPlatoPersistencePort;
import com.sistemas.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.sistemas.plazoleta.domain.spi.IUsuarioExternoPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatoUseCaseTest {

    private static final String CORREO_PROPIETARIO = "juan@correo.com";
    private static final Long ID_PROPIETARIO = 1L;
    private static final Long ID_OTRO_USUARIO = 2L;

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IUsuarioExternoPort usuarioExternoPort;

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

    private Restaurante restauranteDeJuan() {
        return new Restaurante(1L, "Restaurante de Juan", "900123456", "Calle 1",
                "+573000000000", "https://logo.com/x.png", ID_PROPIETARIO);
    }

    // ---------- crearPlato ----------

    @Test
    void crearPlato_conDatosValidosYPropietarioCorrecto_quedaActivoYSeGuarda() {
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo(CORREO_PROPIETARIO)).thenReturn(ID_PROPIETARIO);

        platoUseCase.crearPlato(platoValido(), CORREO_PROPIETARIO);

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).guardarPlato(captor.capture());
        assertTrue(captor.getValue().isActivo());
    }

    @Test
    void crearPlato_siElRestauranteNoLePertenece_lanzaNoAutorizadoException() {
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo("otro@correo.com")).thenReturn(ID_OTRO_USUARIO);

        assertThrows(NoAutorizadoException.class,
                () -> platoUseCase.crearPlato(platoValido(), "otro@correo.com"));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_conPrecioCero_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setPrecio(0);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato, CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_conPrecioNegativo_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setPrecio(-100);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato, CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_sinNombre_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setNombre(" ");

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato, CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_sinDescripcion_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setDescripcion(null);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato, CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_sinIdRestaurante_lanzaValidacionException() {
        Plato plato = platoValido();
        plato.setIdRestaurante(null);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(plato, CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_siElRestauranteNoExiste_lanzaValidacionException() {
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(null);

        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(platoValido(), CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void crearPlato_conPlatoNulo_lanzaValidacionException() {
        assertThrows(ValidacionException.class, () -> platoUseCase.crearPlato(null, CORREO_PROPIETARIO));
    }

    // ---------- modificarPlato ----------

    @Test
    void modificarPlato_conPropietarioCorrecto_actualizaAmbos() {
        Plato existente = platoExistente();
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(existente);
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo(CORREO_PROPIETARIO)).thenReturn(ID_PROPIETARIO);

        platoUseCase.modificarPlato(10L, 20000, "Nueva descripcion", CORREO_PROPIETARIO);

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).actualizarPlato(captor.capture());
        assertEquals(20000, captor.getValue().getPrecio());
        assertEquals("Nueva descripcion", captor.getValue().getDescripcion());
    }

    @Test
    void modificarPlato_siNoEsElPropietario_lanzaNoAutorizadoException() {
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(platoExistente());
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo("otro@correo.com")).thenReturn(ID_OTRO_USUARIO);

        assertThrows(NoAutorizadoException.class,
                () -> platoUseCase.modificarPlato(10L, 20000, "Algo", "otro@correo.com"));
        verify(platoPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void modificarPlato_soloConPrecio_noTocaLaDescripcion() {
        Plato existente = platoExistente();
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(existente);
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo(CORREO_PROPIETARIO)).thenReturn(ID_PROPIETARIO);

        platoUseCase.modificarPlato(10L, 18000, null, CORREO_PROPIETARIO);

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).actualizarPlato(captor.capture());
        assertEquals(18000, captor.getValue().getPrecio());
        assertEquals("Carne, queso y vegetales", captor.getValue().getDescripcion());
    }

    @Test
    void modificarPlato_soloConDescripcion_noTocaElPrecio() {
        Plato existente = platoExistente();
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(existente);
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo(CORREO_PROPIETARIO)).thenReturn(ID_PROPIETARIO);

        platoUseCase.modificarPlato(10L, null, "Solo cambia la descripcion", CORREO_PROPIETARIO);

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).actualizarPlato(captor.capture());
        assertEquals(15000, captor.getValue().getPrecio());
        assertEquals("Solo cambia la descripcion", captor.getValue().getDescripcion());
    }

    @Test
    void modificarPlato_siElPlatoNoExiste_lanzaPlatoNoEncontradoException() {
        when(platoPersistencePort.buscarPorId(99L)).thenReturn(null);

        assertThrows(PlatoNoEncontradoException.class,
                () -> platoUseCase.modificarPlato(99L, 20000, "Algo", CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void modificarPlato_sinPrecioNiDescripcion_lanzaValidacionException() {
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(platoExistente());
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo(CORREO_PROPIETARIO)).thenReturn(ID_PROPIETARIO);

        assertThrows(ValidacionException.class,
                () -> platoUseCase.modificarPlato(10L, null, null, CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void modificarPlato_conPrecioCero_lanzaValidacionException() {
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(platoExistente());
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo(CORREO_PROPIETARIO)).thenReturn(ID_PROPIETARIO);

        assertThrows(ValidacionException.class,
                () -> platoUseCase.modificarPlato(10L, 0, "Algo", CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void modificarPlato_sinIdPlato_lanzaValidacionException() {
        assertThrows(ValidacionException.class,
                () -> platoUseCase.modificarPlato(null, 20000, "Algo", CORREO_PROPIETARIO));
    }

    // ---------- cambiarEstado ----------

    @Test
    void cambiarEstado_deshabilitarConPropietarioCorrecto_actualiza() {
        Plato existente = platoExistente();
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(existente);
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo(CORREO_PROPIETARIO)).thenReturn(ID_PROPIETARIO);

        platoUseCase.cambiarEstado(10L, false, CORREO_PROPIETARIO);

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).actualizarPlato(captor.capture());
        assertFalse(captor.getValue().isActivo());
    }

    @Test
    void cambiarEstado_habilitarConPropietarioCorrecto_actualiza() {
        Plato existente = platoExistente();
        existente.setActivo(false);
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(existente);
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo(CORREO_PROPIETARIO)).thenReturn(ID_PROPIETARIO);

        platoUseCase.cambiarEstado(10L, true, CORREO_PROPIETARIO);

        ArgumentCaptor<Plato> captor = ArgumentCaptor.forClass(Plato.class);
        verify(platoPersistencePort).actualizarPlato(captor.capture());
        assertTrue(captor.getValue().isActivo());
    }

    @Test
    void cambiarEstado_siNoEsElPropietario_lanzaNoAutorizadoException() {
        when(platoPersistencePort.buscarPorId(10L)).thenReturn(platoExistente());
        when(restaurantePersistencePort.buscarPorId(1L)).thenReturn(restauranteDeJuan());
        when(usuarioExternoPort.obtenerIdUsuarioPorCorreo("otro@correo.com")).thenReturn(ID_OTRO_USUARIO);

        assertThrows(NoAutorizadoException.class,
                () -> platoUseCase.cambiarEstado(10L, false, "otro@correo.com"));
        verify(platoPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void cambiarEstado_siElPlatoNoExiste_lanzaPlatoNoEncontradoException() {
        when(platoPersistencePort.buscarPorId(99L)).thenReturn(null);

        assertThrows(PlatoNoEncontradoException.class,
                () -> platoUseCase.cambiarEstado(99L, false, CORREO_PROPIETARIO));
        verify(platoPersistencePort, never()).actualizarPlato(any());
    }
}
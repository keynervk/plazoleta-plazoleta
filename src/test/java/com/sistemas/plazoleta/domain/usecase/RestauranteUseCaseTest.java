package com.sistemas.plazoleta.domain.usecase;

import com.sistemas.plazoleta.domain.exception.RestauranteYaExisteException;
import com.sistemas.plazoleta.domain.exception.ValidacionException;
import com.sistemas.plazoleta.domain.model.Restaurante;
import com.sistemas.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.sistemas.plazoleta.domain.spi.IUsuarioExternoPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestauranteUseCaseTest {

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IUsuarioExternoPort usuarioExternoPort;

    @InjectMocks
    private RestauranteUseCase restauranteUseCase;

    private Restaurante restauranteValido() {
        return new Restaurante(null, "Pizza Buena", "900123456", "Calle 10 # 20-30",
                "+573005551234", "https://logo.com/pizza.png", 1L);
    }

    @Test
    void guardarRestaurante_conDatosValidos_guarda() {
        when(restaurantePersistencePort.existePorNit("900123456")).thenReturn(false);
        when(usuarioExternoPort.esPropietarioValido(1L)).thenReturn(true);

        restauranteUseCase.guardarRestaurante(restauranteValido());

        verify(restaurantePersistencePort).guardarRestaurante(any(Restaurante.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "456789", " 999 "})
    void guardarRestaurante_conNombreSoloNumeros_lanzaValidacionException(String nombre) {
        Restaurante restaurante = restauranteValido();
        restaurante.setNombre(nombre);

        assertThrows(ValidacionException.class, () -> restauranteUseCase.guardarRestaurante(restaurante));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_conNombreConLetrasYNumeros_guarda() {
        Restaurante restaurante = restauranteValido();
        restaurante.setNombre("Pizza 123");
        when(restaurantePersistencePort.existePorNit("900123456")).thenReturn(false);
        when(usuarioExternoPort.esPropietarioValido(1L)).thenReturn(true);

        restauranteUseCase.guardarRestaurante(restaurante);

        verify(restaurantePersistencePort).guardarRestaurante(restaurante);
    }

    @ParameterizedTest
    @ValueSource(strings = {"123abc", "900-123", "90O123456"})
    void guardarRestaurante_conNitNoNumerico_lanzaValidacionException(String nit) {
        Restaurante restaurante = restauranteValido();
        restaurante.setNit(nit);

        assertThrows(ValidacionException.class, () -> restauranteUseCase.guardarRestaurante(restaurante));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"+5730055512341", "300ABC1234", "++573005551234"})
    void guardarRestaurante_conTelefonoInvalido_lanzaValidacionException(String telefono) {
        Restaurante restaurante = restauranteValido();
        restaurante.setTelefono(telefono);

        assertThrows(ValidacionException.class, () -> restauranteUseCase.guardarRestaurante(restaurante));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_conUsuarioNulo_lanzaValidacionException() {
        assertThrows(ValidacionException.class, () -> restauranteUseCase.guardarRestaurante(null));
    }

    @Test
    void guardarRestaurante_siElNitYaExiste_lanzaRestauranteYaExisteException() {
        when(restaurantePersistencePort.existePorNit("900123456")).thenReturn(true);

        assertThrows(RestauranteYaExisteException.class,
                () -> restauranteUseCase.guardarRestaurante(restauranteValido()));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_siElIdPropietarioNoEsValido_lanzaValidacionException() {
        when(restaurantePersistencePort.existePorNit("900123456")).thenReturn(false);
        when(usuarioExternoPort.esPropietarioValido(1L)).thenReturn(false);

        assertThrows(ValidacionException.class,
                () -> restauranteUseCase.guardarRestaurante(restauranteValido()));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }
}
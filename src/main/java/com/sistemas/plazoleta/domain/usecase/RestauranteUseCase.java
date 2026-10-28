package com.sistemas.plazoleta.domain.usecase;

import com.sistemas.plazoleta.domain.api.IRestauranteServicePort;
import com.sistemas.plazoleta.domain.exception.RestauranteNoEncontradoException;
import com.sistemas.plazoleta.domain.exception.RestauranteYaExisteException;
import com.sistemas.plazoleta.domain.exception.ValidacionException;
import com.sistemas.plazoleta.domain.model.Restaurante;
import com.sistemas.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.sistemas.plazoleta.domain.spi.IUsuarioExternoPort;

import java.util.List;
import java.util.regex.Pattern;

public class RestauranteUseCase implements IRestauranteServicePort {

    private static final Pattern PATRON_SOLO_NUMEROS = Pattern.compile("^\\d+$");
    private static final Pattern PATRON_NIT = Pattern.compile("^\\d+$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^\\+?\\d{1,12}$");

    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IUsuarioExternoPort usuarioExternoPort;

    public RestauranteUseCase(IRestaurantePersistencePort restaurantePersistencePort,
                              IUsuarioExternoPort usuarioExternoPort) {
        this.restaurantePersistencePort = restaurantePersistencePort;
        this.usuarioExternoPort = usuarioExternoPort;
    }

    @Override
    public void guardarRestaurante(Restaurante restaurante) {
        validarCamposObligatorios(restaurante);
        validarNombre(restaurante.getNombre());
        validarNit(restaurante.getNit());
        validarTelefono(restaurante.getTelefono());

        if (restaurantePersistencePort.existePorNit(restaurante.getNit())) {
            throw new RestauranteYaExisteException("Ya existe un restaurante con ese NIT");
        }

        if (!usuarioExternoPort.esPropietarioValido(restaurante.getIdPropietario())) {
            throw new ValidacionException("El idPropietario no corresponde a un usuario con rol PROPIETARIO");
        }

        restaurantePersistencePort.guardarRestaurante(restaurante);
    }

    @Override
    public List<Restaurante> listarRestaurantes() {
        return restaurantePersistencePort.listarRestaurantes();
    }

    @Override
    public List<Restaurante> listarRestaurantesPaginado(int pagina, int tamanioPagina) {
        if (pagina < 0) {
            throw new ValidacionException("El numero de pagina no puede ser negativo");
        }
        if (tamanioPagina <= 0) {
            throw new ValidacionException("El tamanio de la pagina debe ser mayor a 0");
        }
        return restaurantePersistencePort.listarRestaurantesPaginado(pagina, tamanioPagina);
    }

    @Override
    public Restaurante buscarRestaurantePorId(Long id) {
        if (id == null) {
            throw new ValidacionException("El id del restaurante es obligatorio");
        }
        Restaurante restaurante = restaurantePersistencePort.buscarPorId(id);
        if (restaurante == null) {
            throw new RestauranteNoEncontradoException("No existe un restaurante con ese id");
        }
        return restaurante;
    }

    private void validarCamposObligatorios(Restaurante restaurante) {
        if (restaurante == null) {
            throw new ValidacionException("El restaurante es obligatorio");
        }
        if (estaVacio(restaurante.getNombre())) {
            throw new ValidacionException("El nombre es obligatorio");
        }
        if (estaVacio(restaurante.getNit())) {
            throw new ValidacionException("El NIT es obligatorio");
        }
        if (estaVacio(restaurante.getDireccion())) {
            throw new ValidacionException("La direccion es obligatoria");
        }
        if (estaVacio(restaurante.getTelefono())) {
            throw new ValidacionException("El telefono es obligatorio");
        }
        if (estaVacio(restaurante.getUrlLogo())) {
            throw new ValidacionException("La url del logo es obligatoria");
        }
        if (restaurante.getIdPropietario() == null) {
            throw new ValidacionException("El id del propietario es obligatorio");
        }
    }

    private void validarNombre(String nombre) {
        if (PATRON_SOLO_NUMEROS.matcher(nombre.trim()).matches()) {
            throw new ValidacionException("El nombre del restaurante no puede contener solo numeros");
        }
    }

    private void validarNit(String nit) {
        if (!PATRON_NIT.matcher(nit).matches()) {
            throw new ValidacionException("El NIT debe ser unicamente numerico");
        }
    }

    private void validarTelefono(String telefono) {
        if (!PATRON_TELEFONO.matcher(telefono).matches()) {
            throw new ValidacionException(
                    "El telefono debe tener maximo 13 caracteres, solo digitos y opcionalmente un + al inicio");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
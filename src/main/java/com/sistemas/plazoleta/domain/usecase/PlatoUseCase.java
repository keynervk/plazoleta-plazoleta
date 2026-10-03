package com.sistemas.plazoleta.domain.usecase;

import com.sistemas.plazoleta.domain.api.IPlatoServicePort;
import com.sistemas.plazoleta.domain.exception.ValidacionException;
import com.sistemas.plazoleta.domain.model.Plato;
import com.sistemas.plazoleta.domain.spi.IPlatoPersistencePort;
import com.sistemas.plazoleta.domain.spi.IRestaurantePersistencePort;

public class PlatoUseCase implements IPlatoServicePort {

    private final IPlatoPersistencePort platoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;

    public PlatoUseCase(IPlatoPersistencePort platoPersistencePort,
                        IRestaurantePersistencePort restaurantePersistencePort) {
        this.platoPersistencePort = platoPersistencePort;
        this.restaurantePersistencePort = restaurantePersistencePort;
    }

    @Override
    public void crearPlato(Plato plato) {
        validarCamposObligatorios(plato);

        if (plato.getPrecio() == null || plato.getPrecio() <= 0) {
            throw new ValidacionException("El precio debe ser un numero entero positivo mayor a 0");
        }

        if (plato.getIdRestaurante() == null) {
            throw new ValidacionException("El id del restaurante es obligatorio");
        }

        if (!restaurantePersistencePort.existePorId(plato.getIdRestaurante())) {
            throw new ValidacionException("El restaurante indicado no existe");
        }

        plato.setActivo(true);
        platoPersistencePort.guardarPlato(plato);
    }

    private void validarCamposObligatorios(Plato plato) {
        if (plato == null) {
            throw new ValidacionException("El plato es obligatorio");
        }
        if (estaVacio(plato.getNombre())) {
            throw new ValidacionException("El nombre del plato es obligatorio");
        }
        if (estaVacio(plato.getDescripcion())) {
            throw new ValidacionException("La descripcion es obligatoria");
        }
        if (estaVacio(plato.getUrlImagen())) {
            throw new ValidacionException("La url de la imagen es obligatoria");
        }
        if (estaVacio(plato.getCategoria())) {
            throw new ValidacionException("La categoria es obligatoria");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
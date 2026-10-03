package com.sistemas.plazoleta.domain.spi;

import com.sistemas.plazoleta.domain.model.Restaurante;

public interface IRestaurantePersistencePort {

    Restaurante guardarRestaurante(Restaurante restaurante);

    boolean existePorNit(String nit);
}
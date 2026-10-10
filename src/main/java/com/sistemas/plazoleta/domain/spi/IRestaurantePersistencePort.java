package com.sistemas.plazoleta.domain.spi;

import com.sistemas.plazoleta.domain.model.Restaurante;

import java.util.List;

public interface IRestaurantePersistencePort {

    Restaurante guardarRestaurante(Restaurante restaurante);

    boolean existePorNit(String nit);

    boolean existePorId(Long id);

    List<Restaurante> listarRestaurantes();

    Restaurante buscarPorId(Long id);
}
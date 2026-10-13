package com.sistemas.plazoleta.domain.spi;

import com.sistemas.plazoleta.domain.model.Restaurante;

import java.util.List;

public interface IRestaurantePersistencePort {

    Restaurante guardarRestaurante(Restaurante restaurante);

    boolean existePorNit(String nit);

    boolean existePorId(Long id);

    Restaurante buscarPorId(Long id);

    List<Restaurante> listarRestaurantes();

    List<Restaurante> listarRestaurantesPaginado(int pagina, int tamanioPagina);
}
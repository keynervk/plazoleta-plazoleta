package com.sistemas.plazoleta.domain.spi;

import com.sistemas.plazoleta.domain.model.Plato;

import java.util.List;

public interface IPlatoPersistencePort {

    Plato guardarPlato(Plato plato);

    Plato buscarPorId(Long id);

    Plato actualizarPlato(Plato plato);

    List<Plato> listarPorRestaurante(Long idRestaurante);

    List<Plato> listarActivosPorRestaurantePaginado(Long idRestaurante, String categoria, int pagina, int tamanioPagina);
}
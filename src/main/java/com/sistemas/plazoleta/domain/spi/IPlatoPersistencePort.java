package com.sistemas.plazoleta.domain.spi;

import com.sistemas.plazoleta.domain.model.Plato;

public interface IPlatoPersistencePort {

    Plato guardarPlato(Plato plato);

    Plato buscarPorId(Long id);

    Plato actualizarPlato(Plato plato);
}
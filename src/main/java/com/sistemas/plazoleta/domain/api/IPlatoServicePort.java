package com.sistemas.plazoleta.domain.api;

import com.sistemas.plazoleta.domain.model.Plato;

public interface IPlatoServicePort {

    void crearPlato(Plato plato);

    void modificarPlato(Long idPlato, Integer nuevoPrecio, String nuevaDescripcion);

    void cambiarEstado(Long idPlato, boolean activo);
}
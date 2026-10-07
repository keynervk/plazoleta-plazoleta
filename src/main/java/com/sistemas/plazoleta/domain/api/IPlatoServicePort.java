package com.sistemas.plazoleta.domain.api;

import com.sistemas.plazoleta.domain.model.Plato;

import java.util.List;

public interface IPlatoServicePort {

    void crearPlato(Plato plato);

    void modificarPlato(Long idPlato, Integer nuevoPrecio, String nuevaDescripcion);

    void cambiarEstado(Long idPlato, boolean activo);

    List<Plato> listarPorRestaurante(Long idRestaurante);
}
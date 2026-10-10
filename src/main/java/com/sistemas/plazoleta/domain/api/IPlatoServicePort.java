package com.sistemas.plazoleta.domain.api;

import com.sistemas.plazoleta.domain.model.Plato;

import java.util.List;

public interface IPlatoServicePort {

    void crearPlato(Plato plato, String correoUsuarioAutenticado);

    void modificarPlato(Long idPlato, Integer nuevoPrecio, String nuevaDescripcion, String correoUsuarioAutenticado);

    void cambiarEstado(Long idPlato, boolean activo, String correoUsuarioAutenticado);

    List<Plato> listarPorRestaurante(Long idRestaurante);
}
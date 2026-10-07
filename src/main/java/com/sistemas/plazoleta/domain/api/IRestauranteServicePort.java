package com.sistemas.plazoleta.domain.api;

import com.sistemas.plazoleta.domain.model.Restaurante;

import java.util.List;

public interface IRestauranteServicePort {

    void guardarRestaurante(Restaurante restaurante);

    List<Restaurante> listarRestaurantes();
}
package com.sistemas.plazoleta.application.handler;

import com.sistemas.plazoleta.application.dto.RestauranteRequestDto;
import com.sistemas.plazoleta.application.mapper.RestauranteRequestMapper;
import com.sistemas.plazoleta.domain.api.IRestauranteServicePort;
import org.springframework.stereotype.Service;

@Service
public class RestauranteHandler {

    private final IRestauranteServicePort restauranteServicePort;
    private final RestauranteRequestMapper mapper;

    public RestauranteHandler(IRestauranteServicePort restauranteServicePort, RestauranteRequestMapper mapper) {
        this.restauranteServicePort = restauranteServicePort;
        this.mapper = mapper;
    }

    public void guardarRestaurante(RestauranteRequestDto dto) {
        restauranteServicePort.guardarRestaurante(mapper.toModel(dto));
    }
}
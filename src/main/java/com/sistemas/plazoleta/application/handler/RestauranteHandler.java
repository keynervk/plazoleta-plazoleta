package com.sistemas.plazoleta.application.handler;

import com.sistemas.plazoleta.application.dto.RestauranteRequestDto;
import com.sistemas.plazoleta.application.dto.RestauranteResponseDto;
import com.sistemas.plazoleta.application.mapper.RestauranteRequestMapper;
import com.sistemas.plazoleta.application.mapper.ResponseMapper;
import com.sistemas.plazoleta.domain.api.IRestauranteServicePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteHandler {

    private final IRestauranteServicePort restauranteServicePort;
    private final RestauranteRequestMapper mapper;
    private final ResponseMapper responseMapper;

    public RestauranteHandler(IRestauranteServicePort restauranteServicePort,
                              RestauranteRequestMapper mapper,
                              ResponseMapper responseMapper) {
        this.restauranteServicePort = restauranteServicePort;
        this.mapper = mapper;
        this.responseMapper = responseMapper;
    }

    public void guardarRestaurante(RestauranteRequestDto dto) {
        restauranteServicePort.guardarRestaurante(mapper.toModel(dto));
    }

    public List<RestauranteResponseDto> listarRestaurantes() {
        return restauranteServicePort.listarRestaurantes().stream().map(responseMapper::toDto).toList();
    }
}
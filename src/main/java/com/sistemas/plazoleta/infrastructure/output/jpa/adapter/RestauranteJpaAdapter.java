package com.sistemas.plazoleta.infrastructure.output.jpa.adapter;

import com.sistemas.plazoleta.domain.model.Restaurante;
import com.sistemas.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.sistemas.plazoleta.infrastructure.output.jpa.mapper.RestauranteEntityMapper;
import com.sistemas.plazoleta.infrastructure.output.jpa.repository.IRestauranteRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestauranteJpaAdapter implements IRestaurantePersistencePort {

    private final IRestauranteRepository restauranteRepository;
    private final RestauranteEntityMapper mapper;

    public RestauranteJpaAdapter(IRestauranteRepository restauranteRepository, RestauranteEntityMapper mapper) {
        this.restauranteRepository = restauranteRepository;
        this.mapper = mapper;
    }

    @Override
    public Restaurante guardarRestaurante(Restaurante restaurante) {
        return mapper.toModel(restauranteRepository.save(mapper.toEntity(restaurante)));
    }

    @Override
    public boolean existePorNit(String nit) {
        return restauranteRepository.existsByNit(nit);
    }

    @Override
    public boolean existePorId(Long id) {
        return restauranteRepository.existsById(id);
    }

    @Override
    public List<Restaurante> listarRestaurantes() {
        return restauranteRepository.findAll().stream().map(mapper::toModel).toList();
    }
}
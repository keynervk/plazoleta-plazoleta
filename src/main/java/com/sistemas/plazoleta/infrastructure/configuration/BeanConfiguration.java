package com.sistemas.plazoleta.infrastructure.configuration;

import com.sistemas.plazoleta.domain.api.IRestauranteServicePort;
import com.sistemas.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.sistemas.plazoleta.domain.spi.IUsuarioExternoPort;
import com.sistemas.plazoleta.domain.usecase.RestauranteUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IRestauranteServicePort restauranteServicePort(IRestaurantePersistencePort restaurantePersistencePort,
                                                           IUsuarioExternoPort usuarioExternoPort) {
        return new RestauranteUseCase(restaurantePersistencePort, usuarioExternoPort);
    }
}
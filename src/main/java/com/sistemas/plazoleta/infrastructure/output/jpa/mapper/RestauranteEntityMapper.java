package com.sistemas.plazoleta.infrastructure.output.jpa.mapper;

import com.sistemas.plazoleta.domain.model.Restaurante;
import com.sistemas.plazoleta.infrastructure.output.jpa.entity.RestauranteEntity;
import org.springframework.stereotype.Component;

@Component
public class RestauranteEntityMapper {

    public RestauranteEntity toEntity(Restaurante restaurante) {
        RestauranteEntity entity = new RestauranteEntity();
        entity.setId(restaurante.getId());
        entity.setNombre(restaurante.getNombre());
        entity.setNit(restaurante.getNit());
        entity.setDireccion(restaurante.getDireccion());
        entity.setTelefono(restaurante.getTelefono());
        entity.setUrlLogo(restaurante.getUrlLogo());
        entity.setIdPropietario(restaurante.getIdPropietario());
        return entity;
    }

    public Restaurante toModel(RestauranteEntity entity) {
        return new Restaurante(
                entity.getId(),
                entity.getNombre(),
                entity.getNit(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getUrlLogo(),
                entity.getIdPropietario());
    }
}
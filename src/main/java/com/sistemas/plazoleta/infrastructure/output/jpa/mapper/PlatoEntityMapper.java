package com.sistemas.plazoleta.infrastructure.output.jpa.mapper;

import com.sistemas.plazoleta.domain.model.Plato;
import com.sistemas.plazoleta.infrastructure.output.jpa.entity.PlatoEntity;
import org.springframework.stereotype.Component;

@Component
public class PlatoEntityMapper {

    public PlatoEntity toEntity(Plato plato) {
        PlatoEntity entity = new PlatoEntity();
        entity.setId(plato.getId());
        entity.setNombre(plato.getNombre());
        entity.setPrecio(plato.getPrecio());
        entity.setDescripcion(plato.getDescripcion());
        entity.setUrlImagen(plato.getUrlImagen());
        entity.setCategoria(plato.getCategoria());
        entity.setActivo(plato.isActivo());
        entity.setIdRestaurante(plato.getIdRestaurante());
        return entity;
    }

    public Plato toModel(PlatoEntity entity) {
        return new Plato(
                entity.getId(),
                entity.getNombre(),
                entity.getPrecio(),
                entity.getDescripcion(),
                entity.getUrlImagen(),
                entity.getCategoria(),
                entity.isActivo(),
                entity.getIdRestaurante());
    }
}
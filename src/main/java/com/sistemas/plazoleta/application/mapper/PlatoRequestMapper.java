package com.sistemas.plazoleta.application.mapper;

import com.sistemas.plazoleta.application.dto.PlatoRequestDto;
import com.sistemas.plazoleta.domain.model.Plato;
import org.springframework.stereotype.Component;

@Component
public class PlatoRequestMapper {

    public Plato toModel(PlatoRequestDto dto) {
        Plato plato = new Plato();
        plato.setNombre(dto.nombre());
        plato.setPrecio(dto.precio());
        plato.setDescripcion(dto.descripcion());
        plato.setUrlImagen(dto.urlImagen());
        plato.setCategoria(dto.categoria());
        plato.setIdRestaurante(dto.idRestaurante());
        return plato;
    }
}
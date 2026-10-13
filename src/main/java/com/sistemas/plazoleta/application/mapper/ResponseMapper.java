package com.sistemas.plazoleta.application.mapper;

import com.sistemas.plazoleta.application.dto.PlatoResponseDto;
import com.sistemas.plazoleta.application.dto.RestauranteListadoDto;
import com.sistemas.plazoleta.application.dto.RestauranteResponseDto;
import com.sistemas.plazoleta.domain.model.Plato;
import com.sistemas.plazoleta.domain.model.Restaurante;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

    public RestauranteResponseDto toDto(Restaurante r) {
        return new RestauranteResponseDto(r.getId(), r.getNombre(), r.getNit(), r.getDireccion(),
                r.getTelefono(), r.getUrlLogo(), r.getIdPropietario());
    }

    public RestauranteListadoDto toListadoDto(Restaurante r) {
        return new RestauranteListadoDto(r.getNombre(), r.getUrlLogo());
    }

    public PlatoResponseDto toDto(Plato p) {
        return new PlatoResponseDto(p.getId(), p.getNombre(), p.getPrecio(), p.getDescripcion(),
                p.getUrlImagen(), p.getCategoria(), p.isActivo(), p.getIdRestaurante());
    }
}
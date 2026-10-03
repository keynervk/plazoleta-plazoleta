package com.sistemas.plazoleta.application.mapper;

import com.sistemas.plazoleta.application.dto.RestauranteRequestDto;
import com.sistemas.plazoleta.domain.model.Restaurante;
import org.springframework.stereotype.Component;

@Component
public class RestauranteRequestMapper {

    public Restaurante toModel(RestauranteRequestDto dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNombre(dto.nombre());
        restaurante.setNit(dto.nit());
        restaurante.setDireccion(dto.direccion());
        restaurante.setTelefono(dto.telefono());
        restaurante.setUrlLogo(dto.urlLogo());
        restaurante.setIdPropietario(dto.idPropietario());
        return restaurante;
    }
}
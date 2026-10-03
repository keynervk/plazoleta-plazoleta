package com.sistemas.plazoleta.application.handler;

import com.sistemas.plazoleta.application.dto.ModificarPlatoRequestDto;
import com.sistemas.plazoleta.application.dto.PlatoRequestDto;
import com.sistemas.plazoleta.application.mapper.PlatoRequestMapper;
import com.sistemas.plazoleta.domain.api.IPlatoServicePort;
import org.springframework.stereotype.Service;

@Service
public class PlatoHandler {

    private final IPlatoServicePort platoServicePort;
    private final PlatoRequestMapper mapper;

    public PlatoHandler(IPlatoServicePort platoServicePort, PlatoRequestMapper mapper) {
        this.platoServicePort = platoServicePort;
        this.mapper = mapper;
    }

    public void crearPlato(PlatoRequestDto dto) {
        platoServicePort.crearPlato(mapper.toModel(dto));
    }

    public void modificarPlato(Long idPlato, ModificarPlatoRequestDto dto) {
        platoServicePort.modificarPlato(idPlato, dto.precio(), dto.descripcion());
    }
}
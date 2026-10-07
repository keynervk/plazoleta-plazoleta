package com.sistemas.plazoleta.application.handler;

import com.sistemas.plazoleta.application.dto.ModificarPlatoRequestDto;
import com.sistemas.plazoleta.application.dto.PlatoRequestDto;
import com.sistemas.plazoleta.application.dto.PlatoResponseDto;
import com.sistemas.plazoleta.application.mapper.PlatoRequestMapper;
import com.sistemas.plazoleta.application.mapper.ResponseMapper;
import com.sistemas.plazoleta.domain.api.IPlatoServicePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatoHandler {

    private final IPlatoServicePort platoServicePort;
    private final PlatoRequestMapper mapper;
    private final ResponseMapper responseMapper;

    public PlatoHandler(IPlatoServicePort platoServicePort, PlatoRequestMapper mapper, ResponseMapper responseMapper) {
        this.platoServicePort = platoServicePort;
        this.mapper = mapper;
        this.responseMapper = responseMapper;
    }

    public void crearPlato(PlatoRequestDto dto) {
        platoServicePort.crearPlato(mapper.toModel(dto));
    }

    public void modificarPlato(Long idPlato, ModificarPlatoRequestDto dto) {
        platoServicePort.modificarPlato(idPlato, dto.precio(), dto.descripcion());
    }

    public void cambiarEstado(Long idPlato, boolean activo) {
        platoServicePort.cambiarEstado(idPlato, activo);
    }

    public List<PlatoResponseDto> listarPorRestaurante(Long idRestaurante) {
        return platoServicePort.listarPorRestaurante(idRestaurante).stream().map(responseMapper::toDto).toList();
    }
}
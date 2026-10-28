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

    public void crearPlato(PlatoRequestDto dto, String correoUsuarioAutenticado) {
        platoServicePort.crearPlato(mapper.toModel(dto), correoUsuarioAutenticado);
    }

    public void modificarPlato(Long idPlato, ModificarPlatoRequestDto dto, String correoUsuarioAutenticado) {
        platoServicePort.modificarPlato(idPlato, dto.precio(), dto.descripcion(), correoUsuarioAutenticado);
    }

    public void cambiarEstado(Long idPlato, boolean activo, String correoUsuarioAutenticado) {
        platoServicePort.cambiarEstado(idPlato, activo, correoUsuarioAutenticado);
    }

    public List<PlatoResponseDto> listarPorRestaurante(Long idRestaurante) {
        return platoServicePort.listarPorRestaurante(idRestaurante).stream().map(responseMapper::toDto).toList();
    }

    public List<PlatoResponseDto> listarPaginado(Long idRestaurante, String categoria, int pagina, int tamanioPagina) {
        return platoServicePort.listarPlatosPaginado(idRestaurante, categoria, pagina, tamanioPagina)
                .stream().map(responseMapper::toDto).toList();
    }
}
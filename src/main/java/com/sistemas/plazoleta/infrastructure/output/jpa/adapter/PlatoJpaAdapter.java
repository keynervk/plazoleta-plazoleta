package com.sistemas.plazoleta.infrastructure.output.jpa.adapter;

import com.sistemas.plazoleta.domain.model.Plato;
import com.sistemas.plazoleta.domain.spi.IPlatoPersistencePort;
import com.sistemas.plazoleta.infrastructure.output.jpa.mapper.PlatoEntityMapper;
import com.sistemas.plazoleta.infrastructure.output.jpa.repository.IPlatoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlatoJpaAdapter implements IPlatoPersistencePort {

    private final IPlatoRepository platoRepository;
    private final PlatoEntityMapper mapper;

    public PlatoJpaAdapter(IPlatoRepository platoRepository, PlatoEntityMapper mapper) {
        this.platoRepository = platoRepository;
        this.mapper = mapper;
    }

    @Override
    public Plato guardarPlato(Plato plato) {
        return mapper.toModel(platoRepository.save(mapper.toEntity(plato)));
    }

    @Override
    public Plato buscarPorId(Long id) {
        return platoRepository.findById(id).map(mapper::toModel).orElse(null);
    }

    @Override
    public Plato actualizarPlato(Plato plato) {
        return mapper.toModel(platoRepository.save(mapper.toEntity(plato)));
    }

    @Override
    public List<Plato> listarPorRestaurante(Long idRestaurante) {
        return platoRepository.findByIdRestaurante(idRestaurante).stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Plato> listarActivosPorRestaurantePaginado(Long idRestaurante, String categoria,
                                                          int pagina, int tamanioPagina) {
        var pageable = PageRequest.of(pagina, tamanioPagina, Sort.by(Sort.Direction.ASC, "nombre"));
        var resultado = categoria == null
                ? platoRepository.findByIdRestauranteAndActivoTrue(idRestaurante, pageable)
                : platoRepository.findByIdRestauranteAndCategoriaIgnoreCaseAndActivoTrue(idRestaurante, categoria, pageable);
        return resultado.stream().map(mapper::toModel).toList();
    }
}
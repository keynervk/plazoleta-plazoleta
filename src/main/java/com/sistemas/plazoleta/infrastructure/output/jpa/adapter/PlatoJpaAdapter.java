package com.sistemas.plazoleta.infrastructure.output.jpa.adapter;

import com.sistemas.plazoleta.domain.model.Plato;
import com.sistemas.plazoleta.domain.spi.IPlatoPersistencePort;
import com.sistemas.plazoleta.infrastructure.output.jpa.mapper.PlatoEntityMapper;
import com.sistemas.plazoleta.infrastructure.output.jpa.repository.IPlatoRepository;
import org.springframework.stereotype.Component;

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
}
package com.sistemas.plazoleta.infrastructure.output.jpa.repository;

import com.sistemas.plazoleta.infrastructure.output.jpa.entity.PlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPlatoRepository extends JpaRepository<PlatoEntity, Long> {

    List<PlatoEntity> findByIdRestaurante(Long idRestaurante);
}
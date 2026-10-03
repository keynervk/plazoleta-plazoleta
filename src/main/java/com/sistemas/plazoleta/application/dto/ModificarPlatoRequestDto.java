package com.sistemas.plazoleta.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ModificarPlatoRequestDto(
        @Schema(example = "18000") Integer precio,
        @Schema(example = "Nueva descripcion del plato") String descripcion) {
}
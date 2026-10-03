package com.sistemas.plazoleta.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlatoRequestDto(
        @Schema(example = "Hamburguesa clasica") String nombre,
        @Schema(example = "15000") Integer precio,
        @Schema(example = "Carne, queso y vegetales") String descripcion,
        @Schema(example = "https://img.com/hamburguesa.png") String urlImagen,
        @Schema(example = "Comida rapida") String categoria,
        @Schema(example = "1") Long idRestaurante) {
}
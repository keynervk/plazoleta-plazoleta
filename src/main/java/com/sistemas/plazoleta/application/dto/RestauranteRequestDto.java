package com.sistemas.plazoleta.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RestauranteRequestDto(
        @Schema(example = "Pizza Buena") String nombre,
        @Schema(example = "900123456") String nit,
        @Schema(example = "Calle 10 # 20-30") String direccion,
        @Schema(example = "+573005551234") String telefono,
        @Schema(example = "https://logo.com/pizza.png") String urlLogo,
        @Schema(example = "1") Long idPropietario) {
}
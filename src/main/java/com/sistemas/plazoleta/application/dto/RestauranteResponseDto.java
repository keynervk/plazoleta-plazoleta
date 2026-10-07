package com.sistemas.plazoleta.application.dto;

public record RestauranteResponseDto(
        Long id,
        String nombre,
        String nit,
        String direccion,
        String telefono,
        String urlLogo,
        Long idPropietario) {
}
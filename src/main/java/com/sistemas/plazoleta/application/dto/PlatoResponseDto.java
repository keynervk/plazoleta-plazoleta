package com.sistemas.plazoleta.application.dto;

public record PlatoResponseDto(
        Long id,
        String nombre,
        Integer precio,
        String descripcion,
        String urlImagen,
        String categoria,
        boolean activo,
        Long idRestaurante) {
}
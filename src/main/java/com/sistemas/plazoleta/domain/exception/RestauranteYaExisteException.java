package com.sistemas.plazoleta.domain.exception;

public class RestauranteYaExisteException extends RuntimeException {
    public RestauranteYaExisteException(String mensaje) {
        super(mensaje);
    }
}
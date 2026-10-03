package com.sistemas.plazoleta.domain.exception;

public class PlatoNoEncontradoException extends RuntimeException {
    public PlatoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
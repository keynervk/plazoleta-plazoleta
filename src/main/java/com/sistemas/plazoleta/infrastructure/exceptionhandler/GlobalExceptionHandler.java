package com.sistemas.plazoleta.infrastructure.exceptionhandler;

import com.sistemas.plazoleta.domain.exception.NoAutorizadoException;
import com.sistemas.plazoleta.domain.exception.PlatoNoEncontradoException;
import com.sistemas.plazoleta.domain.exception.RestauranteNoEncontradoException;
import com.sistemas.plazoleta.domain.exception.RestauranteYaExisteException;
import com.sistemas.plazoleta.domain.exception.ValidacionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(int status, String mensaje) {
    }

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(ValidacionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, ex.getMessage()));
    }

    @ExceptionHandler(RestauranteYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleYaExiste(RestauranteYaExisteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, ex.getMessage()));
    }

    @ExceptionHandler(PlatoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePlatoNoEncontrado(PlatoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, ex.getMessage()));
    }

    @ExceptionHandler(RestauranteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRestauranteNoEncontrado(RestauranteNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, ex.getMessage()));
    }

    @ExceptionHandler(NoAutorizadoException.class)
    public ResponseEntity<ErrorResponse> handleNoAutorizado(NoAutorizadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(403, ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "El cuerpo de la peticion es invalido"));
    }
}
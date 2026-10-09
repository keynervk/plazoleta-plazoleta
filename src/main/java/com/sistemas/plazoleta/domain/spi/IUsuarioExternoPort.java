package com.sistemas.plazoleta.domain.spi;

public interface IUsuarioExternoPort {

    boolean esPropietarioValido(Long idUsuario);

    Long obtenerIdUsuarioPorCorreo(String correo);
}
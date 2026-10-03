package com.sistemas.plazoleta.infrastructure.output.cliente;

import com.sistemas.plazoleta.domain.spi.IUsuarioExternoPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioExternoAdapter implements IUsuarioExternoPort {

    private final RestTemplate restTemplate;
    private final String urlBaseUsuarios;

    public UsuarioExternoAdapter(RestTemplate restTemplate,
                                 @Value("${app.usuarios.url}") String urlBaseUsuarios) {
        this.restTemplate = restTemplate;
        this.urlBaseUsuarios = urlBaseUsuarios;
    }

    @Override
    public boolean esPropietarioValido(Long idUsuario) {
        try {
            String url = urlBaseUsuarios + "/usuarios/" + idUsuario + "/es-propietario";
            Boolean resultado = restTemplate.getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(resultado);
        } catch (RestClientException e) {
            return false;
        }
    }
}
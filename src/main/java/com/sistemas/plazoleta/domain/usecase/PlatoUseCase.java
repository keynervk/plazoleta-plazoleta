package com.sistemas.plazoleta.domain.usecase;

import com.sistemas.plazoleta.domain.api.IPlatoServicePort;
import com.sistemas.plazoleta.domain.exception.PlatoNoEncontradoException;
import com.sistemas.plazoleta.domain.exception.ValidacionException;
import com.sistemas.plazoleta.domain.model.Plato;
import com.sistemas.plazoleta.domain.spi.IPlatoPersistencePort;
import com.sistemas.plazoleta.domain.spi.IRestaurantePersistencePort;

import java.util.List;

public class PlatoUseCase implements IPlatoServicePort {

    private final IPlatoPersistencePort platoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;

    public PlatoUseCase(IPlatoPersistencePort platoPersistencePort,
                        IRestaurantePersistencePort restaurantePersistencePort) {
        this.platoPersistencePort = platoPersistencePort;
        this.restaurantePersistencePort = restaurantePersistencePort;
    }

    @Override
    public void crearPlato(Plato plato) {
        validarCamposObligatorios(plato);

        if (plato.getPrecio() == null || plato.getPrecio() <= 0) {
            throw new ValidacionException("El precio debe ser un numero entero positivo mayor a 0");
        }

        if (plato.getIdRestaurante() == null) {
            throw new ValidacionException("El id del restaurante es obligatorio");
        }

        if (!restaurantePersistencePort.existePorId(plato.getIdRestaurante())) {
            throw new ValidacionException("El restaurante indicado no existe");
        }

        plato.setActivo(true);
        platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public void modificarPlato(Long idPlato, Integer nuevoPrecio, String nuevaDescripcion) {
        if (idPlato == null) {
            throw new ValidacionException("El id del plato es obligatorio");
        }

        Plato plato = platoPersistencePort.buscarPorId(idPlato);
        if (plato == null) {
            throw new PlatoNoEncontradoException("No existe un plato con ese id");
        }

        if (nuevoPrecio == null && (nuevaDescripcion == null || nuevaDescripcion.isBlank())) {
            throw new ValidacionException("Debe enviar al menos el precio o la descripcion a modificar");
        }

        if (nuevoPrecio != null) {
            if (nuevoPrecio <= 0) {
                throw new ValidacionException("El precio debe ser un numero entero positivo mayor a 0");
            }
            plato.setPrecio(nuevoPrecio);
        }

        if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()) {
            plato.setDescripcion(nuevaDescripcion);
        }

        platoPersistencePort.actualizarPlato(plato);
    }

    @Override
    public void cambiarEstado(Long idPlato, boolean activo) {
        if (idPlato == null) {
            throw new ValidacionException("El id del plato es obligatorio");
        }

        Plato plato = platoPersistencePort.buscarPorId(idPlato);
        if (plato == null) {
            throw new PlatoNoEncontradoException("No existe un plato con ese id");
        }

        plato.setActivo(activo);
        platoPersistencePort.actualizarPlato(plato);
    }

    @Override
    public List<Plato> listarPorRestaurante(Long idRestaurante) {
        return platoPersistencePort.listarPorRestaurante(idRestaurante);
    }

    private void validarCamposObligatorios(Plato plato) {
        if (plato == null) {
            throw new ValidacionException("El plato es obligatorio");
        }
        if (estaVacio(plato.getNombre())) {
            throw new ValidacionException("El nombre del plato es obligatorio");
        }
        if (estaVacio(plato.getDescripcion())) {
            throw new ValidacionException("La descripcion es obligatoria");
        }
        if (estaVacio(plato.getUrlImagen())) {
            throw new ValidacionException("La url de la imagen es obligatoria");
        }
        if (estaVacio(plato.getCategoria())) {
            throw new ValidacionException("La categoria es obligatoria");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
package com.sistemas.plazoleta.domain.usecase;

import com.sistemas.plazoleta.domain.api.IPlatoServicePort;
import com.sistemas.plazoleta.domain.exception.NoAutorizadoException;
import com.sistemas.plazoleta.domain.exception.PlatoNoEncontradoException;
import com.sistemas.plazoleta.domain.exception.ValidacionException;
import com.sistemas.plazoleta.domain.model.Plato;
import com.sistemas.plazoleta.domain.model.Restaurante;
import com.sistemas.plazoleta.domain.spi.IPlatoPersistencePort;
import com.sistemas.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.sistemas.plazoleta.domain.spi.IUsuarioExternoPort;

import java.util.List;

public class PlatoUseCase implements IPlatoServicePort {

    private final IPlatoPersistencePort platoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IUsuarioExternoPort usuarioExternoPort;

    public PlatoUseCase(IPlatoPersistencePort platoPersistencePort,
                        IRestaurantePersistencePort restaurantePersistencePort,
                        IUsuarioExternoPort usuarioExternoPort) {
        this.platoPersistencePort = platoPersistencePort;
        this.restaurantePersistencePort = restaurantePersistencePort;
        this.usuarioExternoPort = usuarioExternoPort;
    }

    @Override
    public void crearPlato(Plato plato, String correoUsuarioAutenticado) {
        validarCamposObligatorios(plato);

        if (plato.getPrecio() == null || plato.getPrecio() <= 0) {
            throw new ValidacionException("El precio debe ser un numero entero positivo mayor a 0");
        }

        if (plato.getIdRestaurante() == null) {
            throw new ValidacionException("El id del restaurante es obligatorio");
        }

        Restaurante restaurante = restaurantePersistencePort.buscarPorId(plato.getIdRestaurante());
        if (restaurante == null) {
            throw new ValidacionException("El restaurante indicado no existe");
        }

        validarPropietario(restaurante, correoUsuarioAutenticado);

        plato.setActivo(true);
        platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public void modificarPlato(Long idPlato, Integer nuevoPrecio, String nuevaDescripcion, String correoUsuarioAutenticado) {
        if (idPlato == null) {
            throw new ValidacionException("El id del plato es obligatorio");
        }

        Plato plato = platoPersistencePort.buscarPorId(idPlato);
        if (plato == null) {
            throw new PlatoNoEncontradoException("No existe un plato con ese id");
        }

        Restaurante restaurante = restaurantePersistencePort.buscarPorId(plato.getIdRestaurante());
        validarPropietario(restaurante, correoUsuarioAutenticado);

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
    public void cambiarEstado(Long idPlato, boolean activo, String correoUsuarioAutenticado) {
        if (idPlato == null) {
            throw new ValidacionException("El id del plato es obligatorio");
        }

        Plato plato = platoPersistencePort.buscarPorId(idPlato);
        if (plato == null) {
            throw new PlatoNoEncontradoException("No existe un plato con ese id");
        }

        Restaurante restaurante = restaurantePersistencePort.buscarPorId(plato.getIdRestaurante());
        validarPropietario(restaurante, correoUsuarioAutenticado);

        plato.setActivo(activo);
        platoPersistencePort.actualizarPlato(plato);
    }

    @Override
    public List<Plato> listarPorRestaurante(Long idRestaurante) {
        return platoPersistencePort.listarPorRestaurante(idRestaurante);
    }

    private void validarPropietario(Restaurante restaurante, String correoUsuarioAutenticado) {
        Long idUsuarioAutenticado = usuarioExternoPort.obtenerIdUsuarioPorCorreo(correoUsuarioAutenticado);

        if (idUsuarioAutenticado == null || !idUsuarioAutenticado.equals(restaurante.getIdPropietario())) {
            throw new NoAutorizadoException("Este restaurante no te pertenece, no puedes administrar sus platos");
        }
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
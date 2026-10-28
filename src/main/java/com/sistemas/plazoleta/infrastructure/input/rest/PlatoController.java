package com.sistemas.plazoleta.infrastructure.input.rest;

import com.sistemas.plazoleta.application.dto.ModificarPlatoRequestDto;
import com.sistemas.plazoleta.application.dto.PlatoRequestDto;
import com.sistemas.plazoleta.application.dto.PlatoResponseDto;
import com.sistemas.plazoleta.application.handler.PlatoHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/platos")
@Tag(name = "Platos", description = "Gestion del menu de los restaurantes")
public class PlatoController {

    private final PlatoHandler platoHandler;

    public PlatoController(PlatoHandler platoHandler) {
        this.platoHandler = platoHandler;
    }

    private String correoUsuarioAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Operation(summary = "Crear un plato (requiere rol PROPIETARIO, dueno del restaurante)")
    @ApiResponse(responseCode = "201", description = "Plato creado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos o restaurante inexistente")
    @ApiResponse(responseCode = "403", description = "El restaurante no pertenece al usuario autenticado")
    @PostMapping
    public ResponseEntity<Void> crearPlato(@RequestBody PlatoRequestDto dto) {
        platoHandler.crearPlato(dto, correoUsuarioAutenticado());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Modificar precio y/o descripcion de un plato (requiere rol PROPIETARIO, dueno del restaurante)")
    @ApiResponse(responseCode = "200", description = "Plato modificado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "403", description = "El restaurante no pertenece al usuario autenticado")
    @ApiResponse(responseCode = "404", description = "El plato no existe")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> modificarPlato(@PathVariable Long id, @RequestBody ModificarPlatoRequestDto dto) {
        platoHandler.modificarPlato(id, dto, correoUsuarioAutenticado());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Habilitar un plato (requiere rol PROPIETARIO, dueno del restaurante)")
    @ApiResponse(responseCode = "200", description = "Plato habilitado")
    @ApiResponse(responseCode = "403", description = "El restaurante no pertenece al usuario autenticado")
    @ApiResponse(responseCode = "404", description = "El plato no existe")
    @PatchMapping("/{id}/habilitar")
    public ResponseEntity<Void> habilitarPlato(@PathVariable Long id) {
        platoHandler.cambiarEstado(id, true, correoUsuarioAutenticado());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deshabilitar un plato (requiere rol PROPIETARIO, dueno del restaurante)")
    @ApiResponse(responseCode = "200", description = "Plato deshabilitado")
    @ApiResponse(responseCode = "403", description = "El restaurante no pertenece al usuario autenticado")
    @ApiResponse(responseCode = "404", description = "El plato no existe")
    @PatchMapping("/{id}/deshabilitar")
    public ResponseEntity<Void> deshabilitarPlato(@PathVariable Long id) {
        platoHandler.cambiarEstado(id, false, correoUsuarioAutenticado());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar los platos de un restaurante")
    @ApiResponse(responseCode = "200", description = "Lista de platos")
    @GetMapping
    public ResponseEntity<List<PlatoResponseDto>> listarPorRestaurante(@RequestParam Long idRestaurante) {
        return ResponseEntity.ok(platoHandler.listarPorRestaurante(idRestaurante));
    }

    @Operation(summary = "HU 10: Listar el menu de un restaurante paginado, opcionalmente filtrado por categoria "
            + "(solo platos activos, en orden alfabetico)")
    @ApiResponse(responseCode = "200", description = "Pagina de platos")
    @ApiResponse(responseCode = "400", description = "Parametros de paginacion invalidos")
    @ApiResponse(responseCode = "404", description = "El restaurante no existe")
    @GetMapping("/paginado")
    public ResponseEntity<List<PlatoResponseDto>> listarPaginado(
            @Parameter(description = "Id del restaurante") @RequestParam Long idRestaurante,
            @Parameter(description = "Categoria a filtrar (opcional)") @RequestParam(required = false) String categoria,
            @Parameter(description = "Numero de pagina, empieza en 0") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Cantidad de elementos por pagina") @RequestParam(defaultValue = "10") int tamanioPagina) {
        return ResponseEntity.ok(platoHandler.listarPaginado(idRestaurante, categoria, pagina, tamanioPagina));
    }
}
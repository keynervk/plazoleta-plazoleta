package com.sistemas.plazoleta.infrastructure.input.rest;

import com.sistemas.plazoleta.application.dto.ModificarPlatoRequestDto;
import com.sistemas.plazoleta.application.dto.PlatoRequestDto;
import com.sistemas.plazoleta.application.handler.PlatoHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platos")
@Tag(name = "Platos", description = "Gestion del menu de los restaurantes")
public class PlatoController {

    private final PlatoHandler platoHandler;

    public PlatoController(PlatoHandler platoHandler) {
        this.platoHandler = platoHandler;
    }

    @Operation(summary = "Crear un plato (requiere rol PROPIETARIO, dueno del restaurante)")
    @ApiResponse(responseCode = "201", description = "Plato creado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos o restaurante inexistente")
    @PostMapping
    public ResponseEntity<Void> crearPlato(@RequestBody PlatoRequestDto dto) {
        platoHandler.crearPlato(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Modificar precio y/o descripcion de un plato (requiere rol PROPIETARIO, dueno del restaurante)")
    @ApiResponse(responseCode = "200", description = "Plato modificado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "404", description = "El plato no existe")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> modificarPlato(@PathVariable Long id, @RequestBody ModificarPlatoRequestDto dto) {
        platoHandler.modificarPlato(id, dto);
        return ResponseEntity.ok().build();
    }
}
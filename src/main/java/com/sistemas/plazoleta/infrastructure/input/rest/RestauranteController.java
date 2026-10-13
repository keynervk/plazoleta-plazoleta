package com.sistemas.plazoleta.infrastructure.input.rest;

import com.sistemas.plazoleta.application.dto.RestauranteListadoDto;
import com.sistemas.plazoleta.application.dto.RestauranteRequestDto;
import com.sistemas.plazoleta.application.dto.RestauranteResponseDto;
import com.sistemas.plazoleta.application.handler.RestauranteHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@Tag(name = "Restaurantes", description = "Gestion de restaurantes")
public class RestauranteController {

    private final RestauranteHandler restauranteHandler;

    public RestauranteController(RestauranteHandler restauranteHandler) {
        this.restauranteHandler = restauranteHandler;
    }

    @Operation(summary = "Crear un restaurante (requiere rol ADMINISTRADOR)")
    @ApiResponse(responseCode = "201", description = "Restaurante creado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "409", description = "Ya existe un restaurante con ese NIT")
    @PostMapping
    public ResponseEntity<Void> guardarRestaurante(@RequestBody RestauranteRequestDto dto) {
        restauranteHandler.guardarRestaurante(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Listar todos los restaurantes (sin paginar, campos completos)")
    @ApiResponse(responseCode = "200", description = "Lista de restaurantes")
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDto>> listarRestaurantes() {
        return ResponseEntity.ok(restauranteHandler.listarRestaurantes());
    }

    @Operation(summary = "HU 9: Listar restaurantes paginados y en orden alfabetico (solo Nombre y UrlLogo)")
    @ApiResponse(responseCode = "200", description = "Pagina de restaurantes")
    @ApiResponse(responseCode = "400", description = "Parametros de paginacion invalidos")
    @GetMapping("/paginado")
    public ResponseEntity<List<RestauranteListadoDto>> listarPaginado(
            @Parameter(description = "Numero de pagina, empieza en 0") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Cantidad de elementos por pagina") @RequestParam(defaultValue = "10") int tamanioPagina) {
        return ResponseEntity.ok(restauranteHandler.listarPaginado(pagina, tamanioPagina));
    }
}
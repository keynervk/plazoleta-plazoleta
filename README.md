# Microservicio Plazoleta

Restaurantes y platos de la plazoleta de comidas (HU 2, 3, 4, 7, 9 y 10). Puerto **8082**, base de datos
**`plazoletadb`**.

Arquitectura hexagonal: `domain` (Java puro, sin Spring: modelos, casos de uso y puertos), `application`
(DTOs, mappers y handlers) e `infrastructure` (REST, JPA, seguridad JWT y cliente HTTP a Usuarios).
Nunca accede a la base de datos de Usuarios: todo lo consulta por HTTP. Java 17 y Spring Boot 4.

## Cómo levantarlo

1. Crear la base de datos (Postgres no la crea sola): `CREATE DATABASE plazoletadb;`
2. Variables de entorno: `DB_PASSWORD` (obligatoria) y `JWT_SECRET` (opcional; debe ser **la misma** que en
   `usuarios` y `pedidos`, el valor por defecto ya coincide).
3. Levantar `usuarios` (8081) y luego `./mvnw spring-boot:run`.
4. Swagger: <http://localhost:8082/swagger-ui.html>. Inicia sesión con `POST http://localhost:8081/usuarios/login`
   y pega el token en **Authorize**.

Pruebas unitarias: `./mvnw test`.

## Endpoints

| HU | Método y ruta | Acceso | Qué hace |
|----|---------------|--------|----------|
| 2 | `POST /restaurantes` | ADMINISTRADOR | Crea un restaurante para un usuario con rol PROPIETARIO |
| 9 | `GET /restaurantes/paginado?pagina=0&tamanioPagina=10` | Público | Restaurantes en orden alfabético, solo `nombre` y `urlLogo` |
| — | `GET /restaurantes` | Público | Todos los restaurantes con todos sus campos |
| — | `GET /restaurantes/{id}` | Público | Un restaurante; lo usan Usuarios, Pedidos y el frontend |
| 3 | `POST /platos` | PROPIETARIO dueño | Crea un plato, activo por defecto |
| 4 | `PATCH /platos/{id}` | PROPIETARIO dueño | Modifica solo `precio` y/o `descripcion` |
| 7 | `PATCH /platos/{id}/habilitar` y `/deshabilitar` | PROPIETARIO dueño | Activa o desactiva el plato en el menú |
| 10 | `GET /platos/paginado?idRestaurante=1&categoria=Bebidas&pagina=0&tamanioPagina=10` | Público | Menú paginado: solo platos activos, por nombre; `categoria` es opcional y no distingue mayúsculas |
| — | `GET /platos?idRestaurante=1` | Público | Todos los platos del restaurante, activos e inactivos |

Bodies:

```jsonc
// POST /restaurantes
{ "nombre": "Pizza Buena", "nit": "900123456", "direccion": "Calle 10 # 20-30",
  "telefono": "+573005551234", "urlLogo": "https://...", "idPropietario": 2 }

// POST /platos
{ "nombre": "Hamburguesa clasica", "precio": 15000, "descripcion": "Carne, queso y vegetales",
  "urlImagen": "https://...", "categoria": "Comida rapida", "idRestaurante": 1 }

// PATCH /platos/{id}
{ "precio": 18000, "descripcion": "Nueva descripcion" }
```

Los errores siguen el formato común `{ "status": <código>, "mensaje": "..." }`: 400 (datos inválidos o
paginación inválida), 401 (sin token o token inválido), 403 (rol incorrecto o restaurante ajeno),
404 (restaurante o plato inexistente) y 409 (NIT ya registrado).

## Reglas de negocio

- **Restaurante**: nombre, NIT, dirección, teléfono, URL del logo e id del propietario obligatorios. NIT
  solo numérico y único; teléfono de máximo 13 caracteres que puede empezar por `+`; el nombre puede tener
  números pero no ser solo números; `idPropietario` debe ser un usuario con rol PROPIETARIO.
- **Plato**: nombre, precio, descripción, URL de imagen, categoría y restaurante obligatorios. Precio entero
  mayor a 0. Todo plato nuevo queda `activo = true`.
- Solo el **propietario dueño del restaurante** puede crear, modificar, habilitar o deshabilitar sus platos.

## Decisiones de diseño

- **Quién es el dueño**: el token trae el correo del usuario; Plazoleta pide su id a Usuarios
  (`GET /usuarios/id-por-correo`) y lo compara con el `idPropietario` del restaurante.
- **Rol del propietario al crear restaurante**: se valida con `GET /usuarios/{id}/es-propietario`.
- **Consultas públicas**: listar restaurantes y platos no exige token, para que cualquiera vea el menú y para
  que los otros microservicios puedan consultarlos.
- **Paginación**: se hace en la base de datos (`PageRequest` ordenado por nombre) y se devuelve solo la lista
  de la página pedida.
- **CORS**: se permite el frontend en `http://localhost:5173`.

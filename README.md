# Practica de Spring WebFlux

Java 21. API de lectura con datos en memoria: no requiere una base de datos.
Se retiraron los starters R2DBC porque no hay un driver ni una conexion configurados.

## Ejecutar

Configura JAVA_HOME con tu JDK 21 y recarga Maven en IntelliJ.

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

Tambien puedes ejecutar InterviewApplication desde IntelliJ.

## Capas

`controller -> service -> repository -> memory`

- controller: rutas HTTP y traduccion de errores a ProblemDetail (400/404).
- service: validacion y composicion reactiva con flatMap.
- repository: contratos de acceso a datos.
- repository/memory: implementaciones de ejemplo, reemplazables por persistencia.
- model: records Cliente y Cuenta, con accesores id(), nombre(), saldo().

## Probar

| GET | Resultado |
| --- | --- |
| http://localhost:8080/api/clientes | 200: Ana y Luis |
| http://localhost:8080/api/clientes/1 | 200: Ana |
| http://localhost:8080/api/clientes/1/cuenta | 200: cuenta ES001 |
| http://localhost:8080/api/clientes/2/cuenta | 404: cliente sin cuenta |
| http://localhost:8080/api/clientes/99/cuenta | 404: cliente inexistente |
| http://localhost:8080/api/clientes/0/cuenta | 400: ID invalido |

```powershell
Invoke-RestMethod http://localhost:8080/api/clientes/1/cuenta
```

## Ejercicios

1. Agregar busqueda de clientes por nombre con filter.
2. Crear un DTO de respuesta y transformarlo con map.
3. Permitir multiples cuentas por cliente y devolver Flux<Cuenta>.
4. Agregar POST de clientes: usar almacenamiento concurrente, generar IDs y validar entradas.
5. Agregar pruebas de cada nuevo comportamiento antes de cambiar persistencia.
6. Reemplazar memoria por R2DBC con el starter de Spring Data, driver y conexion apropiados.

No usar block() ni subscribe() dentro de controller/service: WebFlux realiza la suscripcion.
Los datos son demostrativos; esta API no implementa autenticacion ni operaciones bancarias reales.

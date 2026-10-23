# Practica de Spring WebFlux

Java 21. API de lectura con datos en memoria: no requiere una base de datos.
El perfil postgres habilita R2DBC para consultar transacciones en PostgreSQL.
Sin ese perfil, se puede practicar con clientes y cuentas en memoria.

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
 
## PostgreSQL local en Docker

El contenedor debe publicar el puerto 5432 al host. Ejecuta la aplicacion desde Windows.
La tabla existente public.transacciones se consulta sin crear, borrar ni insertar datos.
Clientes y cuentas siguen siendo ejemplos en memoria; no hay tablas equivalentes en la captura.

```powershell
$env:DB_PASSWORD = '<tu password local>'
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.profiles=postgres'
```

En IntelliJ: activa el perfil postgres (o argumento --spring.profiles.active=postgres)
y agrega DB_PASSWORD en las variables de entorno de la configuracion de ejecucion.
Valores por defecto: localhost:5432, database practica, usuario postgres.
Puedes sobrescribir DB_URL y DB_USER. La clave no se guarda en el repositorio.

GET http://localhost:8080/api/transacciones?limite=20&despuesDeId=0

Para la siguiente pagina usa como despuesDeId el ultimo ID recibido.
El limite permitido es de 1 a 100. Un cursor sin resultados devuelve [].
La consulta usa la clave primaria id y no carga el millon de filas en memoria.

Pruebas optativas de lectura contra los datos del ejercicio (esperan IDs 1, 2 y 3):

```powershell
$env:RUN_POSTGRES_TESTS = 'true'
.\mvnw.cmd test
Remove-Item Env:RUN_POSTGRES_TESTS
```

Si ejecutas la API dentro de otro contenedor, localhost apunta a ese contenedor:
configura DB_URL con el nombre del servicio PostgreSQL en la red Docker.
Los datos son demostrativos; esta API no implementa autenticacion ni operaciones bancarias reales.

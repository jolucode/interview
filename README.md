# Practica Spring WebFlux + PostgreSQL

Java 21. Todas las consultas de clientes, cuentas y transacciones usan PostgreSQL mediante R2DBC.
No hay repositorios en memoria ni datos de ejemplo embebidos en el codigo de la aplicacion.

## Arrancar

El perfil por defecto es postgres. El contenedor debe publicar el puerto 5432.
Recarga Maven en IntelliJ y configura DB_PASSWORD en las variables de entorno de InterviewApplication.
Ejecuta InterviewApplication, o desde PowerShell:

```powershell
$env:DB_PASSWORD = '<tu password local>'
.\mvnw.cmd spring-boot:run
```

Conexion por defecto: r2dbc:postgresql://localhost:5432/practica, usuario postgres.
DB_URL y DB_USER permiten cambiar estos valores. La clave no se almacena en el repositorio.
Si arrancas desde IntelliJ, la variable de su terminal no se propaga al boton Run:
configurala en Run > Edit Configurations > InterviewApplication > Environment variables.
Reinicia cualquier instancia anterior de la API para cargar estos cambios.

## Capas

Controller -> Service -> Repository -> PostgreSQL.
Los servicios conservan las interfaces ClienteRepository y CuentaRepository.
PostgresClienteRepository y PostgresCuentaRepository implementan consultas SQL con parametros.
Cliente incluye id, nombre, email. Cuenta incluye id, clienteId, saldo.
Transaccion incluye id, clienteId, monto, fecha. No se usa block() ni subscribe() en las capas.

## Endpoints GET

- /api/clientes: clientes de public.clientes, ordenados por id.
- /api/clientes/1: cliente con ID 1; 404 si no existe.
- /api/clientes/1/cuenta: cuenta de public.cuentas para ese cliente; 404 si falta cliente o cuenta.
- /api/transacciones?limite=20&despuesDeId=0: pagina de transacciones.

En transacciones, usa el ultimo ID recibido como cursor de la pagina siguiente.
limite debe estar entre 1 y 100. Un cursor sin resultados devuelve [].
Los IDs de clientes deben ser positivos (400 en caso contrario).
La lista de clientes todavia no tiene paginacion; puedes agregarla como ejercicio.

## Pruebas contra la base local

```powershell
$env:DB_PASSWORD = '<tu password local>'
$env:RUN_POSTGRES_TESTS = 'true'
.\mvnw.cmd clean test
```

Las pruebas solo leen datos. Esperan los datos del ejercicio: clientes 1/2/3
(Jose Becerra, Carlos Perez, Maria Lopez), saldos 15000.50/8500.00/25000.75,
cliente 99 inexistente y transacciones con IDs 1/2/3.
Sin RUN_POSTGRES_TESTS=true se omiten estas pruebas de integracion.
No se crean tablas ni se insertan o eliminan filas automaticamente.

## Docker

localhost corresponde a Windows al ejecutar la API desde IntelliJ.
Si la API tambien corre en Docker, usa en DB_URL el nombre del servicio PostgreSQL en su red.

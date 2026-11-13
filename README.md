
# Sistema de Entregas y Mensajería

## Descripción
Proyecto académico desarrollado con Spring Boot para gestionar clientes, repartidores, paquetes y entregas mediante una API REST.

## Objetivo
Organizar y facilitar la administración de los servicios de mensajería.

## Tecnologías utilizadas
- Java 21
- Spring Boot
- Maven
- MySQL 8
- Spring Data JPA
- Git y GitHub

## Estructura del proyecto
- **entity:** entidades del sistema.
- **repository:** acceso a la base de datos.
- **service:** lógica de negocio.
- **controller:** controladores REST.
- **dto:** objetos de transferencia de datos.
- **exception:** manejo de excepciones.
- **interceptor:** supervisión de solicitudes HTTP.
- **aspect:** registro de operaciones y medición de tiempos.
- **config:** configuración de la aplicación.

## Configuración de la base de datos
Crear la base de datos en MySQL:

```sql
CREATE DATABASE sistema_mensajeria;
```

Configurar las variables de entorno:
- DB_USERNAME
- DB_PASSWORD

## Ejecución del proyecto
En la terminal ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación utiliza el puerto 8080.

## Componentes implementados
**Interceptor HTTP:** registra el método, la ruta y el código de respuesta de las solicitudes.

**Spring AOP:** registra las operaciones de la capa de servicios y mide su tiempo de ejecución.

**Config:** integra el interceptor con Spring Boot.

## Integración continua
GitHub Actions automatiza la compilación y ejecución de pruebas del proyecto, utilizando MySQL para las pruebas.

## Equipo de trabajo
Proyecto desarrollado en equipo para la asignatura de Programación Avanzada.

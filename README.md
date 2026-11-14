# Sistema de Entregas y Mensajería

## 1. Descripción

Proyecto académico desarrollado con Spring Boot para gestionar clientes, repartidores, paquetes y entregas mediante una API REST.

## 2. Objetivo

Organizar y facilitar la administración de los servicios de mensajería mediante una aplicación que permita registrar clientes, administrar paquetes, asignar repartidores y controlar el estado de las entregas.

## 3. Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.2
- Maven
- MySQL 8
- Spring Data JPA
- Spring Web MVC
- Spring Validation
- Spring AOP
- Lombok
- Git y GitHub
- GitHub Actions
- Postman
- JUnit 5 y Mockito

## 4. Estructura del proyecto

- **entity:** entidades del sistema.
- **repository:** acceso a la base de datos.
- **service:** lógica de negocio.
- **controller:** controladores REST.
- **dto:** objetos de transferencia de datos.
- **exception:** manejo de excepciones.
- **interceptor:** supervisión de solicitudes HTTP.
- **aspect:** registro de operaciones y medición de tiempos.
- **config:** configuración de la aplicación.

## 5. Endpoints de la API REST

### Clientes

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/v1/clientes` | Registrar cliente |
| GET | `/api/v1/clientes` | Listar clientes |
| GET | `/api/v1/clientes/{id}` | Consultar cliente |
| PUT | `/api/v1/clientes/{id}` | Actualizar cliente |
| DELETE | `/api/v1/clientes/{id}` | Eliminar cliente |

### Paquetes

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/v1/paquetes` | Registrar paquete |
| GET | `/api/v1/paquetes` | Listar paquetes |
| GET | `/api/v1/paquetes/{id}` | Consultar paquete |
| PUT | `/api/v1/paquetes/{id}` | Actualizar paquete |
| DELETE | `/api/v1/paquetes/{id}` | Eliminar paquete |

### Repartidores

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/v1/repartidores` | Registrar repartidor |
| GET | `/api/v1/repartidores` | Listar repartidores |
| GET | `/api/v1/repartidores/{id}` | Consultar repartidor |
| PUT | `/api/v1/repartidores/{id}` | Actualizar repartidor |
| DELETE | `/api/v1/repartidores/{id}` | Eliminar repartidor |

### Entregas

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/v1/entregas` | Registrar entrega |
| GET | `/api/v1/entregas` | Listar entregas |
| GET | `/api/v1/entregas/{id}` | Consultar entrega |
| GET | `/api/v1/entregas/pendientes` | Listar entregas pendientes |
| PATCH | `/api/v1/entregas/{id}/estado` | Actualizar estado |
| PATCH | `/api/v1/entregas/{id}/repartidor/{repartidorId}` | Asignar repartidor |
| DELETE | `/api/v1/entregas/{id}` | Eliminar entrega |

## 6. Reglas de negocio

El sistema implementa las siguientes reglas:

1. Toda entrega nueva comienza con estado `PENDIENTE` si no se especifica otro estado.
2. Únicamente se pueden asignar repartidores que estén disponibles.
3. Una entrega pendiente puede pasar a `EN_CAMINO` y posteriormente a `ENTREGADO`.
4. Una entrega completada no puede volver a otro estado.
5. Cuando una entrega cambia a estado `ENTREGADO`, se registra automáticamente la fecha y hora de entrega.
6. No se puede asignar un repartidor a una entrega que ya esté completada.

## 7. Configuración de la base de datos

Crear una base de datos en MySQL:

```sql
CREATE DATABASE sistema_mensajeria;
```

Configurar las siguientes variables de entorno:

- `DB_USERNAME`: usuario de MySQL.
- `DB_PASSWORD`: contraseña de MySQL.

El archivo `application.yml` utiliza estas variables para establecer la conexión con la base de datos.

## 8. Ejecución del proyecto

Clonar el repositorio:

```bash
git clone https://github.com/eddstark94/SistemaMensajeria.git
```

Ingresar a la carpeta:

```bash
cd SistemaMensajeria
```

Ejecutar la aplicación mediante Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación utiliza el puerto **8080**.

## 9. Componentes implementados

### Interceptor HTTP

Registra información de las solicitudes HTTP, incluyendo:

- Método HTTP.
- Ruta solicitada.
- Código de respuesta.

### Spring AOP

Registra las operaciones ejecutadas en la capa de servicios y mide su tiempo de ejecución.

### Manejo de excepciones

Se implementan excepciones personalizadas y un manejador centralizado para responder ante errores como:

- Recursos no encontrados.
- Reglas de negocio incumplidas.
- Errores de validación.
- Conflictos de integridad de datos.

## 10. Pruebas automatizadas

El proyecto cuenta con pruebas automatizadas utilizando JUnit 5, Mockito y Spring Boot Test.

Se verifican los siguientes componentes:

- Servicios.
- Controladores REST.
- Validaciones.
- Reglas de negocio.
- Interceptor HTTP.
- Aspecto AOP.
- Persistencia en MySQL.

**Resultado de las pruebas realizadas: 68 pruebas aprobadas.**

## 11. Integración continua

El proyecto utiliza GitHub Actions para automatizar la compilación y ejecución de las pruebas.

El flujo de integración continua ejecuta:

```bash
mvn -B clean verify
```

Se utiliza un servicio MySQL 8.0 durante la ejecución de las pruebas automatizadas.

## 12. Equipo de trabajo

Proyecto desarrollado para la asignatura de Programación Avanzada.

- Johanna Tatiana Cano Papa
- Edison Fidel Castillo Parra
- Esteban Andrés Chiliquinga Torres
- William Gonzalo Solórzano Pilaquinga

**Grupo 12 - ESPE**
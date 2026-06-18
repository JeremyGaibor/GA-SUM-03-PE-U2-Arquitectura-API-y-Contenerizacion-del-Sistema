# TiendaTech Microservicios

Proyecto correspondiente a la **Práctica Experimental Unidad 2: Contenerización y Despliegue de Microservicios**.

El sistema implementa una arquitectura basada en microservicios para el PFC **TiendaTech**, utilizando **Spring Boot**, **PostgreSQL**, **Docker**, **Docker Compose**, **Nginx como API Gateway** y **Portainer como herramienta de monitoreo**.

---

## Integrantes

* Gaibor
* Farinango
* Villamarin

---

## Arquitectura general

El sistema está dividido en tres microservicios principales:

| Microservicio          | Puerto | Responsabilidad                            |
| ---------------------- | -----: | ------------------------------------------ |
| `auth-service`         | `8001` | Registro, login y validación de tokens JWT |
| `resource-service`     | `8002` | Gestión CRUD de productos                  |
| `notification-service` | `8003` | Registro de eventos/notificaciones         |

Además, se incluyen los siguientes servicios de infraestructura:

| Servicio          | Puerto | Descripción                                |
| ----------------- | -----: | ------------------------------------------ |
| `nginx-gateway`   | `8080` | API Gateway para enrutar las peticiones    |
| `portainer`       | `9000` | Monitoreo y administración de contenedores |
| `auth-db`         | `5433` | Base PostgreSQL para auth-service          |
| `resource-db`     | `5434` | Base PostgreSQL para resource-service      |
| `notification-db` | `5435` | Base PostgreSQL para notification-service  |

---

## Estructura del proyecto

```text
tiendatech-microservicios/
├── auth-service/
├── resource-service/
├── notification-service/
├── gateway/
│   └── nginx.conf
├── docs/
│   └── api/
│       ├── auth-service-openapi.yaml
│       ├── resource-service-openapi.yaml
│       └── notification-service-openapi.yaml
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

---

## Tecnologías utilizadas

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL 16
* Docker
* Docker Compose
* Nginx
* Portainer
* OpenAPI 3.0
* Postman

---

## Variables de entorno

El proyecto utiliza un archivo `.env` para configurar credenciales y URLs internas.

No se debe subir el archivo `.env` al repositorio.
En su lugar, se incluye el archivo `.env.example`.

Ejemplo:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=1234

AUTH_DB_URL=jdbc:postgresql://auth-db:5432/auth_db
RESOURCE_DB_URL=jdbc:postgresql://resource-db:5432/resource_db
NOTIFICATION_DB_URL=jdbc:postgresql://notification-db:5432/notification_db

JWT_SECRET=tiendatech_secret_key_2026_microservicios_segura

NOTIFICATION_SERVICE_URL=http://notification-service:8003
```

---

## Ejecución del sistema

Desde la raíz del proyecto ejecutar:

```bash
docker compose up --build
```

Para detener los contenedores:

```bash
docker compose down
```

Para reconstruir completamente:

```bash
docker compose down
docker compose up --build
```

---

## Accesos principales

| Servicio                     | URL                     |
| ---------------------------- | ----------------------- |
| API Gateway                  | `http://localhost:8080` |
| Auth Service directo         | `http://localhost:8001` |
| Resource Service directo     | `http://localhost:8002` |
| Notification Service directo | `http://localhost:8003` |
| Portainer                    | `http://localhost:9000` |

---

## Rutas por API Gateway

Todas las pruebas principales se realizan usando el API Gateway:

```text
http://localhost:8080
```

| Ruta Gateway         | Servicio destino            |
| -------------------- | --------------------------- |
| `/api/auth/*`        | `auth-service:8001`         |
| `/api/resources/*`   | `resource-service:8002`     |
| `/api/notifications` | `notification-service:8003` |

---

## Endpoints principales

### Auth Service

#### Registrar usuario

```http
POST http://localhost:8080/api/auth/register
```

Body:

```json
{
  "nombre": "Jeremy Gaibor",
  "email": "jeremy@tiendatech.com",
  "password": "1234",
  "rol": "ADMIN"
}
```

Respuesta esperada:

```json
{
  "status": "success",
  "message": "Usuario registrado correctamente",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

#### Iniciar sesión

```http
POST http://localhost:8080/api/auth/login
```

Body:

```json
{
  "email": "admin@tiendatech.com",
  "password": "1234"
}
```

Respuesta esperada:

```json
{
  "status": "success",
  "message": "Login correcto",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

#### Validar JWT

```http
GET http://localhost:8080/api/auth/validate
```

Header:

```http
Authorization: Bearer TU_TOKEN
```

Respuesta esperada:

```json
{
  "status": "success",
  "valid": true
}
```

---

### Resource Service

Los endpoints de productos requieren JWT válido.

Header requerido:

```http
Authorization: Bearer TU_TOKEN
```

---

#### Listar productos

```http
GET http://localhost:8080/api/resources/productos
```

---

#### Buscar producto por ID

```http
GET http://localhost:8080/api/resources/productos/1
```

---

#### Crear producto

```http
POST http://localhost:8080/api/resources/productos
```

Body:

```json
{
  "nombre": "Memoria RAM 122GB DDR4",
  "categoria": "RAM",
  "marca": "Intel",
  "precio": 45.99,
  "stock": 20
}
```

Al crear un producto, el sistema envía un evento al `notification-service`.

---

#### Actualizar producto

```http
PUT http://localhost:8080/api/resources/productos/1
```

Body:

```json
{
  "nombre": "Memoria RAM 122GB DDR4 Actualizada",
  "categoria": "RAM",
  "marca": "Intel",
  "precio": 49.99,
  "stock": 25
}
```

---

#### Eliminar producto

```http
DELETE http://localhost:8080/api/resources/productos/1
```

---

### Notification Service

#### Listar notificaciones

```http
GET http://localhost:8080/api/notifications
```

Respuesta esperada:

```json
[
  {
    "id": 1,
    "evento": "PRODUCTO_CREADO",
    "mensaje": "Se creo el producto: Memoria RAM 122GB DDR4",
    "fecha": "2026-06-17T02:30:52"
  }
]
```

---

#### Crear notificación manual

```http
POST http://localhost:8080/api/notifications
```

Body:

```json
{
  "evento": "PRUEBA",
  "mensaje": "Notificacion registrada manualmente"
}
```

---

## Flujo de prueba recomendado en Postman

### Flujo 1: autenticación

1. `POST /api/auth/register`
2. `POST /api/auth/login`
3. Copiar el token JWT retornado
4. `GET /api/auth/validate` usando el header `Authorization: Bearer TOKEN`

---

### Flujo 2: productos protegidos con JWT

1. Intentar `GET /api/resources/productos` sin token
2. El sistema debe rechazar la solicitud
3. Repetir la petición con el header:

```http
Authorization: Bearer TU_TOKEN
```

4. El sistema debe retornar la lista de productos

---

### Flujo 3: creación de producto y notificación

1. `POST /api/resources/productos` con JWT
2. Crear un producto
3. `GET /api/notifications`
4. Verificar que se registró un evento `PRODUCTO_CREADO`

---

## Contratos OpenAPI

Los contratos OpenAPI 3.0 se encuentran en:

```text
docs/api/
```

Archivos:

```text
auth-service-openapi.yaml
resource-service-openapi.yaml
notification-service-openapi.yaml
```

Estos archivos pueden validarse en Swagger Editor:

```text
https://editor.swagger.io/
```

---

## Docker y contenedores

El sistema crea varios contenedores porque cada microservicio se ejecuta de manera independiente y cada uno tiene su propia base de datos.

Contenedores principales:

```text
tiendatech-auth-service
tiendatech-resource-service
tiendatech-notification-service
tiendatech-auth-db
tiendatech-resource-db
tiendatech-notification-db
tiendatech-nginx-gateway
tiendatech-portainer
```

Esto cumple con la arquitectura solicitada de microservicios contenerizados.

---

## Dockerfiles multi-stage

Cada microservicio cuenta con un Dockerfile multi-stage:

1. **Etapa de compilación:** usa Maven con Java 17 para construir el `.jar`.
2. **Etapa de ejecución:** usa una imagen liviana de Java JRE para ejecutar el `.jar`.

Esto permite separar el entorno de construcción del entorno de ejecución.

---

## Distribución de trabajo

| Integrante | Responsabilidad                                                           |
| ---------- | ------------------------------------------------------------------------- |
| Gaibor     | `auth-service`, registro, login, JWT y validación de token                |
| Farinango  | `resource-service`, CRUD de productos y protección con JWT                |
| Villamarin | `notification-service`, Docker, Nginx, Portainer, OpenAPI y documentación |

---

## Ramas de trabajo

| Integrante | Rama         |
| ---------- | ------------ |
| Gaibor     | `Gaibor`     |
| Farinango  | `Farinango`  |
| Villamarin | `Villamarin` |

Antes de realizar cambios, cada integrante debe actualizar su rama con `main`:

```bash
git checkout main
git pull origin main
git checkout NOMBRE_RAMA
git merge main
```

Después de realizar cambios:

```bash
git add .
git commit -m "mensaje del commit"
git push origin NOMBRE_RAMA
```

---

## Evidencias recomendadas

Para la entrega se recomienda incluir capturas de:

* Docker Desktop o Portainer con todos los contenedores activos.
* `docker compose up --build` ejecutándose correctamente.
* `POST /api/auth/register`.
* `POST /api/auth/login`.
* `GET /api/auth/validate`.
* `GET /api/resources/productos` protegido con JWT.
* `POST /api/resources/productos` creando producto.
* `GET /api/notifications` mostrando el evento generado.
* Carpeta `docs/api/` con los contratos OpenAPI.
* Archivo `docker-compose.yml`.
* Archivo `gateway/nginx.conf`.

---

## Estado del proyecto

El sistema cumple con la contenerización solicitada, implementando tres microservicios funcionales, bases de datos independientes, API Gateway, servicio de monitoreo, variables de entorno, contratos OpenAPI y endpoints demostrables mediante Postman.

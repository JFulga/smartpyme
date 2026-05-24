# SmartP - API de Tienda con Spring Boot

API REST para gestión de productos, ventas y autenticación con JWT y roles.

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- Lombok
- Maven
- Postman

---

## Funcionalidades

### Autenticación
- Login con JWT
- Generación de token
- Protección de endpoints con Bearer Token

### Roles
- ADMIN
- VENDEDOR

Permisos:

- ADMIN:
    - Gestionar productos
    - Gestionar ventas

- VENDEDOR:
    - Gestionar ventas

---

## Endpoints principales

### Auth

#### Login
```http
POST /auth/login
```

Body:

```json
{
  "username": "admin",
  "password": "1234"
}
```

Response:

```json
{
  "token": "jwt_token"
}
```

---

## Productos

### Obtener productos
```http
GET /api/productos
```

### Crear producto
```http
POST /api/productos
```

### Actualizar producto
```http
PUT /api/productos/{id}
```

### Eliminar producto
```http
DELETE /api/productos/{id}
```

Requiere rol:
- ADMIN

---

## Ventas

### Crear venta
```http
POST /api/ventas
```

### Obtener ventas
```http
GET /api/ventas
```

Requiere rol:
- ADMIN
- VENDEDOR

---

## Seguridad

La API usa autenticación JWT.

Agregar token en headers:

```http
Authorization: Bearer tu_token
```

---

## Cómo ejecutar el proyecto

### Clonar repositorio

```bash
git clone https://github.com/tuusuario/smartP.git
```

### Entrar al proyecto

```bash
cd smartP
```

### Configurar base de datos

Editar `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smartp
spring.datasource.username=root
spring.datasource.password=tu_password
```

### Ejecutar

```bash
mvn spring-boot:run
```

---

## Base de datos

Tablas principales:

- users
- productos
- ventas
- factura
- factura_detalle

---

## Autor

Desarrollado por Jeferson Fulga Barajas
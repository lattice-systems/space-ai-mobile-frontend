# 📡 SpaceIA Mobile — API Contracts

> Versión: 1.1.0
>
> Aplicación: SpaceIA Mobile (Android)
>
> Backend: ASP.NET Core 10
>
> Microservicio IA: FastAPI + LangChain + ChromaDB

---

# 🌐 Entornos

## Backend Principal (.NET)

```text
http://<backend-url>/api
```

## Microservicio IA (FastAPI)

```text
http://<ia-url>
```

---

# 🔐 Autenticación

La aplicación utiliza autenticación basada en JWT.

Después de iniciar sesión correctamente se deben almacenar:

* Access Token
* Refresh Token
* Información del usuario

---

# 👤 Módulo de Identidad y Seguridad

## Registro de Usuario

### Endpoint

```http
POST /auth/register
```

### Request

```json
{
  "name": "Juan Pablo Rea Cano",
  "email": "juan.rea@utleon.edu.mx",
  "password": "PasswordSegura123!",
  "folio": "23001501",
  "role": "student"
}
```

### Roles Disponibles

| Valor   | Descripción   |
| ------- | ------------- |
| student | Alumno        |
| teacher | Docente       |
| admin   | Administrador |
| visitor | Visitante     |

### Response

```json
{
  "message": "Usuario registrado exitosamente",
  "userId": "uuid-1234-5678"
}
```

---

## Inicio de Sesión

### Endpoint

```http
POST /auth/login
```

### Request

```json
{
  "email": "juan.rea@utleon.edu.mx",
  "password": "PasswordSegura123!"
}
```

### Response

```json
{
  "accessToken": "jwt_token",
  "refreshToken": "refresh_token",
  "expiresIn": 3600,
  "user": {
    "id": "uuid",
    "name": "Juan Pablo Rea Cano",
    "email": "juan.rea@utleon.edu.mx",
    "folio": "23001501",
    "role": "student"
  }
}
```

### Error

```json
{
  "error": "Credenciales inválidas"
}
```

### Códigos HTTP

| Código | Significado            |
| ------ | ---------------------- |
| 200    | Login correcto         |
| 401    | Credenciales inválidas |

---

# 🎓 Perfil de Usuario

Información obtenida después del Login.

## Modelo User

```json
{
  "id": "uuid",
  "name": "Juan Pablo Rea Cano",
  "email": "juan.rea@utleon.edu.mx",
  "folio": "23001501",
  "role": "student"
}
```

---

# 🔳 Identidad Digital QR

## Obtener QR Dinámico

Genera el token cifrado que será convertido a QR por la aplicación móvil.

### Endpoint

```http
GET /auth/qr/{userId}
```

### Headers

```http
Authorization: Bearer <accessToken>
```

### Response

```json
{
  "qrToken": "enc_abc123",
  "generatedAt": "2026-08-15T10:00:00Z",
  "expiresAt": "2026-08-15T10:15:00Z"
}
```

### Reglas

* Vigencia máxima: 15 minutos.
* Debe mostrarse un contador regresivo.
* Debe regenerarse al expirar.

---

# 🤖 Microservicio IA

## Health Check

Permite verificar disponibilidad del servicio.

### Endpoint

```http
GET /health
```

### Response

```json
{
  "status": "healthy",
  "database": "connected"
}
```

---

# 📚 Base de Conocimiento

## Ingesta de Documentos

Subida de PDFs institucionales.

### Endpoint

```http
POST /api/knowledge/ingest
```

### Content-Type

```http
multipart/form-data
```

### Request

```text
file: reglamento_2026.pdf
```

### Response

```json
{
  "message": "Documento procesado correctamente",
  "documentName": "reglamento_2026.pdf",
  "chunksCreated": 45
}
```

---

# 🧠 Consulta Inteligente (RAG)

Permite realizar consultas al asistente institucional.

### Endpoint

```http
POST /api/knowledge/ask
```

### Request

```json
{
  "question": "¿Dónde se encuentra la biblioteca?"
}
```

### Response

```json
{
  "answer": "La biblioteca se encuentra junto al edificio académico principal.",
  "sources": [
    {
      "documentName": "guia_campus.pdf",
      "page": 3
    }
  ]
}
```

---

# 📍 Navegación Inteligente

## Casos de Uso

La App consumirá la respuesta del motor IA para:

* Encontrar edificios.
* Encontrar oficinas.
* Encontrar laboratorios.
* Encontrar servicios universitarios.
* Guiar al usuario mediante el mapa.

---

# 🚗 Robot de Guiado Autónomo

## Estados Disponibles

```text
AVAILABLE
GUIDING
OUT_OF_SERVICE
EMERGENCY
```

Estos estados podrán visualizarse en:

* Home
* Mapa
* Seguimiento del Robot

---

# 🔔 Convenciones Generales

## Authorization Header

```http
Authorization: Bearer <jwt>
```

---

## Content-Type

```http
application/json
```

---

# 📦 Modelos Kotlin Recomendados

## LoginRequest

```kotlin
data class LoginRequest(
    val email: String,
    val password: String
)
```

## LoginResponse

```kotlin
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: UserDto
)
```

## UserDto

```kotlin
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val folio: String?,
    val role: String
)
```

## QrResponse

```kotlin
data class QrResponse(
    val qrToken: String,
    val generatedAt: String,
    val expiresAt: String
)
```

---

# 🚀 Roadmap de Integraciones

### Sprint 1

* Login
* Registro
* JWT
* QR Dinámico

### Sprint 2

* Perfil
* Dashboard
* Mapa

### Sprint 3

* IA (RAG)
* Chat institucional

### Sprint 4

* Robot Autónomo
* Navegación inteligente

### Sprint 5

* Analítica
* IoT
* MQTT

```
```

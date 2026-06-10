# SpaceIA Mobile 📱🎓

![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF?style=for-the-badge\&logo=kotlin\&logoColor=white)
![Android](https://img.shields.io/badge/Android-35-3DDC84?style=for-the-badge\&logo=android\&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge\&logo=jetpackcompose\&logoColor=white)
![Material 3](https://img.shields.io/badge/Material_3-6750A4?style=for-the-badge\&logo=materialdesign\&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-Dagger-F57C00?style=for-the-badge)
![MVVM](https://img.shields.io/badge/MVVM-Clean_Architecture-0A66C2?style=for-the-badge)

Bienvenido a **SpaceIA Mobile**, la aplicación oficial del ecosistema **Smart Campus SpaceIA**.

La aplicación permite a estudiantes, docentes, visitantes y administradores interactuar con los servicios inteligentes del campus mediante tecnologías de Inteligencia Artificial, Navegación Inteligente, Identidad Digital, IoT y Automatización.

---

# 👨‍💻 Equipo de Desarrollo

* **Desarrollador Principal:** Juan Pablo Rea Cano
* **Equipo de Desarrollo:** Lattice Systems
* **Proyecto:** SpaceIA — Smart Campus Ecosystem
* **Universidad:** Universidad Tecnológica de León

---

# 🚀 Tecnologías Utilizadas

La aplicación móvil está construida utilizando las tecnologías más modernas del ecosistema Android.

| Tecnología             | Propósito                 |
| ---------------------- | ------------------------- |
| Kotlin 2.x             | Lenguaje principal        |
| Jetpack Compose        | UI Declarativa            |
| Material Design 3      | Sistema de Diseño         |
| Navigation Compose     | Navegación                |
| Hilt (Dagger)          | Inyección de Dependencias |
| ViewModel              | Gestión de Estado         |
| StateFlow              | Reactividad               |
| Coroutines             | Programación Asíncrona    |
| Retrofit               | Consumo de APIs           |
| OkHttp                 | Cliente HTTP              |
| Coil                   | Carga de Imágenes         |
| Room                   | Persistencia Local        |
| Google Maps / MapLibre | Navegación del Campus     |
| MQTT                   | Comunicación IoT          |
| JWT                    | Autenticación Segura      |

---

# 🏗️ Arquitectura

El proyecto sigue una arquitectura basada en:

* MVVM (Model - View - ViewModel)
* Clean Architecture
* Principios SOLID
* Inyección de Dependencias con Hilt
* Single Source of Truth
* Unidirectional Data Flow (UDF)

La separación de responsabilidades garantiza un sistema escalable, mantenible y preparado para futuras expansiones.

---

# 📱 Funcionalidades Principales

## 🎓 Identidad Digital

* Inicio de sesión seguro.
* Gestión de sesiones.
* Generación de QR dinámico.
* Validación de identidad.

## 🤖 Asistente Inteligente

* Consultas académicas.
* Información institucional.
* Navegación guiada.
* Soporte basado en IA (RAG).

## 🗺️ Navegación Inteligente

* Mapa interactivo del campus.
* Búsqueda de edificios.
* Generación de rutas.
* Ubicación de servicios.

## 🚗 Robot de Guiado Autónomo

* Solicitud de acompañamiento.
* Seguimiento en tiempo real.
* Estado operativo del robot.
* Navegación asistida.

## 📢 Servicios Universitarios

* Noticias institucionales.
* Eventos.
* Avisos importantes.
* Servicios académicos.

## 🚨 Centro de Emergencias

* Contactos de emergencia.
* Reporte de incidentes.
* Acceso rápido a soporte.

---

# 🛠️ Instalación y Configuración

## 1. Prerrequisitos

Antes de comenzar asegúrate de tener instalado:

| Herramienta    | Versión            |
| -------------- | ------------------ |
| Android Studio | Narwhal o superior |
| JDK            | 21                 |
| Kotlin         | 2.x                |
| Android SDK    | API 35             |
| Git            | Última versión     |

Verificar instalación:

```bash
java -version
```

```bash
git --version
```

---

## 2. Clonar el Repositorio

```bash
git clone https://github.com/lattice-systems/space-ai-mobile.git
cd space-ai-mobile
```

---

## 3. Configurar Variables Locales

Crear o actualizar:

```properties
local.properties
```

Ejemplo:

```properties
MAPS_API_KEY=YOUR_KEY
BASE_URL=https://api.spaceia.com
MQTT_HOST=mqtt.spaceia.com
```

> ⚠️ Nunca subir credenciales reales al repositorio.

---

## 4. Sincronizar Dependencias

Abrir el proyecto en Android Studio.

Ejecutar:

```bash
Sync Project with Gradle Files
```

o desde terminal:

```bash
./gradlew build
```

---

## 5. Ejecutar la Aplicación

```bash
Run > Run 'app'
```

o

```bash
Shift + F10
```

---

# 📂 Estructura del Proyecto

```text
app/src/main/java/com/latticesystems/spaceia

├── core
│   ├── di
│   ├── navigation
│   ├── theme
│   ├── common
│   └── utils
│
├── data
│   ├── remote
│   ├── local
│   ├── repository
│   └── mapper
│
├── domain
│   ├── model
│   ├── repository
│   └── usecase
│
├── presentation
│   ├── login
│   ├── home
│   ├── qr
│   ├── map
│   ├── assistant
│   ├── robot
│   ├── profile
│   └── components
```

---

# 🔐 Seguridad

La aplicación implementa:

* JWT Authentication
* Control de acceso basado en roles (RBAC)
* Validación de QR dinámico
* Comunicación segura HTTPS
* Manejo seguro de sesiones

---

# 📈 Roadmap

### Sprint 1

* Arquitectura base.
* Login.
* Hilt.
* Navigation Compose.

### Sprint 2

* QR Dinámico.
* Perfil de usuario.
* Dashboard principal.

### Sprint 3

* Mapa inteligente.
* Navegación del campus.

### Sprint 4

* Asistente IA.
* Integración RAG.

### Sprint 5

* Robot autónomo.
* Seguimiento en tiempo real.

---

# 📄 Licencia

Proyecto académico desarrollado para la Universidad Tecnológica de León por el equipo **DeepCode** en colaboración con **Lattice Systems**.

---

<div align="center">

### 🚀 SpaceIA

### Smart Campus Ecosystem

Transformando la experiencia universitaria mediante Inteligencia Artificial, IoT y Automatización.

</div>

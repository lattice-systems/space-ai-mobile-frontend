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

| Tecnología             | Propósito                                     |
| ---------------------- | --------------------------------------------- |
| Kotlin 2.x             | Lenguaje principal                            |
| Jetpack Compose        | UI Declarativa y Animaciones Premium          |
| Material Design 3      | Sistema de Diseño Adaptativo                  |
| Navigation Compose     | Gestión de flujo de pantallas                 |
| Hilt (Dagger)          | Inyección de Dependencias                     |
| DataStore Proto        | Almacenamiento persistente fuertemente tipado |
**[Novedad]** **Android Keystore** | Cifrado AES/GCM de tokens y datos sensibles |
**[Novedad]** **Protobuf**         | Serialización eficiente de datos            |
| ViewModel              | Gestión de Estado (UDF)                       |
| StateFlow              | Reactividad y flujos de datos                 |
| Coroutines             | Programación Asíncrona                        |
| Retrofit 2             | Consumo de APIs REST                          |
| OkHttp 3               | Cliente HTTP con Interceptores                |
| Coil                   | Carga de Imágenes                             |
| Room                   | Persistencia Local (Próximamente)             |
| Google Maps / MapLibre | Navegación del Campus                         |
| MQTT                   | Comunicación IoT                              |
| JWT                    | Autenticación Segura                          |

---

# 🏗️ Arquitectura

El proyecto sigue una arquitectura basada en **Clean Architecture** y **MVVM**:

* **Presentation:** UI con Jetpack Compose, ViewModels y StateFlow.
* **Domain:** Entidades de negocio, interfaces de Repositorio y Casos de Uso (Use Cases).
* **Data:** Implementaciones de Repositorios, DTOs, Mappers y API Services.
* **Core:** Módulos transversales como Seguridad (Keystore), Red y Persistencia (DataStore).

---

# 🧪 Modo de Desarrollo (Mock Mode)

Para facilitar el desarrollo sin dependencia constante de un backend activo, la aplicación incluye un **Mock Repository** que simula el proceso de autenticación.

### Credenciales de Prueba (Mocks)

| Campo      | Valor               |
| ---------- |---------------------|
| **Email**    | `ejemplo@gmail.com` |
| **Password** | `admin12345`        |

> **Nota:** Este repositorio de prueba reside en el source set `debug` (`app/src/debug/java`), por lo que el código de prueba nunca se incluirá en las versiones de producción (`release`).

---

# 📱 Funcionalidades Principales

## ✨ Pantalla de Presentación (Splash)
* Animaciones premium de alta fidelidad.
* Efectos de partículas dinámicas y flotación de logo.
* Transición automática fluida al Login.

## 🎓 Identidad Digital
* Inicio de sesión seguro con validación en tiempo real.
* **[NUEVO]** **QR Dinámico Premium:** Contador de expiración animado con estilo Wallet y refresco automático.
* Almacenamiento cifrado de JWT mediante Keystore.
* Soporte para UI responsiva (Teléfonos y Tablets).

## 🗺️ Smart Campus & Navegación 3D
* **Campus 3D:** Visualización interactiva de los edificios de la UTL en 3D.
* **Geofencing:** Detección automática de entrada/salida del campus.
* **Navegación Inteligente:** Motor de rutas basado en el algoritmo A* y grafo universitario.
* **Experiencia Premium:** Renderizado de rutas animadas con estilo moderno (Material 3).
* Ver más detalles en: [Documentación del Mapa](docs/SMART_CAMPUS_MAP.md).

## 👤 Navegación y Perfil Premium
* **Navigation Drawer Premium:** Menú lateral responsivo con efecto blur de fondo, micro-interacciones hápticas y cabecera institucional.
* **Perfil del Estudiante:** Gestión completa de información personal, académica y de contacto con diseño tipo "Card Stack".
* **Seguridad Avanzada:** Cambio de contraseña con validaciones en tiempo real y feedback visual de alta fidelidad.
* **Skeleton Loading:** Shimmer effect profesional para transiciones de carga fluidas.

## ⚙️ Configuración y Accesibilidad
* **Módulo de Accesibilidad:** Control granular sobre animaciones, contraste, tamaño de texto y soporte avanzado para TalkBack. **[FUNCIONAL]**
* **Privacidad y Datos:** Gestión transparente de permisos reales y visualización de consumo de red (WiFi/Datos). **[FUNCIONAL]**
* **Diseño Inclusivo:** Áreas táctiles optimizadas y separación visual mejorada para estudiantes con necesidades especiales.
* **Persistencia Tipada:** Uso de Proto DataStore para el almacenamiento seguro de preferencias del usuario.
* **Documentación Legal:** Acceso a política de privacidad, términos y condiciones, y créditos institucionales.

---

# 🛠️ Instalación y Configuración

## 1. Prerrequisitos

* Android Studio Narwhal o superior.
* JDK 21.
* SDK de Android API 35.

## 2. Sincronizar y Ejecutar

```bash
# Sincronizar dependencias
./gradlew build

# Generar clases de Proto (si es necesario)
./gradlew :app:generateDebugProto

# Ejecutar la app
Run > app (Debug)
```

---

# 🔐 Seguridad

La seguridad es el pilar de SpaceIA Mobile:
* **Cifrado en Reposo:** Los tokens (Access & Refresh) se cifran con AES-GCM antes de guardarse en DataStore.
* **Hardware-Backed Security:** Las llaves maestras residen en el enclave seguro del dispositivo (Keystore).
* **Network Security:** Interceptor automático de JWT y configuración de tiempos de espera (timeouts).

---

# 📄 Licencia

Proyecto académico desarrollado para la Universidad Tecnológica de León por el equipo **DeepCode** en colaboración con **Lattice Systems**.

---

<div align="center">

### 🚀 SpaceIA — Smart Campus Ecosystem
Transformando la experiencia universitaria mediante Inteligencia Artificial, IoT y Automatización.

</div>

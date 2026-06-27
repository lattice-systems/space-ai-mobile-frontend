# Smart Campus UTL — Documentación del Sistema de Mapas y Navegación

Esta documentación detalla la implementación del módulo **Smart Campus** de SpaceIA Mobile, centrándose en el mapa 3D, el sistema de geolocalización y el motor de navegación inteligente para la Universidad Tecnológica de León.

## 🚀 Resumen del Proyecto
El sistema permite a los estudiantes visualizar el campus en 3D, determinar su ubicación exacta (dentro o fuera de la universidad) y calcular rutas óptimas entre edificios y puntos de interés (POIs).

---

## 🏗️ Arquitectura
El módulo sigue estrictamente los principios de **Clean Architecture**, **MVVM** y **Hilt** para asegurar escalabilidad y mantenibilidad.

- **Domain Layer**: Contiene modelos de negocio (`Building`, `GraphNode`, `NavigationRoute`) y casos de uso (`CalculateRouteUseCase`, `IsInsideCampusUseCase`).
- **Data Layer**: Maneja fuentes de datos como archivos GeoJSON y OSM, e implementa algoritmos de navegación (`AStarPathfinder`) y geofencing.
- **Presentation Layer**: Pantallas y ViewModels construidos íntegramente con **Jetpack Compose** y **Material Design 3**.

---

## 🗺️ Componentes Principales

### 1. Mapa 3D y Visualización (Sprint 1 & 4)
- **Motor**: MapLibre Android SDK.
- **Extrusión 3D**: Renderizado de edificios con alturas reales basadas en niveles.
- **Renderizado de Rutas**: Polilíneas de doble capa (estilo premium Google/Apple Maps) con animaciones progresivas (800ms).
- **Cámara Inteligente**: Ajuste automático del encuadre para mostrar rutas completas.

### 2. Geolocalización y Geofence (Sprint 2)
- **Precisión**: Uso de `FusedLocationProviderClient` con alta prioridad.
- **Geofencing Dinámico**: Algoritmo *Point In Polygon* basado en el perímetro real del campus extraído de `map.osm` (`amenity=university`).
- **Tolerancia**: Margen de 20 metros para manejar fluctuaciones de GPS.

### 3. Motor de Navegación A* (Sprint 3)
- **Fuente**: Grafo del campus cargado desde `campus_graph.json`.
- **Heurística**: Fórmula de **Haversine** para cálculos de distancia geoespacial precisos.
- **Eficiencia**: Implementación con `PriorityQueue` y caché en memoria para cálculos instantáneos (< 100ms).
- **Métricas**: Estimación de tiempo de caminata a una velocidad constante de 1.4 m/s.

---

## 📄 Fuentes de Verdad (Assets)
- `utl_3d.geojson`: Geometría 3D de los edificios.
- `map.osm`: Datos de OpenStreetMap para perímetros y límites.
- `campus_graph.json`: Red de nodos y conexiones para la navegación.
- `pois.json`: Ubicación y metadatos de los puntos de interés.

---

## 🛠️ Tecnologías Utilizadas
- **Kotlin 2.x**: Lenguaje principal.
- **Jetpack Compose**: UI declarativa.
- **Material 3**: Diseño moderno y responsive.
- **Hilt**: Inyección de dependencias.
- **Coroutines & Flow**: Manejo de asincronía y flujos de datos en tiempo real.
- **MapLibre SDK**: Visualización cartográfica.

---

## ✅ Verificación y Calidad
Se han implementado pruebas unitarias para validar:
- Lógica de Geofencing (*Point In Polygon*).
- Algoritmo A* (búsqueda de ruta más corta).
- Formateadores de distancia y tiempo.

---

## 📱 Experiencia de Usuario (UI/UX)
- **Indicador de Estado**: Visualización inmediata de si el usuario está dentro o fuera del campus.
- **Tarjetas de Información**: Detalles simplificados de edificios (pisos y categorías) con traducción al español.
- **Integración con IA**: Acceso directo al asistente de IA mediante el botón "Más información".

package org.utl.idgs901.space_ai_mobile.presentation.map

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression.*
import org.maplibre.android.style.layers.*
import org.maplibre.android.style.layers.PropertyFactory.*
import org.maplibre.android.style.light.Position
import org.maplibre.android.style.sources.GeoJsonSource
import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocationState
import org.utl.idgs901.space_ai_mobile.presentation.location.CampusLocationViewModel
import org.utl.idgs901.space_ai_mobile.presentation.location.components.CampusStatusIndicator
import org.utl.idgs901.space_ai_mobile.presentation.navigation.NavigationViewModel
import org.utl.idgs901.space_ai_mobile.presentation.navigation.NavigationRendererViewModel
import org.utl.idgs901.space_ai_mobile.presentation.navigation.NavigationRendererUiState
import org.utl.idgs901.space_ai_mobile.presentation.navigation.components.NavigationInfoCard
import org.utl.idgs901.space_ai_mobile.presentation.navigation.components.NavigationFloatingPanel
import org.utl.idgs901.space_ai_mobile.presentation.navigation.renderer.RouteRenderer
import org.maplibre.android.geometry.LatLngBounds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusMapScreen(
    viewModel: CampusMapViewModel = hiltViewModel(),
    locationViewModel: CampusLocationViewModel = hiltViewModel(),
    navigationViewModel: NavigationViewModel = hiltViewModel(),
    navigationRendererViewModel: NavigationRendererViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val locationState by locationViewModel.uiState.collectAsState()
    val navigationUiState by navigationViewModel.uiState.collectAsState()
    val navigationRendererUiState by navigationRendererViewModel.uiState.collectAsState()
    
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.selectedBuilding) {
        showBottomSheet = uiState.selectedBuilding != null
    }

    // Connect Navigation Engine with Renderer
    LaunchedEffect(navigationUiState.currentRoute) {
        navigationUiState.currentRoute?.let { route ->
            uiState.selectedBuilding?.let { building ->
                navigationRendererViewModel.setRoute(route, building.name)
            }
        } ?: navigationRendererViewModel.clearRoute()
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("Smart Campus UTL", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
                    actions = { 
                        CampusStatusIndicator(
                            state = locationState.campusState,
                            isLoading = locationState.isLoading
                        ) 
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                NavigationFloatingPanel(state = navigationRendererUiState)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            CampusMapView(
                uiState = uiState,
                locationState = locationState.campusState,
                userLocation = locationState.location?.let { LatLng(it.latitude, it.longitude) },
                navigationState = navigationRendererUiState,
                onBuildingClick = { viewModel.onEvent(CampusMapEvent.BuildingSelected(it)) }
            )

            if (locationState.campusState is CampusLocationState.Outside && !navigationRendererUiState.isRouteVisible) {
                OutsideWarning(Modifier.align(Alignment.TopCenter).padding(16.dp))
            }

            if (uiState.isLoading || navigationUiState.calculatingRoute) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF0D47A1))
            }

            NavigationInfoCard(
                state = navigationRendererUiState,
                onClose = { 
                    navigationViewModel.clearRoute()
                    viewModel.onEvent(CampusMapEvent.ClearSelection)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (showBottomSheet && !navigationRendererUiState.isRouteVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(CampusMapEvent.ClearSelection) },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Column {
                    uiState.selectedBuilding?.let { building ->
                        BuildingDetails(building)
                        
                        Button(
                            onClick = {
                                locationState.location?.let { loc ->
                                    navigationViewModel.calculateRoute(loc.latitude, loc.longitude, building)
                                }
                                showBottomSheet = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Directions, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cómo llegar")
                        }
                    }
                    
                    if (locationState.location != null) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                        UserLocationInfo(locationState)
                    }
                }
            }
        }
    }
}

@Composable
fun CampusMapView(
    uiState: CampusMapUiState,
    locationState: org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocationState,
    userLocation: LatLng?,
    navigationState: NavigationRendererUiState,
    onBuildingClick: (Building) -> Unit,
    routeRenderer: RouteRenderer = hiltViewModel<org.utl.idgs901.space_ai_mobile.presentation.navigation.renderer.RouteRendererWrapper>().renderer
) {
    val context = LocalContext.current
    val utlCenter = LatLng(21.0630, -101.5815)
    
    AndroidView(
        factory = { MapView(context) },
        modifier = Modifier.fillMaxSize(),
        update = { mapView ->
            mapView.getMapAsync { map ->
                map.setStyle(Style.Builder().fromUri("https://demotiles.maplibre.org/style.json")) { style ->
                    // 0. FORZAR Fondo Limpio (Eliminar amarillo de la capa base)
                    val backgroundLayer = style.getLayer("background")
                    if (backgroundLayer != null) {
                        backgroundLayer.setProperties(backgroundColor("#F1F5F9"))
                    } else {
                        // Si no existe, creamos una capa de fondo sólida para cubrir todo el lienzo
                        style.addLayerAt(BackgroundLayer("custom-background").withProperties(
                            backgroundColor("#F1F5F9")
                        ), 0)
                    }

                    // Forzar color de atmósfera neutra en la iluminación
                    style.light?.let { light ->
                        light.setAnchor("viewport")
                        light.setColor("#FFFFFF")
                        light.setIntensity(0.6f)
                        light.setPosition(Position(1.15f, 210f, 30f))
                    }

                    uiState.buildingsGeoJson?.let { geoJson ->
                        val sourceId = "buildings-source"
                        if (style.getSource(sourceId) == null) {
                            style.addSource(GeoJsonSource(sourceId, geoJson))
                            
                            // 1. Sombra de Contacto Profesional (Ambient Occlusion)
                            style.addLayer(FillLayer("buildings-shadow", sourceId).withProperties(
                                fillColor("#1E293B"),
                                fillOpacity(0.08f),
                                fillTranslate(arrayOf(1.5f, 1.5f))
                            ))

                            // 2. Capa Principal de Extrusión 3D (Acabado Moderno)
                            style.addLayer(FillExtrusionLayer("buildings-layer", sourceId).withProperties(
                                fillExtrusionHeight(get("height")),
                                fillExtrusionBase(0f),
                                fillExtrusionColor(
                                    interpolate(
                                        linear(), get("height"),
                                        stop(0, "#F1F5F9"), // Base clara y limpia
                                        stop(8, get("color")) // Color institucional
                                    )
                                ),
                                fillExtrusionOpacity(0.95f),
                                fillExtrusionVerticalGradient(true)
                            ))

                            // 3. Etiquetas Minimalistas de Alta Legibilidad
                            style.addLayer(SymbolLayer("labels-layer", sourceId).withProperties(
                                textField(get("name")),
                                textSize(10.5f),
                                textColor("#334155"),
                                textHaloColor("#FFFFFF"),
                                textHaloWidth(2.5f),
                                textHaloBlur(1f),
                                textAnchor("center"),
                                textTransform("uppercase"),
                                textLetterSpacing(0.1f),
                                textAllowOverlap(false)
                            ))
                        }
                    }
                    
                    uiState.walkwaysGeoJson?.let { geoJson ->
                        val sourceId = "walkways-source"
                        if (style.getSource(sourceId) == null) {
                            style.addSource(GeoJsonSource(sourceId, geoJson))
                            
                            // Senderos de concreto moderno
                            style.addLayer(LineLayer("walkways-layer", sourceId).withProperties(
                                lineColor("#CBD5E1"),
                                lineWidth(3f),
                                lineOpacity(0.6f),
                                lineCap("round"),
                                lineJoin("round")
                            ))
                        }
                    }

                    // User Location Marker
                    userLocation?.let { location ->
                        val userSourceId = "user-location-source"
                        val geoJson = """
                            {
                                "type": "FeatureCollection",
                                "features": [
                                    {
                                        "type": "Feature",
                                        "geometry": {
                                            "type": "Point",
                                            "coordinates": [${location.longitude}, ${location.latitude}]
                                        }
                                    }
                                ]
                            }
                        """.trimIndent()

                        val source = style.getSource(userSourceId) as? GeoJsonSource
                        if (source == null) {
                            style.addSource(GeoJsonSource(userSourceId, geoJson))
                            
                            // Blue dot for user
                            style.addLayer(CircleLayer("user-location-layer", userSourceId).withProperties(
                                circleColor("#2196F3"),
                                circleRadius(8f),
                                circleStrokeColor("#FFFFFF"),
                                circleStrokeWidth(2f)
                            ))
                        } else {
                            source.setGeoJson(geoJson)
                        }
                    }

                    // ROUTE RENDERING (Sprint 4)
                    if (navigationState.isRouteVisible && navigationState.route != null) {
                        val routeSourceId = "route-source"
                        val routeLayerId = "route-layer"
                        val routeOutlineId = "route-outline"
                        
                        val routeGeoJson = routeRenderer.generateAnimatedGeoJson(
                            navigationState.route, 
                            navigationState.animationProgress
                        )

                        val source = style.getSource(routeSourceId) as? GeoJsonSource
                        if (source == null) {
                            style.addSource(GeoJsonSource(routeSourceId, routeGeoJson))
                            
                            // Route Outline (Shadow/Border)
                            style.addLayerBelow(LineLayer(routeOutlineId, routeSourceId).withProperties(
                                lineColor("#CBD5E1"),
                                lineWidth(10f),
                                lineOpacity(0.5f),
                                lineCap("round"),
                                lineJoin("round")
                            ), "user-location-layer")

                            // Main Route Line
                            style.addLayerAbove(LineLayer(routeLayerId, routeSourceId).withProperties(
                                lineColor("#0D47A1"),
                                lineWidth(6f),
                                lineOpacity(0.9f),
                                lineCap("round"),
                                lineJoin("round")
                            ), routeOutlineId)
                        } else {
                            source.setGeoJson(routeGeoJson)
                        }
                        
                        // Destination Marker
                        val destNode = navigationState.route.path.last()
                        val destSourceId = "dest-location-source"
                        val destGeoJson = """
                            {"type":"Point","coordinates":[${destNode.longitude},${destNode.latitude}]}
                        """
                        val dSource = style.getSource(destSourceId) as? GeoJsonSource
                        if (dSource == null) {
                            style.addSource(GeoJsonSource(destSourceId, destGeoJson))
                            style.addLayerAbove(CircleLayer("dest-layer", destSourceId).withProperties(
                                circleColor("#F44336"),
                                circleRadius(10f),
                                circleStrokeColor("#FFFFFF"),
                                circleStrokeWidth(3f)
                            ), routeLayerId)
                        } else {
                            dSource.setGeoJson(destGeoJson)
                        }
                    } else {
                        // Clear route if not visible
                        style.getLayer("route-layer")?.let { style.removeLayer(it) }
                        style.getLayer("route-outline")?.let { style.removeLayer(it) }
                        style.getLayer("dest-layer")?.let { style.removeLayer(it) }
                        style.getSource("route-source")?.let { style.removeSource(it) }
                        style.getSource("dest-location-source")?.let { style.removeSource(it) }
                    }
                }

                // Camera logic (Sprint 4: Fit Bounds)
                if (navigationState.isRouteVisible && navigationState.route != null) {
                    val points = navigationState.route.path.map { LatLng(it.latitude, it.longitude) }
                    val bounds = LatLngBounds.Builder().includes(points).build()
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150), 1000)
                } else if (locationState is org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocationState.Inside && userLocation != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLng(userLocation), 1000)
                } else {
                    map.cameraPosition = CameraPosition.Builder()
                        .target(utlCenter)
                        .zoom(17.8)
                        .tilt(60.0)
                        .bearing(-15.0)
                        .build()
                }

                map.addOnMapClickListener { point ->
                    val features = map.queryRenderedFeatures(map.projection.toScreenLocation(point), "buildings-layer")
                    if (features.isNotEmpty()) {
                        val id = features[0].getStringProperty("id")
                        uiState.buildings.find { it.id == id }?.let { onBuildingClick(it) }
                    }
                    true
                }
            }
        }
    )
}

@Composable
fun CampusStatusIndicator(state: CampusLocationState) {
    val color = if (state is CampusLocationState.Inside) Color(0xFF4CAF50) else Color(0xFFF44336)
    val text = if (state is CampusLocationState.Inside) "Dentro del campus" else "Fuera del campus"
    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(end = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Box(modifier = Modifier.size(6.dp).background(color, RoundedCornerShape(3.dp)))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = color)
        }
    }
}

@Composable
fun OutsideWarning(modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color(0xFFE65100))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Estás fuera de la UTL. La exploración es visual.", fontSize = 12.sp, color = Color(0xFFE65100))
        }
    }
}

@Composable
fun BuildingDetails(building: Building) {
    Column(modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 48.dp)) {
        Text(building.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Text(building.category.uppercase(), fontSize = 12.sp, color = Color.Gray, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(24.dp))
        DetailRow("Pisos", building.levels.toString(), Icons.Default.Layers)
        DetailRow("Altura", "${building.height} metros", Icons.Default.Height)
        DetailRow("Estado", building.status, Icons.Default.CheckCircle)
    }
}

@Composable
fun DetailRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
        }
    }
}

@Composable
fun UserLocationInfo(state: org.utl.idgs901.space_ai_mobile.presentation.location.CampusLocationUiState) {
    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
        Text("Mi Ubicación", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(16.dp))
        state.location?.let { loc ->
            DetailRow("Latitud", loc.latitude.toString(), Icons.Default.LocationOn)
            DetailRow("Longitud", loc.longitude.toString(), Icons.Default.Map)
            DetailRow("Precisión", "${loc.accuracy} metros", Icons.Default.GpsFixed)
            
            val statusText = if (state.campusState is org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocationState.Inside) "Dentro del campus" else "Fuera del campus"
            val statusColor = if (state.campusState is org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocationState.Inside) Color(0xFF4CAF50) else Color(0xFFF44336)
            
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Icon(Icons.Default.Info, contentDescription = null, tint = statusColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(statusText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = statusColor)
            }
        }
    }
}

private fun Color.toArgb(): Int {
    return (alpha * 255).toInt() shl 24 or ((red * 255).toInt() shl 16) or ((green * 255).toInt() shl 8) or (blue * 255).toInt()
}

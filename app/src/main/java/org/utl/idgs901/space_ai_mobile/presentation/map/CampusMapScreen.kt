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
import com.google.android.gms.location.LocationServices
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
import org.utl.idgs901.space_ai_mobile.domain.map.model.CampusLocationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusMapScreen(
    viewModel: CampusMapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.selectedBuilding) {
        showBottomSheet = uiState.selectedBuilding != null
    }

    LaunchedEffect(Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    viewModel.onEvent(CampusMapEvent.UserLocationUpdated(it.latitude, it.longitude))
                }
            }
        } catch (e: SecurityException) {
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Smart Campus UTL", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
                actions = { CampusStatusIndicator(uiState.locationState) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            CampusMapView(
                uiState = uiState,
                onBuildingClick = { viewModel.onEvent(CampusMapEvent.BuildingSelected(it)) }
            )

            if (uiState.locationState is CampusLocationState.Outside) {
                OutsideWarning(Modifier.align(Alignment.TopCenter).padding(16.dp))
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF0D47A1))
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(CampusMapEvent.ClearSelection) },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                uiState.selectedBuilding?.let { BuildingDetails(it) }
            }
        }
    }
}

@Composable
fun CampusMapView(uiState: CampusMapUiState, onBuildingClick: (Building) -> Unit) {
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
                            style.addLayer(FillLayer("buildings-shadow", sourceId).apply {
                                setProperties(
                                    fillColor("#1E293B"),
                                    fillOpacity(0.08f),
                                    fillTranslate(arrayOf(1.5f, 1.5f))
                                )
                            })

                            // 2. Capa Principal de Extrusión 3D (Acabado Moderno)
                            style.addLayer(FillExtrusionLayer("buildings-layer", sourceId).apply {
                                setProperties(
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
                                )
                            })

                            // 3. Etiquetas Minimalistas de Alta Legibilidad
                            style.addLayer(SymbolLayer("labels-layer", sourceId).apply {
                                setProperties(
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
                                )
                            })
                        }
                    }
                    
                    uiState.walkwaysGeoJson?.let { geoJson ->
                        val sourceId = "walkways-source"
                        if (style.getSource(sourceId) == null) {
                            style.addSource(GeoJsonSource(sourceId, geoJson))
                            
                            // Senderos de concreto moderno
                            style.addLayer(LineLayer("walkways-layer", sourceId).apply {
                                setProperties(
                                    lineColor("#CBD5E1"),
                                    lineWidth(3f),
                                    lineOpacity(0.6f),
                                    lineCap("round"),
                                    lineJoin("round")
                                )
                            })
                        }
                    }
                }
                map.cameraPosition = CameraPosition.Builder()
                    .target(utlCenter)
                    .zoom(17.8)
                    .tilt(60.0) // Máximo tilt permitido para evitar errores de validación
                    .bearing(-15.0) // Ligera rotación para mejor perspectiva
                    .build()
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

private fun Color.toArgb(): Int {
    return (alpha * 255).toInt() shl 24 or ((red * 255).toInt() shl 16) or ((green * 255).toInt() shl 8) or (blue * 255).toInt()
}

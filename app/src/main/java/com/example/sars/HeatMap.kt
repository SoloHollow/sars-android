package com.example.sars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.sars.sampledata.AppBottomBar
import com.example.sars.sampledata.AppTopBar
import com.google.maps.android.compose.*
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng
import com.google.android.gms.maps.model.*

/**
 * Shared in-memory store for submitted animal reports.
 * When a report is submitted via Form.kt, its lat/lng is added here.
 * HeatMap reads from this list to render the heatmap overlay.
 */
object ReportStore {
    val reports = mutableStateListOf<AnimalReport>()

    fun addReport(report: AnimalReport) {
        reports.add(report)
    }
}

@Composable
fun HeatMap(navController: NavController) {
    var permissionGranted by remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableIntStateOf(0) }
    var showCamera by remember { mutableStateOf(false) }

    if (!permissionGranted) {
        RequestCameraPermission {
            permissionGranted = true
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Requesting Camera Permission...")
        }
    } else if (showCamera) {
        CameraScreen(navController, onClose = { showCamera = false })
    } else {
        Scaffold(
            topBar = { AppTopBar() },
            bottomBar = {
                AppBottomBar(
                    navController,
                    selectedIndex,
                    onAddClick = { showCamera = true })
            },
            floatingActionButton = {
                FloatingActionButtonWithNav(navController)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Heading ──────────────────────────────────────────────────
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Animal Sighting Heatmap",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                // ── Summary table ─────────────────────────────────────────────
                val reports = ReportStore.reports

                if (reports.isNotEmpty()) {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray, RoundedCornerShape(4.dp))
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text("City", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Animal", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Health", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Count", modifier = Modifier.weight(0.6f), fontWeight = FontWeight.Bold)
                    }

                    // Up to 5 most recent rows
                    reports.takeLast(5).reversed().forEach { report ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                report.city ?: "Unknown",
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp
                            )
                            Text(
                                report.animalType.name,
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp
                            )
                            Text(
                                report.healthStatus.name.replace("_", " "),
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp
                            )
                            Text(
                                "${report.countEstimate}",
                                modifier = Modifier.weight(0.6f),
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    Text(
                        "No reports yet. Submit a report using the camera button.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ── Google Map with Heatmap overlay ──────────────────────────
                HeatMapView(
                    reports = reports,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun HeatMapView(
    reports: List<AnimalReport>,
    modifier: Modifier = Modifier
) {
    // Default centre: Kollam, Kerala
    val defaultCenter = LatLng(8.8932, 76.6141)

    // Derive centre from reports if available
    val mapCenter = remember(reports) {
        if (reports.isNotEmpty()) {
            val avgLat = reports.map { it.latitude }.average()
            val avgLng = reports.map { it.longitude }.average()
            LatLng(avgLat, avgLng)
        } else defaultCenter
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapCenter, 12f)
    }

    // Re-centre when new reports arrive
    LaunchedEffect(mapCenter) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(mapCenter, 12f)
    }

    // Build weighted points for heatmap
    val weightedPoints = remember(reports) {
        if (reports.isNotEmpty()) {
            reports.map { report ->
                // Use countEstimate as weight so larger packs appear hotter
                WeightedLatLng(
                    LatLng(report.latitude, report.longitude),
                    report.countEstimate.toDouble().coerceAtLeast(1.0)
                )
            }
        } else null
    }

    // Build tile provider outside of a render-sensitive block
    val heatmapTileProvider = remember(weightedPoints) {
        weightedPoints?.let { pts ->
            HeatmapTileProvider.Builder()
                .weightedData(pts)
                .radius(50)
                .build()
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        if (heatmapTileProvider != null) {
            // Render heatmap tile overlay
            TileOverlay(
                tileProvider = heatmapTileProvider
            )
        }

        // Also show individual markers for precise location
        reports.forEach { report ->
            Marker(
                state = MarkerState(position = LatLng(report.latitude, report.longitude)),
                title = "${report.animalType.name} – ${report.city ?: "Unknown"}",
                snippet = "${report.healthStatus.name.replace("_", " ")} | Count: ${report.countEstimate}"
            )
        }

        // Show default centre marker when no reports yet
        if (reports.isEmpty()) {
            Marker(
                state = MarkerState(position = defaultCenter),
                title = "Kollam, Kerala",
                snippet = "Reports will appear here"
            )
        }
    }
}

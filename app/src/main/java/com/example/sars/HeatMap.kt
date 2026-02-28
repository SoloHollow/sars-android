package com.example.sars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// Shared in-memory store.
// Reports are added here locally AND loaded from the backend on launch.
// ─────────────────────────────────────────────────────────────────────────────
object ReportStore {
    val reports = mutableStateListOf<AnimalReport>()

    fun addReport(report: AnimalReport) {
        // Avoid duplicates when the same report comes back from the server
        if (reports.none { it.id != null && it.id == report.id }) {
            reports.add(report)
        }
    }

    fun replaceAll(incoming: List<AnimalReport>) {
        reports.clear()
        reports.addAll(incoming)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Grouped summary row – combines all reports for one city / animal-type pair
// ─────────────────────────────────────────────────────────────────────────────
data class GroupedReport(
    val city: String,
    val animalType: AnimalType,
    val dominantHealth: HealthStatus,   // most severe health status in the group
    val totalCount: Int,                // sum of countEstimate
    val reportCount: Int,               // how many individual reports
    val avgLat: Double,
    val avgLng: Double
)

private fun List<AnimalReport>.grouped(): List<GroupedReport> {
    val healthOrder = listOf(
        HealthStatus.NORMAL,
        HealthStatus.INJURED,
        HealthStatus.DISEASED,
        HealthStatus.RABIES_SUSPECT
    )
    return groupBy { Pair(it.city ?: "Unknown", it.animalType) }
        .map { (key, group) ->
            GroupedReport(
                city = key.first,
                animalType = key.second,
                dominantHealth = group
                    .maxByOrNull { healthOrder.indexOf(it.healthStatus) }!!
                    .healthStatus,
                totalCount = group.sumOf { it.countEstimate },
                reportCount = group.size,
                avgLat = group.map { it.latitude }.average(),
                avgLng = group.map { it.longitude }.average()
            )
        }
        .sortedByDescending { it.totalCount }
}

// ─────────────────────────────────────────────────────────────────────────────
// Main HeatMap screen
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HeatMap(navController: NavController) {
    var permissionGranted by remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableIntStateOf(0) }
    var showCamera by remember { mutableStateOf(false) }

    if (!permissionGranted) {
        RequestCameraPermission { permissionGranted = true }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Requesting Camera Permission…")
        }
    } else if (showCamera) {
        CameraScreen(navController, onClose = { showCamera = false })
    } else {
        Scaffold(
            topBar = { AppTopBar() },
            bottomBar = {
                AppBottomBar(navController, selectedIndex, onAddClick = { showCamera = true })
            },
            floatingActionButton = { FloatingActionButtonWithNav(navController) }
        ) { innerPadding ->
            HeatMapContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Content – split into fixed-height table + expanding map
// Using a Column with explicit sizes so the map never shrinks.
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun HeatMapContent(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val reports = ReportStore.reports
    val grouped = remember(reports.toList()) { reports.toList().grouped() }

    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Fetch from backend when the screen opens
    LaunchedEffect(Unit) {
        isLoading = true
        errorMsg = null
        scope.launch {
            ApiService.fetchReports()
                .onSuccess { fetched ->
                    ReportStore.replaceAll(fetched)
                    isLoading = false
                }
                .onFailure { err ->
                    errorMsg = "Could not load reports: ${err.message}"
                    isLoading = false
                }
        }
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // ── Title ──────────────────────────────────────────────────────────
        Text(
            "Animal Sighting Heatmap",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // ── Loading / Error banner ─────────────────────────────────────────
        when {
            isLoading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            errorMsg != null -> Text(
                errorMsg!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // ── Grouped summary table — fixed height so map stays stable ───────
        // Table takes only as much space as it needs (max ~240 dp)
        if (grouped.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp)  // ← caps table height; map stays fixed below
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF4CAF50), RoundedCornerShape(6.dp))
                        .padding(vertical = 8.dp, horizontal = 6.dp)
                ) {
                    TableHeaderCell("City", Modifier.weight(1.4f))
                    TableHeaderCell("Animal", Modifier.weight(1f))
                    TableHeaderCell("Health", Modifier.weight(1.2f))
                    TableHeaderCell("Total", Modifier.weight(0.6f))
                    TableHeaderCell("Rpts", Modifier.weight(0.5f))
                }

                Spacer(Modifier.height(2.dp))

                // Scrollable rows — won't push the map off screen
                LazyColumn {
                    items(grouped) { g ->
                        GroupedRow(g)
                        Divider(color = Color.LightGray, thickness = 0.5.dp)
                    }
                }
            }
        } else if (!isLoading) {
            Text(
                "No reports yet. Submit one using the camera button.",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        // ── Map — takes ALL remaining space, never shrinks ─────────────────
        HeatMapView(
            reports = reports.toList(),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)   // fills whatever space is left after the table
        )
    }
}

@Composable
private fun TableHeaderCell(text: String, modifier: Modifier) {
    Text(
        text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = Color.White
    )
}

@Composable
private fun GroupedRow(g: GroupedReport) {
    val healthColor = when (g.dominantHealth) {
        HealthStatus.NORMAL -> Color(0xFF388E3C)
        HealthStatus.INJURED -> Color(0xFFF57C00)
        HealthStatus.DISEASED -> Color(0xFFD32F2F)
        HealthStatus.RABIES_SUSPECT -> Color(0xFF7B1FA2)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(g.city, modifier = Modifier.weight(1.4f), fontSize = 13.sp)
        Text(g.animalType.name, modifier = Modifier.weight(1f), fontSize = 13.sp)
        Text(
            g.dominantHealth.name.replace("_", " "),
            modifier = Modifier.weight(1.2f),
            fontSize = 12.sp,
            color = healthColor,
            fontWeight = FontWeight.SemiBold
        )
        Text("${g.totalCount}", modifier = Modifier.weight(0.6f), fontSize = 13.sp)
        Text("${g.reportCount}", modifier = Modifier.weight(0.5f), fontSize = 13.sp)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Google Map with heatmap tile overlay
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HeatMapView(
    reports: List<AnimalReport>,
    modifier: Modifier = Modifier
) {
    val defaultCenter = LatLng(8.8932, 76.6141) // Kollam, Kerala

    val mapCenter = remember(reports) {
        if (reports.isNotEmpty()) LatLng(
            reports.map { it.latitude }.average(),
            reports.map { it.longitude }.average()
        ) else defaultCenter
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapCenter, 12f)
    }

    LaunchedEffect(mapCenter) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(mapCenter, 12f)
    }

    val weightedPoints = remember(reports) {
        reports.takeIf { it.isNotEmpty() }?.map { r ->
            WeightedLatLng(
                LatLng(r.latitude, r.longitude),
                r.countEstimate.toDouble().coerceAtLeast(1.0)
            )
        }
    }

    val heatmapTileProvider = remember(weightedPoints) {
        weightedPoints?.let {
            HeatmapTileProvider.Builder()
                .weightedData(it)
                .radius(50)
                .build()
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        heatmapTileProvider?.let { provider ->
            TileOverlay(tileProvider = provider)
        }

        // One marker per grouped location (not per individual report)
        reports
            .groupBy { Pair(it.city ?: "Unknown", it.animalType) }
            .forEach { (key, group) ->
                val avgLat = group.map { it.latitude }.average()
                val avgLng = group.map { it.longitude }.average()
                val total = group.sumOf { it.countEstimate }
                Marker(
                    state = MarkerState(position = LatLng(avgLat, avgLng)),
                    title = "${key.second.name} in ${key.first}",
                    snippet = "Total: $total animals | ${group.size} report(s)"
                )
            }

        if (reports.isEmpty()) {
            Marker(
                state = MarkerState(position = defaultCenter),
                title = "Kollam, Kerala",
                snippet = "Reports will appear here"
            )
        }
    }
}

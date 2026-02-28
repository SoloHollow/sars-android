package com.example.sars

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(
    navController: NavController,
    detectedStreet: String = ""
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Form state
    var animalType by rememberSaveable { mutableStateOf(AnimalType.DOG) }
    var isPack by rememberSaveable { mutableStateOf(false) }
    var countEstimate by rememberSaveable { mutableStateOf("1") }
    var healthStatus by rememberSaveable { mutableStateOf(HealthStatus.NORMAL) }
    var extraInfo by rememberSaveable { mutableStateOf("") }
    var contact by rememberSaveable { mutableStateOf("") }

    var location by rememberSaveable { mutableStateOf(detectedStreet) }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var city by remember { mutableStateOf<String?>(null) }
    var state by remember { mutableStateOf<String?>(null) }

    var isLoadingLocation by remember { mutableStateOf(false) }
    var showAnimalTypeMenu by remember { mutableStateOf(false) }
    var showHealthStatusMenu by remember { mutableStateOf(false) }

    // Location services
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Function to check if location permission is granted
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    // Function to get location
    fun getLocation() {
        if (!hasLocationPermission()) {
            location = "Permission not granted"
            return
        }

        isLoadingLocation = true

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                if (lastLoc != null) {
                    latitude = lastLoc.latitude
                    longitude = lastLoc.longitude

                    scope.launch(Dispatchers.IO) {
                        try {
                            val geocoder = Geocoder(context, Locale.getDefault())

                            @Suppress("DEPRECATION")
                            val addresses = geocoder.getFromLocation(
                                lastLoc.latitude,
                                lastLoc.longitude,
                                1
                            )

                            withContext(Dispatchers.Main) {
                                addresses?.firstOrNull()?.let { address ->
                                    city = address.locality ?: address.subAdminArea
                                    state = address.adminArea

                                    location = buildString {
                                        address.thoroughfare?.let { append(it) }
                                        address.subLocality?.let {
                                            if (isNotEmpty()) append(", ")
                                            append(it)
                                        }
                                        city?.let {
                                            if (isNotEmpty()) append(", ")
                                            append(it)
                                        }
                                    }.ifEmpty {
                                        "${lastLoc.latitude}, ${lastLoc.longitude}"
                                    }
                                } ?: run {
                                    location = "${lastLoc.latitude}, ${lastLoc.longitude}"
                                }
                                isLoadingLocation = false
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            withContext(Dispatchers.Main) {
                                location = "${lastLoc.latitude}, ${lastLoc.longitude}"
                                isLoadingLocation = false
                            }
                        }
                    }
                } else {
                    val cancellationTokenSource = CancellationTokenSource()
                    fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        cancellationTokenSource.token
                    ).addOnSuccessListener { currentLoc ->
                        if (currentLoc != null) {
                            latitude = currentLoc.latitude
                            longitude = currentLoc.longitude

                            scope.launch(Dispatchers.IO) {
                                try {
                                    val geocoder = Geocoder(context, Locale.getDefault())

                                    @Suppress("DEPRECATION")
                                    val addresses = geocoder.getFromLocation(
                                        currentLoc.latitude,
                                        currentLoc.longitude,
                                        1
                                    )

                                    withContext(Dispatchers.Main) {
                                        addresses?.firstOrNull()?.let { address ->
                                            city = address.locality ?: address.subAdminArea
                                            state = address.adminArea

                                            location = buildString {
                                                address.thoroughfare?.let { append(it) }
                                                address.subLocality?.let {
                                                    if (isNotEmpty()) append(", ")
                                                    append(it)
                                                }
                                                city?.let {
                                                    if (isNotEmpty()) append(", ")
                                                    append(it)
                                                }
                                            }.ifEmpty {
                                                "${currentLoc.latitude}, ${currentLoc.longitude}"
                                            }
                                        } ?: run {
                                            location = "${currentLoc.latitude}, ${currentLoc.longitude}"
                                        }
                                        isLoadingLocation = false
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    withContext(Dispatchers.Main) {
                                        location = "${currentLoc.latitude}, ${currentLoc.longitude}"
                                        isLoadingLocation = false
                                    }
                                }
                            }
                        } else {
                            location = "Unable to detect location - try again"
                            isLoadingLocation = false
                        }
                    }.addOnFailureListener { e ->
                        location = "Unable to detect location: ${e.message}"
                        isLoadingLocation = false
                    }
                }
            }.addOnFailureListener { e ->
                location = "Error getting location: ${e.message}"
                isLoadingLocation = false
            }
        } catch (e: SecurityException) {
            location = "Permission denied"
            isLoadingLocation = false
        }
    }

    // Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            getLocation()
        } else {
            location = "Permission denied - enable in settings"
            isLoadingLocation = false
        }
    }

    // Captured image from previous screen
    val capturedBitmap = remember {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<ByteArray?>("capturedImage", null)
            ?.value
            ?.toBitmap()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CapturedImageBox(bitmap = capturedBitmap)

        // Animal Type
        Column {
            Text("Animal Type", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = showAnimalTypeMenu,
                onExpandedChange = { showAnimalTypeMenu = it }
            ) {
                OutlinedTextField(
                    value = animalType.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showAnimalTypeMenu) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = showAnimalTypeMenu,
                    onDismissRequest = { showAnimalTypeMenu = false }
                ) {
                    AnimalType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                animalType = type
                                showAnimalTypeMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Is Pack
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Is this a pack/group?", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = isPack,
                onCheckedChange = { isPack = it }
            )
        }

        // Count Estimate
        FormField(
            label = "Estimated Count",
            value = countEstimate,
            onValueChange = { if (it.all { char -> char.isDigit() }) countEstimate = it },
            placeholder = "1",
            keyboardType = KeyboardType.Number
        )

        // Health Status
        Column {
            Text("Health Status", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = showHealthStatusMenu,
                onExpandedChange = { showHealthStatusMenu = it }
            ) {
                OutlinedTextField(
                    value = healthStatus.name.replace("_", " "),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showHealthStatusMenu) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = showHealthStatusMenu,
                    onDismissRequest = { showHealthStatusMenu = false }
                ) {
                    HealthStatus.values().forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.name.replace("_", " ")) },
                            onClick = {
                                healthStatus = status
                                showHealthStatusMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Location field with detect button
        Column {
            Text("Location", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = location,
                    onValueChange = { location = it },
                    placeholder = { Text("Detected location") },
                    enabled = !isLoadingLocation
                )

                IconButton(
                    onClick = {
                        if (hasLocationPermission()) {
                            getLocation()
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    if (isLoadingLocation) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Detect Location",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Extra Info
        FormField(
            label = "Additional Information",
            value = extraInfo,
            onValueChange = { extraInfo = it },
            placeholder = "Any additional details...",
            modifier = Modifier.height(100.dp),
            maxLines = 5
        )

        // Contact Number
        FormField(
            label = "Contact Number",
            value = contact,
            onValueChange = { contact = it },
            placeholder = "e.g. 9876543210",
            keyboardType = KeyboardType.Phone
        )

        Button(
            onClick = {
                if (latitude != null && longitude != null) {
                    scope.launch {
                        submitReport(
                            animalType = animalType,
                            isPack = isPack,
                            countEstimate = countEstimate.toIntOrNull() ?: 1,
                            healthStatus = healthStatus,
                            latitude = latitude!!,
                            longitude = longitude!!,
                            city = city,
                            state = state,
                            extraInfo = extraInfo.ifEmpty { null },
                            contact = contact
                        )
                    }
                } else {
                    Log.e("ReportSubmit", "Location not detected")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = latitude != null && longitude != null
        ) {
            Text("Submit Report")
        }
    }
}

@Composable
private fun CapturedImageBox(bitmap: android.graphics.Bitmap?) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color.Gray, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Placeholder",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            readOnly = readOnly,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

private suspend fun submitReport(
    animalType: AnimalType,
    isPack: Boolean,
    countEstimate: Int,
    healthStatus: HealthStatus,
    latitude: Double,
    longitude: Double,
    city: String?,
    state: String?,
    extraInfo: String?,
    contact: String
) {
    val report = AnimalReport(
        reportedBy = java.util.UUID.randomUUID(), // Replace with real user UUID from auth
        latitude = latitude,
        longitude = longitude,
        city = city,
        state = state,
        animalType = animalType,
        isPack = isPack,
        countEstimate = countEstimate,
        healthStatus = healthStatus,
        extraInfo = extraInfo
    )

    // 1. Add locally immediately so the heatmap updates without waiting for the server
    ReportStore.addReport(report)
    Log.d("ReportSubmit", "Report added locally: $report")

    // 2. Also POST to backend; if it succeeds, replace the local copy with the server version
    ApiService.submitReport(report)
        .onSuccess { saved ->
            // Server may have assigned a real UUID – update the store
            ReportStore.reports.remove(report)
            ReportStore.addReport(saved)
            Log.d("ReportSubmit", "Report saved on server: $saved")
        }
        .onFailure { err ->
            Log.e("ReportSubmit", "Server submit failed (kept locally): ${err.message}")
        }
}
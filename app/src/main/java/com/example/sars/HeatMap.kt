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
import com.google.android.gms.maps.model.*


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
        CameraScreen(navController,onClose = { showCamera = false })
    } else {
        Scaffold(
            topBar = { AppTopBar(title = "Pawnder") },
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
                // Heading
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Latest Information:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("Area", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("Number", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("Time", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                }

                // Table Rows
                val entries = listOf(
                    Triple("Karicode", "23", "10:45 AM"),
                    Triple("Peroor", "15", "11:10 AM"),
                    Triple("Kuttichira", "9", "09:30 AM")
                )

                entries.forEach { (area, number, time) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(area, modifier = Modifier.weight(1f))
                        Text(number, modifier = Modifier.weight(1f))
                        Text(time, modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Google Map
                val cameraPositionState = rememberCameraPositionState {
                    position =
                        CameraPosition.fromLatLngZoom(LatLng(8.8912, 76.5952), 10f) // Karicode
                }

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(8.8912, 76.5952)),
                        title = "Karicode"
                    )
                    Marker(
                        state = MarkerState(position = LatLng(8.8821, 76.5820)),
                        title = "Peroor"
                    )
                    Marker(
                        state = MarkerState(position = LatLng(11.2500, 75.7667)),
                        title = "Kuttichira"
                    )
                }
            }
        }
    }
}
